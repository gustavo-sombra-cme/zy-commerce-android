package com.zippyyum.commerce.feature.auth

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val message: String? = null,
) {
    val canSubmit: Boolean = !isSubmitting && email.isNotBlank() && password.isNotBlank()
}
