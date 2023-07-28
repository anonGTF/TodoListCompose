package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
import com.galih.noteappcompose.util.Resource
import com.galih.noteappcompose.util.fetch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpsertTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) : UseCase<Flow<Resource<Unit>>, Todo>() {
    override fun run(params: Todo): Flow<Resource<Unit>> = fetch {
        repository.upsertTodo(params)
    }
}