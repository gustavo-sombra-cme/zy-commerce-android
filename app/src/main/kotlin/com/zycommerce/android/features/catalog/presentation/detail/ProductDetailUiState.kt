package com.zycommerce.android.features.catalog.presentation.detail

import com.zycommerce.android.features.catalog.domain.model.Product

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class Success(val product: Product) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
}

sealed interface ProductDetailEvent {
    data object NavigateBack : ProductDetailEvent
    data class ShowSnackbar(val message: String) : ProductDetailEvent
}
