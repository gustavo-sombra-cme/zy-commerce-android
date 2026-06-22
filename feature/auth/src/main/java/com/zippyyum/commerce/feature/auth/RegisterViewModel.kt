package com.zippyyum.commerce.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.RegisterUserUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUser: RegisterUserUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<RegisterUiEffect>()
    val effects: SharedFlow<RegisterUiEffect> = _effects.asSharedFlow()

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
            when (val result = registerUser(current.email, current.password)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            message = "Account created for ${result.value.email}.",
                        )
                    }
                    _effects.emit(RegisterUiEffect.NavigateToSignIn)
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
                    message = if (error.fields.isEmpty()) "Check the registration details and try again." else null,
                )
                is AppError.Conflict -> state.copy(
                    isSubmitting = false,
                    message = error.message ?: "That account could not be created.",
                )
                AppError.NetworkUnavailable -> state.copy(
                    isSubmitting = false,
                    message = "Cannot reach ZY-Commerce right now.",
                )
                AppError.Unauthorized,
                AppError.Forbidden,
                AppError.NotFound,
                is AppError.Unknown -> state.copy(
                    isSubmitting = false,
                    message = "Registration failed. Try again in a moment.",
                )
            }
        }
    }
}
