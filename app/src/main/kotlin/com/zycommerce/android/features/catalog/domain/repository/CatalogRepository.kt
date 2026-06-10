package com.zycommerce.android.features.catalog.domain.repository

import com.zycommerce.android.features.catalog.domain.model.Product
import com.zycommerce.android.features.catalog.domain.model.ProductListItem

interface CatalogRepository {
    suspend fun searchProducts(
        searchTerm: String? = null,
        isActive: Boolean? = null,
        pageNumber: Int = 1,
        pageSize: Int = 20
    ): Result<List<ProductListItem>>

    suspend fun getProductById(productId: String): Result<Product>

    suspend fun createProduct(
        sku: String,
        name: String,
        description: String?
    ): Result<Product>

    suspend fun deleteProduct(productId: String): Result<Unit>
}
