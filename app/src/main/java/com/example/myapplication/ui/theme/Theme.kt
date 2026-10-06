package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BmkgDarkPrimary,
    onPrimary = BmkgDarkOnPrimary,
    primaryContainer = BmkgDarkPrimaryContainer,
    onPrimaryContainer = BmkgDarkOnPrimaryContainer,
    secondary = BmkgDarkSecondary,
    onSecondary = BmkgDarkOnSecondary,
    secondaryContainer = BmkgDarkSecondaryContainer,
    onSecondaryContainer = BmkgDarkOnSecondaryContainer,
    tertiary = BmkgDarkTertiary,
    onTertiary = BmkgDarkOnTertiary,
    tertiaryContainer = BmkgDarkTertiaryContainer,
    onTertiaryContainer = BmkgDarkOnTertiaryContainer,
    background = BmkgDarkBackground,
    onBackground = BmkgDarkOnBackground,
    surface = BmkgDarkSurface,
    onSurface = BmkgDarkOnSurface,
    surfaceVariant = BmkgDarkSurfaceVariant,
    onSurfaceVariant = BmkgDarkOnSurfaceVariant,
    outline = BmkgDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = BmkgBluePrimary,
    onPrimary = BmkgBlueOnPrimary,
    primaryContainer = BmkgBlueContainer,
    onPrimaryContainer = BmkgBlueOnContainer,
    secondary = BmkgAmberSecondary,
    onSecondary = BmkgAmberOnSecondary,
    secondaryContainer = BmkgAmberContainer,
    onSecondaryContainer = BmkgAmberOnContainer,
    tertiary = BmkgTealTertiary,
    onTertiary = BmkgTealOnTertiary,
    tertiaryContainer = BmkgTealContainer,
    onTertiaryContainer = BmkgTealOnContainer,
    background = BmkgLightBackground,
    onBackground = BmkgLightOnBackground,
    surface = BmkgLightSurface,
    onSurface = BmkgLightOnSurface,
    surfaceVariant = BmkgLightSurfaceVariant,
    onSurfaceVariant = BmkgLightOnSurfaceVariant,
    outline = BmkgLightOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize our custom BMKG brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}