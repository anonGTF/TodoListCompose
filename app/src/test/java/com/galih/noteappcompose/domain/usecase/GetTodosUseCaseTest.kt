package com.galih.noteappcompose.domain.usecase

import com.galih.noteappcompose.data.repoimpl.FakeTodoRepository
import com.galih.noteappcompose.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetTodosUseCaseTest {
    private lateinit var getTodosUseCase: GetTodosUseCase
    private lateinit var fakeTodoRepository: FakeTodoRepository

    @Before
    fun setUp() {
        fakeTodoRepository = FakeTodoRepository()
        getTodosUseCase = GetTodosUseCase(fakeTodoRepository)

        fakeTodoRepository.generateRandomTodos()
    }

    @Test
    fun `When get todos should not return null`() = runBlocking {
        val result = getTodosUseCase(Unit)
        Assert.assertNotNull(result)
        result.collect { response ->
            when (response) {
                is Resource.Success -> {
                    Assert.assertEquals(20, response.data?.size)
                }
                else -> {}
            }
        }
    }
}