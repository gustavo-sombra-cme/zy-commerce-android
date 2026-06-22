package com.zippyyum.commerce.domain.auth

import com.zippyyum.commerce.core.common.AppResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AppResult<RegisteredUser>
}
