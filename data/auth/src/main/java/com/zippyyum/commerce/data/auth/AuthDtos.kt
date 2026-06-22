package com.zippyyum.commerce.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequestDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class RegisterUserResponseDto(
    @SerialName("userId") val userId: String,
    @SerialName("email") val email: String,
)
