package com.zippyyum.commerce.feature.auth

import com.google.common.truth.Truth.assertThat
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.GetActiveSessionUseCase
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
class SplashViewModelTest {
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
    fun `init emits authenticated navigation when active session exists`() = runTest(dispatcher) {
        val viewModel = SplashViewModel(
            GetActiveSessionUseCase(
                FakeAuthRepository(
                    activeSession = UserSession(
                        userId = "user-1",
                        email = "user@example.com",
                        accessToken = "jwt-token",
                        tokenType = "Bearer",
                        expiresAt = Instant.parse("2030-01-01T00:00:00Z"),
                    ),
                ),
            ),
        )
        val effect = async { viewModel.effects.first() }

        dispatcher.scheduler.advanceUntilIdle()

        assertThat(effect.await()).isEqualTo(SplashUiEffect.NavigateToAuthenticatedArea)
    }

    @Test
    fun `init emits sign in navigation when no session exists`() = runTest(dispatcher) {
        val viewModel = SplashViewModel(GetActiveSessionUseCase(FakeAuthRepository()))
        val effect = async { viewModel.effects.first() }

        dispatcher.scheduler.advanceUntilIdle()

        assertThat(effect.await()).isEqualTo(SplashUiEffect.NavigateToSignIn)
    }

    private class FakeAuthRepository(
        private val activeSession: UserSession? = null,
    ) : AuthRepository {
        override suspend fun register(email: String, password: String): AppResult<RegisteredUser> {
            error("Not used in this test")
        }

        override suspend fun login(email: String, password: String): AppResult<UserSession> {
            error("Not used in this test")
        }

        override suspend fun getActiveSession(): UserSession? = activeSession

        override suspend fun clearSession() = Unit
    }
}
