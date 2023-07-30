package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
import com.galih.noteappcompose.util.Resource
import com.galih.noteappcompose.util.Utils.wrapWithResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) : UseCase<Flow<Resource<List<Todo>>>, Unit>() {
    override fun run(params: Unit): Flow<Resource<List<Todo>>> =
        repository.getTodos().wrapWithResource()
}
