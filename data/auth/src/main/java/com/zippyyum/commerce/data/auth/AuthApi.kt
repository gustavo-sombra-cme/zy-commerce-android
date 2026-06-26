package com.zippyyum.commerce.data.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/users/register")
    suspend fun register(@Body request: RegisterUserRequestDto): RegisterUserResponseDto

    @POST("api/auth/users/login")
    suspend fun login(@Body request: LoginUserRequestDto): LoginUserResponseDto
}
