package com.plugin.conventions.data.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SafeRequest {
    suspend fun <T> request(
        dispatcher: CoroutineDispatcher,
        apiRequest: suspend () -> T,
    ): OperationResult<T> {
        return withContext(dispatcher) {
            try {
                OperationResult.Success(apiRequest.invoke())
            } catch (throwable: Throwable) {
                OperationResult.Error(throwable)
            }
        }
    }
}