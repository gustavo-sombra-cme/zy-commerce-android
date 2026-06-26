package com.zippyyum.commerce.domain.auth

import java.time.Instant

data class UserSession(
    val userId: String,
    val email: String,
    val accessToken: String,
    val tokenType: String,
    val expiresAt: Instant,
)
