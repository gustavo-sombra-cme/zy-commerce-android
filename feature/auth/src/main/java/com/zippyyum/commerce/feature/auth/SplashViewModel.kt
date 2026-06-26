package com.zippyyum.commerce.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zippyyum.commerce.domain.auth.GetActiveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getActiveSession: GetActiveSessionUseCase,
) : ViewModel() {
    private val _effects = MutableSharedFlow<SplashUiEffect>(replay = 1)
    val effects: SharedFlow<SplashUiEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            val activeSession = getActiveSession()
            _effects.emit(
                if (activeSession != null) {
                    SplashUiEffect.NavigateToAuthenticatedArea
                } else {
                    SplashUiEffect.NavigateToSignIn
                },
            )
        }
    }
}
