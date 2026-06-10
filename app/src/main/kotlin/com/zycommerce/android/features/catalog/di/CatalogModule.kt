package com.zycommerce.android.features.catalog.di

import com.zycommerce.android.features.catalog.data.CatalogRepositoryImpl
import com.zycommerce.android.features.catalog.data.remote.CatalogApiService
import com.zycommerce.android.features.catalog.domain.repository.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogModule {

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    companion object {
        @Provides
        @Singleton
        fun provideCatalogApiService(retrofit: Retrofit): CatalogApiService =
            retrofit.create(CatalogApiService::class.java)
    }
}
