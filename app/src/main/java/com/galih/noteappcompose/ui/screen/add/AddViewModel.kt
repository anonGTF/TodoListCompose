package com.galih.noteappcompose.ui.screen.add

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.usecase.UpsertTodoUseCase
import com.galih.noteappcompose.util.Resource
import com.galih.noteappcompose.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val upsertTodoUseCase: UpsertTodoUseCase
) : ViewModel() {

    private val _addStateStream = MutableStateFlow<ActionState<Unit>>(ActionState())
    val addStateStream = _addStateStream.asStateFlow()

    private var addState: ActionState<Unit>
        get() = _addStateStream.value
        set(newState) {
            _addStateStream.update { newState }
        }

    fun addTodo(title: String, description: String) = viewModelScope.launch {
        val todo = Todo(
            0,
            title,
            description,
            Utils.getCurrentDateTime(),
            false
        )
        upsertTodoUseCase(todo).collect { result ->
            result.toState(
                onSuccess = {
                    addState = addState.copy(
                        isLoading = false,
                        addingSucceed = triggered
                    )
                },
                onError = {
                    addState = addState.copy(
                        isLoading = false,
                        addingFailed = triggered(it.message.toString())
                    )
                },
                onLoading = {
                    addState = addState.copy(
                        isLoading = true,
                    )
                }
            )
        }
    }

    fun onConsumedAddingSucceed(){
        addState = addState.copy(addingSucceed = consumed)
    }

    fun onConsumedAddingFailed(){
        addState = addState.copy(addingFailed = consumed())
    }
}

data class ActionState <T> (
    val isLoading: Boolean = false,
    val addingSucceed: StateEvent = consumed,
    val addingFailed: StateEventWithContent<String> = consumed()
)

inline fun <T> Resource<T>.toState(
    onSuccess: (Resource.Success<T>) -> Unit,
    onError: (Resource.Error<T>) -> Unit,
    onLoading: (Resource.Loading<T>) -> Unit,
) {
    when(this) {
        is Resource.Success -> onSuccess(this)
        is Resource.Error -> onError(this)
        is Resource.Loading -> onLoading(this)
    }
}