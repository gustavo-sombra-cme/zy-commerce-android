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

@Serializable
data class LoginUserRequestDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class LoginUserResponseDto(
    @SerialName("userId") val userId: String,
    @SerialName("email") val email: String,
    @SerialName("accessToken") val accessToken: String,
    @SerialName("tokenType") val tokenType: String,
    @SerialName("expiresAt") val expiresAt: String,
)

@Serializable
data class ValidationErrorResponseDto(
    @SerialName("errors") val errors: Map<String, List<String>> = emptyMap(),
)
