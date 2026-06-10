package com.zycommerce.android.features.catalog.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("productId") val productId: String,
    @SerialName("sku") val sku: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("isActive") val isActive: Boolean = true,
    @SerialName("createdAt") val createdAt: String = "",
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class CreateProductResponseDto(
    @SerialName("productId") val productId: String,
    @SerialName("sku") val sku: String,
    @SerialName("name") val name: String
)
