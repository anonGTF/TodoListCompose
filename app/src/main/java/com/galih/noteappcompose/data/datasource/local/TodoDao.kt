package com.galih.noteappcompose.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galih.noteappcompose.data.model.TodoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)
}