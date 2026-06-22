package com.zippyyum.commerce.core.common

sealed interface AppError {
    data class Validation(val fields: Map<String, List<String>>) : AppError
    data class Conflict(val message: String?) : AppError
    data object Unauthorized : AppError
    data object Forbidden : AppError
    data object NotFound : AppError
    data object NetworkUnavailable : AppError
    data class Unknown(val message: String?) : AppError
}
