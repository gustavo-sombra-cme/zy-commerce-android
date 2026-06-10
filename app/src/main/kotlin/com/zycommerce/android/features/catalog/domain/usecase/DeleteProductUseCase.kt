package com.zycommerce.android.features.catalog.domain.usecase

import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(productId: String): Result<Unit> =
        repository.deleteProduct(productId)
}
