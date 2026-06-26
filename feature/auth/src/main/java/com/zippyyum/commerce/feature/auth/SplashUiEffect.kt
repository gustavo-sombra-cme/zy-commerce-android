package com.zippyyum.commerce.feature.auth

sealed interface SplashUiEffect {
    data object NavigateToAuthenticatedArea : SplashUiEffect

    data object NavigateToSignIn : SplashUiEffect
}
