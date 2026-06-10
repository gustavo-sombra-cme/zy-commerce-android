package com.zycommerce.android.features.catalog.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zycommerce.android.features.catalog.domain.usecase.DeleteProductUseCase
import com.zycommerce.android.features.catalog.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductById: GetProductByIdUseCase,
    private val deleteProduct: DeleteProductUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProductDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadProduct()
    }

    fun retry() = loadProduct()

    fun onDelete() {
        viewModelScope.launch {
            deleteProduct(productId)
                .onSuccess {
                    _events.send(ProductDetailEvent.ShowSnackbar("Product deactivated."))
                    _events.send(ProductDetailEvent.NavigateBack)
                }
                .onFailure { err ->
                    _events.send(ProductDetailEvent.ShowSnackbar("Error: ${err.message}"))
                }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            getProductById(productId)
                .onSuccess { _uiState.value = ProductDetailUiState.Success(it) }
                .onFailure { _uiState.value = ProductDetailUiState.Error(it.message ?: "Unknown error") }
        }
    }
}
