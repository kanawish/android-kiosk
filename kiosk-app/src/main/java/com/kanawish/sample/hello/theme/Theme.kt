package com.kanawish.sample.hello.theme

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    onPrimary = Color(0xFFFFC107),
    secondary = Color.White,
    onSecondary = Color(0xFF0943A2),
    tertiary = Color.DarkGray,
    onTertiary = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color.DarkGray,
    onSurfaceVariant = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    outline = Color.DarkGray,
)

private val FooLightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC107), // #775a0b
    onPrimary = Color.Black,
    secondary = Color(0xFF0943A2),
    onSecondary = Color.White,
    tertiary = Color.LightGray,
    onTertiary = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.LightGray,
    onSurfaceVariant = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    outline = Color.LightGray,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    secondaryContainer = Color(0xFFDBE1FF),
    onSecondaryContainer = Color(0xFF001849),
    tertiaryContainer = Color(0xFFE6E0E9),
    onTertiaryContainer = Color(0xFF1D1B20),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFB4C6FF),
    surfaceTint = Color(0xFFFFC107),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0x52000000)
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Typography
        content = content
    )
}
