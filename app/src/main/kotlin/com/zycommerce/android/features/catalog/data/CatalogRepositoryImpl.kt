package com.zycommerce.android.features.catalog.data

import com.zycommerce.android.features.catalog.data.remote.CatalogApiService
import com.zycommerce.android.features.catalog.data.remote.dto.CreateProductRequest
import com.zycommerce.android.features.catalog.domain.model.Product
import com.zycommerce.android.features.catalog.domain.model.ProductListItem
import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val api: CatalogApiService
) : CatalogRepository {

    override suspend fun searchProducts(
        searchTerm: String?,
        isActive: Boolean?,
        pageNumber: Int,
        pageSize: Int
    ): Result<List<ProductListItem>> = runCatching {
        val response = api.searchProducts(
            searchTerm = searchTerm?.ifBlank { null },
            isActive = isActive,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        response.items.map { dto ->
            ProductListItem(
                id = dto.productId,
                sku = dto.sku,
                name = dto.name,
                description = dto.description,
                isActive = dto.isActive,
                createdAt = dto.createdAt
            )
        }
    }

    override suspend fun getProductById(productId: String): Result<Product> = runCatching {
        val dto = api.getProductById(productId)
        Product(
            id = dto.productId,
            sku = dto.sku,
            name = dto.name,
            description = dto.description,
            isActive = dto.isActive,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    override suspend fun createProduct(
        sku: String,
        name: String,
        description: String?
    ): Result<Product> = runCatching {
        val response = api.createProduct(
            CreateProductRequest(sku = sku, name = name, description = description?.ifBlank { null })
        )
        // Fetch full details after creation
        api.getProductById(response.productId).let { dto ->
            Product(
                id = dto.productId,
                sku = dto.sku,
                name = dto.name,
                description = dto.description,
                isActive = dto.isActive,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt
            )
        }
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> = runCatching {
        val response = api.deleteProduct(productId)
        if (!response.isSuccessful) error("Delete failed: HTTP ${response.code()}")
    }
}
