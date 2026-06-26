package com.zippyyum.commerce.feature.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.RegisterUserUseCase
import com.zippyyum.commerce.domain.auth.RegisteredUser
import com.zippyyum.commerce.domain.auth.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
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
    fun `submit registers account and shows confirmation`() = runTest(dispatcher) {
        val repository = FakeAuthRepository()
        val viewModel = RegisterViewModel(RegisterUserUseCase(repository))

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("Password123")
        viewModel.submit()
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(repository.email).isEqualTo("user@example.com")
        assertThat(viewModel.uiState.value.message).isEqualTo("Account created for user@example.com.")
        assertThat(viewModel.uiState.value.isSubmitting).isFalse()
    }

    @Test
    fun `submit shows duplicate email feedback and preserves form values`() = runTest(dispatcher) {
        val repository = FakeAuthRepository(
            result = AppResult.Failure(AppError.Conflict("That email is already registered.")),
        )
        val viewModel = RegisterViewModel(RegisterUserUseCase(repository))

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("Password123")
        viewModel.submit()
        dispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertThat(email).isEqualTo("user@example.com")
            assertThat(password).isEqualTo("Password123")
            assertThat(emailError).isEqualTo("That email is already registered.")
            assertThat(message).isEqualTo("Sign in or use a different email address.")
            assertThat(isSubmitting).isFalse()
        }
    }

    @Test
    fun `submit shows backend validation errors under matching fields`() = runTest(dispatcher) {
        val repository = FakeAuthRepository(
            result = AppResult.Failure(
                AppError.Validation(
                    mapOf(
                        "email" to listOf("Email is invalid."),
                        "password" to listOf(
                            "The length of 'Password' must be at least 8 characters. You entered 6 characters.",
                        ),
                    ),
                ),
            ),
        )
        val viewModel = RegisterViewModel(RegisterUserUseCase(repository))

        viewModel.onEmailChanged("bad-email")
        viewModel.onPasswordChanged("secret")
        viewModel.submit()
        dispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertThat(email).isEqualTo("bad-email")
            assertThat(password).isEqualTo("secret")
            assertThat(emailError).isEqualTo("Email is invalid.")
            assertThat(passwordError).isEqualTo(
                "The length of 'Password' must be at least 8 characters. You entered 6 characters.",
            )
            assertThat(message).isNull()
            assertThat(isSubmitting).isFalse()
        }
    }

    private class FakeAuthRepository(
        private val result: AppResult<RegisteredUser> = AppResult.Success(
            RegisteredUser("user-1", "user@example.com"),
        ),
    ) : AuthRepository {
        var email: String? = null

        override suspend fun register(email: String, password: String): AppResult<RegisteredUser> {
            this.email = email
            return when (result) {
                is AppResult.Success -> AppResult.Success(RegisteredUser(result.value.userId, email))
                is AppResult.Failure -> result
            }
        }

        override suspend fun login(email: String, password: String): AppResult<UserSession> {
            error("Not used in this test")
        }

        override suspend fun getActiveSession(): UserSession? = null

        override suspend fun clearSession() = Unit
    }
}
