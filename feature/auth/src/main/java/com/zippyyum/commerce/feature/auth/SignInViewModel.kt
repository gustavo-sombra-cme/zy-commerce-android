package com.zippyyum.commerce.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUser: LoginUserUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SignInUiEffect>()
    val effects: SharedFlow<SignInUiEffect> = _effects.asSharedFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, message = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, message = null) }
    }

    fun submit() {
        val current = _uiState.value
        if (current.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, emailError = null, passwordError = null, message = null) }
            when (val result = loginUser(current.email, current.password)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            message = "Welcome back, ${result.value.email}.",
                        )
                    }
                    _effects.emit(SignInUiEffect.NavigateToAuthenticatedArea)
                }
                is AppResult.Failure -> handleFailure(result.error)
            }
        }
    }

    private fun handleFailure(error: AppError) {
        _uiState.update { state ->
            when (error) {
                is AppError.Validation -> state.copy(
                    isSubmitting = false,
                    emailError = error.fields["email"]?.firstOrNull(),
                    passwordError = error.fields["password"]?.firstOrNull(),
                    message = if (error.fields.isEmpty()) "Check your sign-in details and try again." else null,
                )
                AppError.NetworkUnavailable -> state.copy(
                    isSubmitting = false,
                    message = "Cannot reach ZY-Commerce right now.",
                )
                AppError.Unauthorized -> state.copy(
                    isSubmitting = false,
                    message = "The email or password is incorrect.",
                )
                AppError.Forbidden -> state.copy(
                    isSubmitting = false,
                    message = "This account is inactive. Contact support or try another account.",
                )
                AppError.NotFound,
                is AppError.Conflict,
                is AppError.Unknown -> state.copy(
                    isSubmitting = false,
                    message = "Sign in failed. Try again in a moment.",
                )
            }
        }
    }
}
