package com.zippyyum.commerce.data.auth

import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.RegisteredUser
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun register(email: String, password: String): AppResult<RegisteredUser> = try {
        val response = authApi.register(RegisterUserRequestDto(email = email, password = password))
        AppResult.Success(response.toDomain())
    } catch (_: IOException) {
        AppResult.Failure(AppError.NetworkUnavailable)
    } catch (exception: HttpException) {
        AppResult.Failure(exception.toAppError())
    } catch (exception: RuntimeException) {
        AppResult.Failure(AppError.Unknown(exception.message))
    }

    private fun HttpException.toAppError(): AppError = when (code()) {
        400, 422 -> AppError.Validation(emptyMap())
        409 -> AppError.Conflict(message())
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        else -> AppError.Unknown(message())
    }
}
