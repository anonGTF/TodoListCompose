package com.galih.noteappcompose.data.repoimpl

import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.domain.repository.TodoRepository
import com.galih.noteappcompose.util.Faker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTodoRepository : TodoRepository {
    val fakeTodos: MutableList<Todo> = mutableListOf()

    fun generateRandomTodos() {
        val todos = Faker.aList {
            Todo(
                id = Faker.anInt(),
                title = Faker.aString(),
                description = Faker.aString(),
                dueDate = Faker.aDate(),
                isFinished = Faker.aBoolean()
            )
        }
        fakeTodos.addAll(todos)
    }

    fun generateRandomTodo() =
        Todo(
            id = Faker.anInt(),
            title = Faker.aString(),
            description = Faker.aString(),
            dueDate = Faker.aDate(),
            isFinished = false
        )

    override fun getTodos(): Flow<List<Todo>> = flowOf(fakeTodos)

    // IMITATE REPLACE ON CONFLICT STRATEGY FROM ROOM
    override suspend fun upsertTodo(todo: Todo) {
        val existingTodo = fakeTodos.find { it.id == todo.id }

        if (existingTodo != null) {
            val index = fakeTodos.indexOf(existingTodo)
            fakeTodos[index] = todo
        } else {
            fakeTodos.add(todo)
        }
    }

    override suspend fun removeTodo(todo: Todo) {
        fakeTodos.remove(todo)
    }
}