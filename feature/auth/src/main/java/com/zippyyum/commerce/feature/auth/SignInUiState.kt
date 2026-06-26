package com.zippyyum.commerce.feature.auth

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val message: String? = null,
    val isSubmitting: Boolean = false,
) {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting
}
