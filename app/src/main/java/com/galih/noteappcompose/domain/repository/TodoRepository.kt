package com.galih.noteappcompose.domain.repository

import com.galih.noteappcompose.domain.entity.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>
    suspend fun upsertTodo(todo: Todo)
    suspend fun removeTodo(todo: Todo)
}