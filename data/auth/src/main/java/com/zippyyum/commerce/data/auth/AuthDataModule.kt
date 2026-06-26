package com.zippyyum.commerce.data.auth

import android.content.Context
import com.zippyyum.commerce.core.storage.SecureSessionStorage
import com.zippyyum.commerce.core.storage.SessionStorage
import com.zippyyum.commerce.domain.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideSessionStorage(@ApplicationContext context: Context): SessionStorage = SecureSessionStorage(context)
}
