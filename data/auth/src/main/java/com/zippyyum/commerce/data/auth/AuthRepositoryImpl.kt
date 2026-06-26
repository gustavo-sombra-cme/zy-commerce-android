package com.zippyyum.commerce.data.auth

import com.zippyyum.commerce.core.common.AppError
import com.zippyyum.commerce.core.common.AppResult
import com.zippyyum.commerce.core.storage.SessionStorage
import com.zippyyum.commerce.domain.auth.AuthRepository
import com.zippyyum.commerce.domain.auth.RegisteredUser
import com.zippyyum.commerce.domain.auth.UserSession
import java.io.IOException
import java.time.Instant
import javax.inject.Inject
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val json: Json,
    private val sessionStorage: SessionStorage,
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

    override suspend fun login(email: String, password: String): AppResult<UserSession> = try {
        val session = authApi.login(LoginUserRequestDto(email = email, password = password)).toDomain()
        sessionStorage.saveSession(session.toStoredSession())
        AppResult.Success(session)
    } catch (_: IOException) {
        AppResult.Failure(AppError.NetworkUnavailable)
    } catch (exception: HttpException) {
        AppResult.Failure(exception.toAppError())
    } catch (exception: RuntimeException) {
        AppResult.Failure(AppError.Unknown(exception.message))
    }

    override suspend fun getActiveSession(): UserSession? {
        val session = runCatching {
            sessionStorage.getSession()?.toDomain()
        }.getOrElse {
            sessionStorage.clearSession()
            return null
        } ?: return null

        return if (session.expiresAt.isAfter(Instant.now())) {
            session
        } else {
            sessionStorage.clearSession()
            null
        }
    }

    override suspend fun clearSession() {
        sessionStorage.clearSession()
    }

    private fun HttpException.toAppError(): AppError = when (code()) {
        400, 422 -> AppError.Validation(parseValidationErrors())
        409 -> AppError.Conflict("That email is already registered.")
        401 -> AppError.Unauthorized
        403 -> AppError.Forbidden
        404 -> AppError.NotFound
        else -> AppError.Unknown(message())
    }

    private fun HttpException.parseValidationErrors(): Map<String, List<String>> {
        val responseBody = response()?.errorBody()?.string().orEmpty()
        if (responseBody.isBlank()) return emptyMap()

        return runCatching {
            json.decodeFromString<ValidationErrorResponseDto>(responseBody)
                .errors
                .mapKeys { (field, _) -> field.lowercase() }
        }.getOrElse { emptyMap() }
    }
}
