package com.zycommerce.android.features.catalog.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedResultDto<T>(
    @SerialName("items") val items: List<T>,
    @SerialName("pageNumber") val pageNumber: Int,
    @SerialName("pageSize") val pageSize: Int,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("hasPreviousPage") val hasPreviousPage: Boolean,
    @SerialName("hasNextPage") val hasNextPage: Boolean
)
