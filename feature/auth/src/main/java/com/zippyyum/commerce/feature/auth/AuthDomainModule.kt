package com.zippyyum.commerce.feature.auth

import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthDomainModule {
    @Provides
    fun provideRegisterUserUseCase(authRepository: AuthRepository): RegisterUserUseCase = RegisterUserUseCase(authRepository)
}
