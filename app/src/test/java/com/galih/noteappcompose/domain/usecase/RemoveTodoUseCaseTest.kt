package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.data.repoimpl.FakeTodoRepository
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RemoveTodoUseCaseTest {
    private lateinit var removeTodoUseCase: RemoveTodoUseCase
    private lateinit var fakeTodoRepository: FakeTodoRepository
    private lateinit var fakeTodo: Todo

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        removeTodoUseCase = RemoveTodoUseCase(fakeTodoRepository)

        fakeTodo = fakeTodoRepository.generateRandomTodo()
        fakeTodoRepository.fakeTodos.add(fakeTodo)
    }

    @Test
    fun `When remove todo should be removed`() = runBlocking {
        val result = removeTodoUseCase(fakeTodo)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    Assert.assertEquals(0, fakeTodoRepository.fakeTodos.size)
                }
                else -> {}
            }
        }
    }
}