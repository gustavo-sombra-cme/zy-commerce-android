package com.zippyyum.commerce.data.auth

import com.zippyyum.commerce.domain.auth.RegisteredUser

fun RegisterUserResponseDto.toDomain(): RegisteredUser = RegisteredUser(
    userId = userId,
    email = email,
)
