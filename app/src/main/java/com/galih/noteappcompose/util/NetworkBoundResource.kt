package com.galih.noteappcompose.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            val response = fetch()
            saveFetchResult(response)
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable.localizedMessage ?: "unknown error", it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}

inline fun <ResultType> fetch(
    crossinline fetchFn: suspend () -> ResultType
): Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading<ResultType>())
    try {
        val response = fetchFn()
        emit(Resource.Success(response))
    } catch (throwable: Throwable) {
        emit(Resource.Error<ResultType>(throwable.localizedMessage ?: "unknown error"))
    }
}