package com.zycommerce.android.features.catalog.domain.model

data class Product(
    val id: String,
    val sku: String,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)

data class ProductListItem(
    val id: String,
    val sku: String,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdAt: String
)
