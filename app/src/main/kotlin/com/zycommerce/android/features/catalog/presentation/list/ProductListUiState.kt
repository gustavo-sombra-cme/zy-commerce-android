package com.zycommerce.android.features.catalog.presentation.list

import com.zycommerce.android.features.catalog.domain.model.ProductListItem

sealed interface ProductListUiState {
    data object Loading : ProductListUiState
    data class Success(
        val products: List<ProductListItem>,
        val searchQuery: String = ""
    ) : ProductListUiState
    data class Error(val message: String) : ProductListUiState
}

sealed interface ProductListEvent {
    data class ShowSnackbar(val message: String) : ProductListEvent
    data class NavigateToDetail(val productId: String) : ProductListEvent
}
