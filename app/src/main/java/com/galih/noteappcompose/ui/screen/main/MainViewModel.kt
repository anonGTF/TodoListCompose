package com.galih.noteappcompose.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.usecase.GetTodosUseCase
import com.galih.noteappcompose.domain.usecase.ToggleFinishTodoUseCase
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
class MainViewModel @Inject constructor(
    getTodosUseCase: GetTodosUseCase,
    private val toggleFinishTodoUseCase: ToggleFinishTodoUseCase
) : ViewModel() {
    val todoState = getTodosUseCase(Unit)

    private val _toggleStateStream = MutableStateFlow(ActionState())
    val toggleStateStream = _toggleStateStream.asStateFlow()

    private var toggleState: ActionState
        get() = _toggleStateStream.value
        set(newState) {
            _toggleStateStream.update { newState }
        }

    fun toggleFinished(todo: Todo) = viewModelScope.launch {
        toggleFinishTodoUseCase(todo).collect { result ->
            result.map(
                onSuccess = {
                    toggleState = toggleState.copy(
                        isLoading = false,
                        eventSucceed = triggered
                    )
                },
                onError = {
                    toggleState = toggleState.copy(
                        isLoading = false,
                        eventFailed = triggered(it.message.toString())
                    )
                },
                onLoading = {
                    toggleState = toggleState.copy(
                        isLoading = true,
                    )
                }
            )
        }
    }

    fun onConsumedToggleSucceed(){
        toggleState = toggleState.copy(eventSucceed = consumed)
    }

    fun onConsumedToggleFailed(){
        toggleState = toggleState.copy(eventFailed = consumed())
    }
}