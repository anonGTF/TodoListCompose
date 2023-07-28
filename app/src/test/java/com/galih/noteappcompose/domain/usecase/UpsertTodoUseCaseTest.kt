package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.data.repoimpl.FakeTodoRepository
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.util.Faker
import com.galih.noteappcompose.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UpsertTodoUseCaseTest {
    private lateinit var upsertTodoUseCase: UpsertTodoUseCase
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var fakeTodo: Todo
    private lateinit var fakeTitle: String

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        upsertTodoUseCase = UpsertTodoUseCase(fakeTodoRepository)

        fakeTodo = fakeTodoRepository.generateRandomTodo()
        fakeTitle = Faker.aString()
    }

    @Test
    fun `When insert new todo should be inserted`() = runBlocking {
        val result = upsertTodoUseCase(fakeTodo)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    Assert.assertEquals(1, fakeTodoRepository.fakeTodos.size)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `When update todo should not insert new todo rather update the old one`() = runBlocking {
        val updatedTodo = fakeTodo.copy(title = fakeTitle)
        val result = upsertTodoUseCase(updatedTodo)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    Assert.assertEquals(1, fakeTodoRepository.fakeTodos.size)
                    Assert.assertEquals(fakeTitle, fakeTodoRepository.fakeTodos[0].title)
                }
                else -> {}
            }
        }
    }
}