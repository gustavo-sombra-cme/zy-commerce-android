package com.zycommerce.android.features.catalog.domain.usecase

import com.zycommerce.android.features.catalog.domain.model.Product
import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(productId: String): Result<Product> =
        repository.getProductById(productId)
}
