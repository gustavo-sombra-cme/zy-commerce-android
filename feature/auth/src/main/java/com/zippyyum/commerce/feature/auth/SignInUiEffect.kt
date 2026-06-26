package com.zippyyum.commerce.feature.auth

sealed interface SignInUiEffect {
    data object NavigateToAuthenticatedArea : SignInUiEffect
}
