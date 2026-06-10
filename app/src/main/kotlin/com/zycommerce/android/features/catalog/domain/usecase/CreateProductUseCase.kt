package com.zycommerce.android.features.catalog.domain.usecase

import com.zycommerce.android.features.catalog.domain.model.Product
import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        sku: String,
        name: String,
        description: String?
    ): Result<Product> = repository.createProduct(sku = sku, name = name, description = description)
}
