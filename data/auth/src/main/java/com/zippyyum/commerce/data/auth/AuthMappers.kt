package com.zippyyum.commerce.data.auth

import com.zippyyum.commerce.core.storage.StoredSession
import com.zippyyum.commerce.domain.auth.RegisteredUser
import com.zippyyum.commerce.domain.auth.UserSession
import java.time.Instant

fun RegisterUserResponseDto.toDomain(): RegisteredUser = RegisteredUser(
    userId = userId,
    email = email,
)

fun LoginUserResponseDto.toDomain(): UserSession = UserSession(
    userId = userId,
    email = email,
    accessToken = accessToken,
    tokenType = tokenType,
    expiresAt = Instant.parse(expiresAt),
)

fun UserSession.toStoredSession(): StoredSession = StoredSession(
    userId = userId,
    email = email,
    accessToken = accessToken,
    tokenType = tokenType,
    expiresAt = expiresAt.toString(),
)

fun StoredSession.toDomain(): UserSession = UserSession(
    userId = userId,
    email = email,
    accessToken = accessToken,
    tokenType = tokenType,
    expiresAt = Instant.parse(expiresAt),
)
