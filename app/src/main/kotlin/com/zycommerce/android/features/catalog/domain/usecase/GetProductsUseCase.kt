package com.zycommerce.android.features.catalog.domain.usecase

import com.zycommerce.android.features.catalog.domain.model.ProductListItem
import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(
        searchTerm: String? = null,
        isActive: Boolean? = null
    ): Result<List<ProductListItem>> =
        repository.searchProducts(searchTerm = searchTerm, isActive = isActive)
}
