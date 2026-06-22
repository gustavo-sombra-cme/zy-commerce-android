package com.zippyyum.commerce.domain.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RegisterUserUseCaseTest {
    @Test
    fun `invoke trims email before registering`() = runTest {
        val repository = RecordingAuthRepository()
        val useCase = RegisterUserUseCase(repository)

        val result = useCase("  USER@example.COM  ", "Password123")

        assertThat(result).isEqualTo(AppResult.Success(RegisteredUser("user-1", "USER@example.COM")))
        assertThat(repository.email).isEqualTo("USER@example.COM")
        assertThat(repository.password).isEqualTo("Password123")
    }

    @Test
    fun `invoke rejects blank fields before repository call`() = runTest {
        val repository = RecordingAuthRepository()
        val useCase = RegisterUserUseCase(repository)

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
            wasCalled = true
            this.email = email
            this.password = password
            return AppResult.Success(RegisteredUser("user-1", email))
        }
    }
}
