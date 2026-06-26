package com.zippyyum.commerce.feature.auth

import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.GetActiveSessionUseCase
import com.zippyyum.commerce.domain.auth.LoginUserUseCase
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

    @Provides
    fun provideLoginUserUseCase(authRepository: AuthRepository): LoginUserUseCase = LoginUserUseCase(authRepository)

    @Provides
    fun provideGetActiveSessionUseCase(authRepository: AuthRepository): GetActiveSessionUseCase =
        GetActiveSessionUseCase(authRepository)
}
