package com.zippyyum.commerce.domain.auth

import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult

class RegisterUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AppResult<RegisteredUser> {
        val normalizedEmail = email.trim()
        val validationErrors = buildMap {
            if (normalizedEmail.isBlank()) put("email", listOf("Email is required."))
            if (password.isBlank()) put("password", listOf("Password is required."))
        }

        if (validationErrors.isNotEmpty()) {
            return AppResult.Failure(AppError.Validation(validationErrors))
        }

        return authRepository.register(normalizedEmail, password)
    }
}
