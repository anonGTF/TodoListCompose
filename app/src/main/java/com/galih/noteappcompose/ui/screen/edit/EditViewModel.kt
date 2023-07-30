package com.galih.noteappcompose.ui.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.usecase.RemoveTodoUseCase
import com.galih.noteappcompose.domain.usecase.UpsertTodoUseCase
import com.galih.noteappcompose.ui.model.ActionState
import com.galih.noteappcompose.util.Utils.map
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val upsertTodoUseCase: UpsertTodoUseCase,
    private val removeTodoUseCase: RemoveTodoUseCase
) : ViewModel() {
    private val _editStateStream = MutableStateFlow(ActionState())
    val editStateStream = _editStateStream.asStateFlow()

    private var editState: ActionState
        get() = _editStateStream.value
        set(newState) {
            _editStateStream.update { newState }
        }

    fun editTodo(todo: Todo) = viewModelScope.launch {
        upsertTodoUseCase(todo).collect { result ->
            result.map(
                onSuccess = {
                    editState = editState.copy(
                        isLoading = false,
                        eventSucceed = triggered
                    )
                },
                onError = {
                    editState = editState.copy(
                        isLoading = false,
                        eventFailed = triggered(it.message.toString())
                    )
                },
                onLoading = {
                    editState = editState.copy(
                        isLoading = true,
                    )
                }
            )
        }
    }

    fun onConsumedEditSucceed(){
        editState = editState.copy(eventSucceed = consumed)
    }

    fun onConsumedEditFailed(){
        editState = editState.copy(eventFailed = consumed())
    }

    private val _removeStateStream = MutableStateFlow(ActionState())
    val removeStateStream = _removeStateStream.asStateFlow()

    private var removeState: ActionState
        get() = _removeStateStream.value
        set(newState) {
            _removeStateStream.update { newState }
        }

    fun removeTodo(todo: Todo) = viewModelScope.launch {
        removeTodoUseCase(todo).collect { result ->
            result.map(
                onSuccess = {
                    removeState = removeState.copy(
                        isLoading = false,
                        eventSucceed = triggered
                    )
                },
                onError = {
                    removeState = removeState.copy(
                        isLoading = false,
                        eventFailed = triggered(it.message.toString())
                    )
                },
                onLoading = {
                    removeState = removeState.copy(
                        isLoading = true,
                    )
                }
            )
        }
    }

    fun onConsumedRemoveSucceed(){
        removeState = removeState.copy(eventSucceed = consumed)
    }

    fun onConsumedRemoveFailed(){
        removeState = removeState.copy(eventFailed = consumed())
    }
}