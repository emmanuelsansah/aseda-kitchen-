package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AsedaRedPrimary,
    onPrimary = Color.White,
    primaryContainer = AsedaRedDark,
    onPrimaryContainer = Color.White,
    secondary = AsedaOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = AsedaOrangeLight,
    onSecondaryContainer = Color.White,
    background = AsedaBlackBg,
    onBackground = TextPrimaryDark,
    surface = AsedaDarkCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF444444)
)

private val LightColorScheme = lightColorScheme(
    primary = AsedaRedPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDADA),
    onPrimaryContainer = AsedaRedDark,
    secondary = AsedaOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0CC),
    onSecondaryContainer = Color(0xFF6B2400),
    background = AsedaWhiteBg,
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = AsedaOffWhite,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFE0E0E0)
)

@Composable
fun AsedaKitchenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
