package com.zippyyum.commerce.data.auth

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AuthMapperTest {
    @Test
    fun `register response maps to domain user`() {
        val dto = RegisterUserResponseDto(userId = "5b71c1b4-3a0b-4c09-9cc1-255a12044f39", email = "user@example.com")

        val user = dto.toDomain()

        assertThat(user.userId).isEqualTo("5b71c1b4-3a0b-4c09-9cc1-255a12044f39")
        assertThat(user.email).isEqualTo("user@example.com")
    }
}
