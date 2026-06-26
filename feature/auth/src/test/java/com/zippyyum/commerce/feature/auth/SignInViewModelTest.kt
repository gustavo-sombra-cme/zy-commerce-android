package com.zippyyum.commerce.feature.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.LoginUserUseCase
import com.zippyyum.commerce.domain.auth.RegisteredUser
import com.zippyyum.commerce.domain.auth.UserSession
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submit signs in and emits authenticated navigation`() = runTest(dispatcher) {
        val repository = FakeAuthRepository()
        val viewModel = SignInViewModel(LoginUserUseCase(repository))
        val effect = async { viewModel.effects.first() }

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("Password123")
        viewModel.submit()
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(repository.email).isEqualTo("user@example.com")
        assertThat(viewModel.uiState.value.message).isEqualTo("Welcome back, user@example.com.")
        assertThat(effect.await()).isEqualTo(SignInUiEffect.NavigateToAuthenticatedArea)
    }

    @Test
    fun `submit shows validation feedback from use case`() = runTest(dispatcher) {
        val repository = FakeAuthRepository(
            result = AppResult.Failure(
                AppError.Validation(
                    mapOf(
                        "email" to listOf("Email is required."),
                        "password" to listOf("Password is required."),
                    ),
                ),
            ),
        )
        val viewModel = SignInViewModel(LoginUserUseCase(repository))

        viewModel.submit()
        dispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertThat(emailError).isEqualTo("Email is required.")
            assertThat(passwordError).isEqualTo("Password is required.")
            assertThat(isSubmitting).isFalse()
        }
    }

    private class FakeAuthRepository(
        private val result: AppResult<UserSession> = AppResult.Success(
            UserSession(
                userId = "user-1",
                email = "user@example.com",
                accessToken = "jwt-token",
                tokenType = "Bearer",
                expiresAt = Instant.parse("2030-01-01T00:00:00Z"),
            ),
        ),
    ) : AuthRepository {
        var email: String? = null

        override suspend fun register(email: String, password: String): AppResult<RegisteredUser> {
            error("Not used in this test")
        }

        override suspend fun login(email: String, password: String): AppResult<UserSession> {
            this.email = email
            return result
        }

        override suspend fun getActiveSession(): UserSession? = null

        override suspend fun clearSession() = Unit
    }
}
