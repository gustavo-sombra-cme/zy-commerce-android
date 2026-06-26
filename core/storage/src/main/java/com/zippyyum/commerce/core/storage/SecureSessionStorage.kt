package com.zippyyum.commerce.core.storage

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
private const val KEY_ALIAS = "zy_commerce_session_key"
private const val TRANSFORMATION = "AES/GCM/NoPadding"
private const val PREFS_NAME = "zy_commerce_secure_session"
private const val KEY_USER_ID = "user_id"
private const val KEY_EMAIL = "email"
private const val KEY_ACCESS_TOKEN = "access_token"
private const val KEY_TOKEN_TYPE = "token_type"
private const val KEY_EXPIRES_AT = "expires_at"

interface SessionStorage {
    fun saveSession(session: StoredSession)

    fun getSession(): StoredSession?

    fun clearSession()
}

data class StoredSession(
    val userId: String,
    val email: String,
    val accessToken: String,
    val tokenType: String,
    val expiresAt: String,
)

class SecureSessionStorage(context: Context) : SessionStorage {
    private val preferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveSession(session: StoredSession) {
        preferences.edit()
            .putString(KEY_USER_ID, encrypt(session.userId))
            .putString(KEY_EMAIL, encrypt(session.email))
            .putString(KEY_ACCESS_TOKEN, encrypt(session.accessToken))
            .putString(KEY_TOKEN_TYPE, encrypt(session.tokenType))
            .putString(KEY_EXPIRES_AT, encrypt(session.expiresAt))
            .apply()
    }

    override fun getSession(): StoredSession? {
        val userId = preferences.getString(KEY_USER_ID, null)?.let(::decrypt) ?: return null
        val email = preferences.getString(KEY_EMAIL, null)?.let(::decrypt) ?: return null
        val accessToken = preferences.getString(KEY_ACCESS_TOKEN, null)?.let(::decrypt) ?: return null
        val tokenType = preferences.getString(KEY_TOKEN_TYPE, null)?.let(::decrypt) ?: return null
        val expiresAt = preferences.getString(KEY_EXPIRES_AT, null)?.let(::decrypt) ?: return null

        return StoredSession(
            userId = userId,
            email = email,
            accessToken = accessToken,
            tokenType = tokenType,
            expiresAt = expiresAt,
        )
    }

    override fun clearSession() {
        preferences.edit().clear().apply()
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        val encryptedBytes = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))
        val payload = cipher.iv + encryptedBytes
        return Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    private fun decrypt(value: String): String {
        val decoded = Base64.decode(value, Base64.NO_WRAP)
        val iv = decoded.copyOfRange(0, 12)
        val encryptedBytes = decoded.copyOfRange(12, decoded.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }
}
