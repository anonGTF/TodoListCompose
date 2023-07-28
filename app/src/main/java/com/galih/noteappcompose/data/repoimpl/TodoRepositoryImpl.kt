package com.galih.noteappcompose.data.repoimpl

import com.galih.noteappcompose.data.datasource.local.TodoDao
import com.galih.noteappcompose.data.mapper.Mapper
import com.galih.noteappcompose.data.model.TodoModel
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val db: TodoDao,
    private val mapper: Mapper<TodoModel, Todo>
) : TodoRepository {
    override fun getTodos(): Flow<List<Todo>> =
        db.getAllTodos().map { list -> list.map { mapper.mapFromModel(it) } }

    override suspend fun upsertTodo(todo: Todo) = db.upsertTodo(mapper.mapFromEntity(todo))

    override suspend fun removeTodo(todo: Todo) = db.deleteTodo(mapper.mapFromEntity(todo))
}