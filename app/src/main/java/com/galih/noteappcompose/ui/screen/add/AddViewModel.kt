package com.galih.noteappcompose.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.usecase.UpsertTodoUseCase
import com.galih.noteappcompose.ui.model.ActionState
import com.galih.noteappcompose.util.Utils.map
import com.galih.noteappcompose.util.Utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val upsertTodoUseCase: UpsertTodoUseCase
) : ViewModel() {

    private val _addStateStream = MutableStateFlow(ActionState())
    val addStateStream = _addStateStream.asStateFlow()

    private var addState: ActionState
        get() = _addStateStream.value
        set(newState) {
            _addStateStream.update { newState }
        }

    fun addTodo(title: String, description: String, date: LocalDate) = viewModelScope.launch {
        val todo = Todo(
            0,
            title,
            description,
            date.toDate(),
            false
        )
        upsertTodoUseCase(todo).collect { result ->
            result.map(
                onSuccess = {
                    addState = addState.copy(
                        isLoading = false,
                        eventSucceed = triggered
                    )
                },
                onError = {
                    addState = addState.copy(
                        isLoading = false,
                        eventFailed = triggered(it.message.toString())
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
        addState = addState.copy(eventSucceed = consumed)
    }

    fun onConsumedAddingFailed(){
        addState = addState.copy(eventFailed = consumed())
    }
}