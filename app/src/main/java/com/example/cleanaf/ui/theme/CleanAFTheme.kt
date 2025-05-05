package com.example.cleanaf.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1EB980),
    secondary = Color(0xFF7DCE82),
    tertiary = Color(0xFF045D56)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1EB980),
    secondary = Color(0xFF7DCE82),
    tertiary = Color(0xFF045D56)
)

@Composable
fun CleanAFTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
