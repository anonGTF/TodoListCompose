package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
import com.galih.noteappcompose.util.Resource
import com.galih.noteappcompose.util.fetch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) : UseCase<Flow<Resource<List<Todo>>>, Unit>() {
    override fun run(params: Unit): Flow<Resource<List<Todo>>>  = fetch {
        repository.getTodos().first()
    }
}