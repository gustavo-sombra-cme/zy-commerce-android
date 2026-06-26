package com.zippyyum.commerce.domain.auth

import com.zippyyum.commerce.core.common.AppResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AppResult<RegisteredUser>

    suspend fun login(email: String, password: String): AppResult<UserSession>

    suspend fun getActiveSession(): UserSession?

    suspend fun clearSession()
}
