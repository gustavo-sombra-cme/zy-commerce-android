package com.zippyyum.commerce.data.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.core.storage.SessionStorage
import com.zippyyum.commerce.core.storage.StoredSession
import com.zippyyum.commerce.domain.auth.UserSession
import java.time.Instant
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
                            "{\"message\":\"Duplicate email\"}".toResponseBody("application/json".toMediaType()),
                        ),
                    )
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    error("Not used in this test")
                }
            },
            json = testJson,
            sessionStorage = FakeSessionStorage(),
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

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    error("Not used in this test")
                }
            },
            json = testJson,
            sessionStorage = FakeSessionStorage(),
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

    @Test
    fun `login stores returned session`() {
        val sessionStorage = FakeSessionStorage()
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto = LoginUserResponseDto(
                    userId = "user-1",
                    email = request.email,
                    accessToken = "jwt-token",
                    tokenType = "Bearer",
                    expiresAt = "2030-01-01T00:00:00Z",
                )
            },
            json = testJson,
            sessionStorage = sessionStorage,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.login("user@example.com", "Password123")
            }
        }.getOrThrow()

        assertThat(result).isEqualTo(
            AppResult.Success(
                UserSession(
                    userId = "user-1",
                    email = "user@example.com",
                    accessToken = "jwt-token",
                    tokenType = "Bearer",
                    expiresAt = Instant.parse("2030-01-01T00:00:00Z"),
                ),
            ),
        )
        assertThat(sessionStorage.storedSession).isEqualTo(
            StoredSession(
                userId = "user-1",
                email = "user@example.com",
                accessToken = "jwt-token",
                tokenType = "Bearer",
                expiresAt = "2030-01-01T00:00:00Z",
            ),
        )
    }

    @Test
    fun `login maps unauthorized response to unauthorized app error`() {
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    throw HttpException(
                        Response.error<LoginUserResponseDto>(
                            401,
                            "{\"message\":\"Invalid credentials\"}".toResponseBody("application/json".toMediaType()),
                        ),
                    )
                }
            },
            json = testJson,
            sessionStorage = FakeSessionStorage(),
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.login("user@example.com", "WrongPassword123")
            }
        }.getOrThrow()

        assertThat(result).isEqualTo(AppResult.Failure(AppError.Unauthorized))
    }

    @Test
    fun `login maps forbidden response to forbidden app error`() {
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    throw HttpException(
                        Response.error<LoginUserResponseDto>(
                            403,
                            "{\"message\":\"Inactive user\"}".toResponseBody("application/json".toMediaType()),
                        ),
                    )
                }
            },
            json = testJson,
            sessionStorage = FakeSessionStorage(),
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.login("inactive@example.com", "Password123")
            }
        }.getOrThrow()

        assertThat(result).isEqualTo(AppResult.Failure(AppError.Forbidden))
    }

    @Test
    fun `get active session clears expired session`() {
        val sessionStorage = FakeSessionStorage(
            storedSession = StoredSession(
                userId = "user-1",
                email = "user@example.com",
                accessToken = "jwt-token",
                tokenType = "Bearer",
                expiresAt = "2000-01-01T00:00:00Z",
            ),
        )
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    error("Not used in this test")
                }
            },
            json = testJson,
            sessionStorage = sessionStorage,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.getActiveSession()
            }
        }.getOrThrow()

        assertThat(result).isNull()
        assertThat(sessionStorage.storedSession).isNull()
        assertThat(sessionStorage.clearCount).isEqualTo(1)
    }

    @Test
    fun `get active session clears unreadable stored session`() {
        val sessionStorage = FakeSessionStorage(
            getSessionFailure = IllegalStateException("Keystore entry invalidated."),
        )
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    error("Not used in this test")
                }
            },
            json = testJson,
            sessionStorage = sessionStorage,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.getActiveSession()
            }
        }.getOrThrow()

        assertThat(result).isNull()
        assertThat(sessionStorage.clearCount).isEqualTo(1)
    }

    @Test
    fun `get active session clears malformed expiration session`() {
        val sessionStorage = FakeSessionStorage(
            storedSession = StoredSession(
                userId = "user-1",
                email = "user@example.com",
                accessToken = "jwt-token",
                tokenType = "Bearer",
                expiresAt = "not-a-date",
            ),
        )
        val repository = AuthRepositoryImpl(
            authApi = object : AuthApi {
                override suspend fun register(request: RegisterUserRequestDto): RegisterUserResponseDto {
                    error("Not used in this test")
                }

                override suspend fun login(request: LoginUserRequestDto): LoginUserResponseDto {
                    error("Not used in this test")
                }
            },
            json = testJson,
            sessionStorage = sessionStorage,
        )

        val result = runCatching {
            kotlinx.coroutines.runBlocking {
                repository.getActiveSession()
            }
        }.getOrThrow()

        assertThat(result).isNull()
        assertThat(sessionStorage.storedSession).isNull()
        assertThat(sessionStorage.clearCount).isEqualTo(1)
    }

    private class FakeSessionStorage(
        var storedSession: StoredSession? = null,
        private val getSessionFailure: Throwable? = null,
    ) : SessionStorage {
        var clearCount: Int = 0

        override fun saveSession(session: StoredSession) {
            storedSession = session
        }

        override fun getSession(): StoredSession? {
            getSessionFailure?.let { throw it }
            return storedSession
        }

        override fun clearSession() {
            clearCount += 1
            storedSession = null
        }
    }

    private companion object {
        val testJson = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
}
