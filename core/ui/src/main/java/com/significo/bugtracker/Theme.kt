package com.significo.bugtracker

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.significo.bugtracker.components.AppStatusBar

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    colors: ColorScheme = if (darkTheme) darkColors else lightColors,
    extendedColors: ExtendedColors = if (darkTheme) extendedDarkColors else extendedLightColors,
    colorStatusBar: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    AppStatusBar(color = colorStatusBar)

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography,
        LocalElevation provides elevation
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            shapes = shapes
        ) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.primaryText,
                LocalTextStyle provides AppTheme.typography.copyLight,
                content = content
            )
        }
    }
}

object AppTheme {
    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
    val typography: ExtendedTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedTypography.current
    val elevation: Elevation
        @Composable
        @ReadOnlyComposable
        get() = LocalElevation.current
}
