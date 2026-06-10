package com.zycommerce.android.features.catalog.data.remote

import com.zycommerce.android.features.catalog.data.remote.dto.CreateProductRequest
import com.zycommerce.android.features.catalog.data.remote.dto.CreateProductResponseDto
import com.zycommerce.android.features.catalog.data.remote.dto.PagedResultDto
import com.zycommerce.android.features.catalog.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {

    @GET("api/catalog/products")
    suspend fun searchProducts(
        @Query("searchTerm") searchTerm: String? = null,
        @Query("isActive") isActive: Boolean? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): PagedResultDto<ProductDto>

    @GET("api/catalog/products/{productId}")
    suspend fun getProductById(
        @Path("productId") productId: String
    ): ProductDto

    @POST("api/catalog/products")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): CreateProductResponseDto

    @DELETE("api/catalog/products/{productId}")
    suspend fun deleteProduct(
        @Path("productId") productId: String
    ): Response<Unit>
}
