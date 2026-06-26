package com.zippyyum.commerce.domain.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import java.time.Instant
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginUserUseCaseTest {
    @Test
    fun `invoke trims email before signing in`() = runTest {
        val repository = RecordingAuthRepository()
        val useCase = LoginUserUseCase(repository)

        val result = useCase("  USER@example.COM  ", "Password123")

        assertThat(result).isEqualTo(
            AppResult.Success(
                UserSession(
                    userId = "user-1",
                    email = "USER@example.COM",
                    accessToken = "token-1",
                    tokenType = "Bearer",
                    expiresAt = Instant.parse("2030-01-01T00:00:00Z"),
                ),
            ),
        )
        assertThat(repository.email).isEqualTo("USER@example.COM")
        assertThat(repository.password).isEqualTo("Password123")
    }

    @Test
    fun `invoke rejects blank fields before repository call`() = runTest {
        val repository = RecordingAuthRepository()
        val useCase = LoginUserUseCase(repository)

        val result = useCase(" ", "")

        assertThat(repository.wasCalled).isFalse()
        val error = (result as AppResult.Failure).error as AppError.Validation
        assertThat(error.fields.keys).containsExactly("email", "password")
    }

    private class RecordingAuthRepository : AuthRepository {
        var wasCalled = false
        var email: String? = null
        var password: String? = null

        override suspend fun register(email: String, password: String): AppResult<RegisteredUser> {
            error("Not used in this test")
        }

        override suspend fun login(email: String, password: String): AppResult<UserSession> {
            wasCalled = true
            this.email = email
            this.password = password
            return AppResult.Success(
                UserSession(
                    userId = "user-1",
                    email = email,
                    accessToken = "token-1",
                    tokenType = "Bearer",
                    expiresAt = Instant.parse("2030-01-01T00:00:00Z"),
                ),
            )
        }

        override suspend fun getActiveSession(): UserSession? = null

        override suspend fun clearSession() = Unit
    }
}
