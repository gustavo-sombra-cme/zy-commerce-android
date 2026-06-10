package com.zycommerce.android.features.catalog.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequest(
    @SerialName("sku") val sku: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null
)
