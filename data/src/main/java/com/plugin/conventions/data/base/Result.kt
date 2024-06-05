package com.plugin.conventions.data.base

sealed class OperationResult<out T> {
    data class Success<T>(val result: T) : OperationResult<T>()
    data class Error(val exception: Throwable) : OperationResult<Nothing>()
}