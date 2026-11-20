package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ZippyDarkColorScheme = darkColorScheme(
    primary = ZippyGold,
    onPrimary = ZippyOnGold,
    primaryContainer = ZippyCard2,
    onPrimaryContainer = ZippyGoldLight,
    secondary = ZippyGoldLight,
    onSecondary = ZippyOnGold,
    secondaryContainer = ZippyCard,
    onSecondaryContainer = ZippyText,
    tertiary = ZippyOk,
    onTertiary = ZippyBg,
    background = ZippyBg,
    onBackground = ZippyText,
    surface = ZippyBg2,
    onSurface = ZippyText,
    surfaceVariant = ZippyCard,
    onSurfaceVariant = ZippyMute,
    outline = ZippyLine,
    outlineVariant = ZippyLine2,
    error = ZippyBad,
    onError = ZippyBg
)

@Composable
fun ZippyRecapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ZippyDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
