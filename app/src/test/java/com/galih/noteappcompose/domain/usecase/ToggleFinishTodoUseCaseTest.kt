package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.data.repoimpl.FakeTodoRepository
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ToggleFinishTodoUseCaseTest {
    private lateinit var toggleFinishTodoUseCase: ToggleFinishTodoUseCase
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var fakeTodo: Todo

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        toggleFinishTodoUseCase = ToggleFinishTodoUseCase(fakeTodoRepository)

        runBlocking {
            fakeTodo = fakeTodoRepository.generateRandomTodo()
            fakeTodoRepository.upsertTodo(fakeTodo)
        }
    }

    @Test
    fun `When toggle one time should change todo finished property`() = runBlocking {
        val result = toggleFinishTodoUseCase(fakeTodo)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    val todoInRepository = fakeTodoRepository.fakeTodos.find { it.id == fakeTodo.id }
                    Assert.assertEquals(!fakeTodo.isFinished, todoInRepository?.isFinished)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `When toggle two times should NOT change todo finished property`() = runBlocking {
        val result = toggleFinishTodoUseCase(fakeTodo)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    val todoInRepository = fakeTodoRepository.fakeTodos.find { it.id == fakeTodo.id }
                    val result2 = toggleFinishTodoUseCase(todoInRepository!!)

                    result2.collect { response2 ->
                        when (response2) {
                            is Resource.Success -> {
                                val todoInRepository2 = fakeTodoRepository.fakeTodos.find { it.id == fakeTodo.id }
                                Assert.assertEquals(fakeTodo.isFinished, todoInRepository2?.isFinished)
                            }
                            else -> {}
                        }
                    }
                }
                else -> {}
            }
        }
    }
}