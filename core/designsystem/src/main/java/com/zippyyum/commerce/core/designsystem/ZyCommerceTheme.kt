package com.zippyyum.commerce.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZyColorScheme = lightColorScheme(
    primary = Color(0xFF0F766E),
    onPrimary = Color.White,
    secondary = Color(0xFF1D4ED8),
    tertiary = Color(0xFFB45309),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
)

@Composable
fun ZyCommerceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZyColorScheme,
        content = content,
    )
}
