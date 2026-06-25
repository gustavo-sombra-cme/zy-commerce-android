package com.zippyyum.commerce.data.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryImplTest {
    @Test
    fun `register maps conflict response to duplicate email error`() {
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    throw HttpException(
                        Response.error<RegisterUserResponseDto>(
                            409,
                            """{"message":"Duplicate email"}"""
                                .toResponseBody("application/json".toMediaType()),
                        ),
                    )
                }
            },
            json = testJson,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.register("user@example.com", "Password123")
            }
        }.getOrThrow()

        assertThat(result).isInstanceOf(AppResult.Failure::class.java)
        val error = (result as AppResult.Failure).error
        assertThat(error).isEqualTo(AppError.Conflict("That email is already registered."))
    }

    @Test
    fun `register maps validation response fields to lowercase app validation keys`() {
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    throw HttpException(
                        Response.error<RegisterUserResponseDto>(
                            400,
                            """
                            {
                              "type": "https://httpstatuses.com/400",
                              "title": "Validation failed.",
                              "status": 400,
                              "errors": {
                                "Email": ["Email is invalid."],
                                "Password": ["The length of 'Password' must be at least 8 characters."]
                              }
                            }
                            """.trimIndent().toResponseBody("application/json".toMediaType()),
                        ),
                    )
                }
            },
            json = testJson,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.register("bad-email", "secret")
            }
        }.getOrThrow()

        assertThat(result).isInstanceOf(AppResult.Failure::class.java)
        val error = (result as AppResult.Failure).error
        assertThat(error).isEqualTo(
            AppError.Validation(
                mapOf(
                    "email" to listOf("Email is invalid."),
                    "password" to listOf("The length of 'Password' must be at least 8 characters."),
                ),
            ),
        )
    }

    private companion object {
        val testJson = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
}
