package com.zippyyum.commerce.domain.auth

class GetActiveSessionUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): UserSession? = authRepository.getActiveSession()
}
