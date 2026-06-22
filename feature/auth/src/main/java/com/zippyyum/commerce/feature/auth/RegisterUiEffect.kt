package com.zippyyum.commerce.feature.auth

sealed interface RegisterUiEffect {
    data object NavigateToSignIn : RegisterUiEffect
}
