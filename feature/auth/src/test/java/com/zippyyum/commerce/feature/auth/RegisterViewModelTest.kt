package com.zippyyum.commerce.feature.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.RegisterUserUseCase
import com.zippyyum.commerce.domain.auth.RegisteredUser
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

    private class FakeAuthRepository : AuthRepository {
        var email: String? = null

        override suspend fun register(email: String, password: String): AppResult<RegisteredUser> {
            this.email = email
            return AppResult.Success(RegisteredUser("user-1", email))
        }
    }
}
