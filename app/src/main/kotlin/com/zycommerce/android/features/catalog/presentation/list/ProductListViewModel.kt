package com.zycommerce.android.features.catalog.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zycommerce.android.features.catalog.domain.usecase.CreateProductUseCase
import com.zycommerce.android.features.catalog.domain.usecase.DeleteProductUseCase
import com.zycommerce.android.features.catalog.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val createProduct: CreateProductUseCase,
    private val deleteProduct: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProductListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentSearch: String = ""

    init {
        loadProducts()
    }

    fun onSearchQueryChange(query: String) {
        currentSearch = query
        loadProducts(query)
    }

    fun onCreateProduct(sku: String, name: String, description: String?) {
        viewModelScope.launch {
            createProduct(sku = sku.trim(), name = name.trim(), description = description?.trim())
                .onSuccess {
                    _events.send(ProductListEvent.ShowSnackbar("Product \"${it.name}\" created."))
                    loadProducts(currentSearch)
                }
                .onFailure { err ->
                    _events.send(ProductListEvent.ShowSnackbar("Error: ${err.message}"))
                }
        }
    }

    fun onDeleteProduct(productId: String, productName: String) {
        viewModelScope.launch {
            deleteProduct(productId)
                .onSuccess {
                    _events.send(ProductListEvent.ShowSnackbar("\"$productName\" deactivated."))
                    loadProducts(currentSearch)
                }
                .onFailure { err ->
                    _events.send(ProductListEvent.ShowSnackbar("Error: ${err.message}"))
                }
        }
    }

    fun retry() = loadProducts(currentSearch)

    private fun loadProducts(searchTerm: String? = null) {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            getProducts(searchTerm = searchTerm?.ifBlank { null })
                .onSuccess { products ->
                    _uiState.value = ProductListUiState.Success(
                        products = products,
                        searchQuery = searchTerm ?: ""
                    )
                }
                .onFailure { err ->
                    _uiState.value = ProductListUiState.Error(
                        message = err.message ?: "Unknown error"
                    )
                }
        }
    }
}
