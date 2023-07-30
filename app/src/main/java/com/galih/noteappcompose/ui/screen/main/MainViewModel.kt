package com.galih.noteappcompose.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.galih.noteappcompose.domain.usecase.GetTodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel() {

    val todos by lazy { getTodosUseCase(Unit).asLiveData() }

}