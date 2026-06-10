package com.zycommerce.android.features.catalog.presentation.list

import com.zycommerce.android.features.catalog.domain.model.ProductListItem
import com.zycommerce.android.features.catalog.domain.usecase.CreateProductUseCase
import com.zycommerce.android.features.catalog.domain.usecase.DeleteProductUseCase
import com.zycommerce.android.features.catalog.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProducts: GetProductsUseCase
    private lateinit var createProduct: CreateProductUseCase
    private lateinit var deleteProduct: DeleteProductUseCase

    private val fakeProducts = listOf(
        ProductListItem("1", "SKU-001", "Product One", null, true, "2026-01-01T00:00:00Z"),
        ProductListItem("2", "SKU-002", "Product Two", "A desc", true, "2026-01-02T00:00:00Z")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProducts = mockk()
        createProduct = mockk()
        deleteProduct = mockk()
        coEvery { getProducts(any(), any()) } returns Result.success(fakeProducts)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Success state after loading products`() = runTest(testDispatcher) {
        val viewModel = ProductListViewModel(getProducts, createProduct, deleteProduct)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue("Expected Success, got $state", state is ProductListUiState.Success)
        assertEquals(2, (state as ProductListUiState.Success).products.size)
    }

    @Test
    fun `emits Error state when getProducts fails`() = runTest(testDispatcher) {
        coEvery { getProducts(any(), any()) } returns Result.failure(RuntimeException("Network error"))
        val viewModel = ProductListViewModel(getProducts, createProduct, deleteProduct)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertTrue("Expected Error, got $state", state is ProductListUiState.Error)
    }

    @Test
    fun `onSearchQueryChange triggers reload with search term`() = runTest(testDispatcher) {
        coEvery { getProducts(searchTerm = "foo", any()) } returns Result.success(emptyList())
        val viewModel = ProductListViewModel(getProducts, createProduct, deleteProduct)
        advanceUntilIdle()
        viewModel.onSearchQueryChange("foo")
        advanceUntilIdle()
        val state = viewModel.uiState.value as ProductListUiState.Success
        assertEquals(0, state.products.size)
        assertEquals("foo", state.searchQuery)
    }
}
