@file:Suppress("unused")

package com.significo.bugtracker

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val PrimaryColor1 = Color(0xFF0A1E49)
val PrimaryColor2 = Color(0xFFFFA091)
val SecondaryColor3 = Color(0xFFF6E6E2)
val SecondaryColor4 = Color(0xFFCDD0D8)
val Neutral0 = Color(0xFFFFFFFF)
val Neutral70 = Color(0xFF6F7277)
val Neutral90 = Color(0xFF4E5055)

val GithubGreen = Color(0xFF3D7D3F)
val GithubPurple = Color(0xFF7B52D7)
val Red = Color(0xFFD01E43)

internal val lightColors = lightColorScheme(
    primary = PrimaryColor1,
    primaryContainer = PrimaryColor2,
    secondary = PrimaryColor2,
    secondaryContainer = SecondaryColor3,
    background = Color.White,
    surface = Color.White,
    error = Red,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Neutral70,
    onSurface = Neutral70,
    onError = Color.White
)

internal val darkColors = darkColorScheme(
    primary = PrimaryColor1,
    primaryContainer = PrimaryColor2,
    secondary = PrimaryColor2,
    secondaryContainer = SecondaryColor3,
    background = Color.White,
    surface = Color.White,
    error = Red,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Neutral70,
    onSurface = Neutral70,
    onError = Color.White
)

data class ExtendedColors(
    val tertiary: Color,
    val groupedBackground: Color,
    val activeTint: Color,
    val inactiveTint: Color,
    val inactiveText: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val success: Color,
    val error: Color,
    val purple: Color
)

internal val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        tertiary = Color.Unspecified,
        groupedBackground = Color.Unspecified,
        activeTint = Color.Unspecified,
        inactiveTint = Color.Unspecified,
        inactiveText = Color.Unspecified,
        primaryText = Color.Unspecified,
        secondaryText = Color.Unspecified,
        success = Color.Unspecified,
        error = Color.Unspecified,
        purple = Color.Unspecified
    )
}

internal val extendedLightColors = ExtendedColors(
    tertiary = Neutral0,
    groupedBackground = SecondaryColor4,
    activeTint = Neutral90,
    inactiveTint = SecondaryColor4,
    inactiveText = Neutral70,
    primaryText = Neutral90,
    secondaryText = PrimaryColor1,
    success = GithubGreen,
    error = Red,
    purple = GithubPurple
)

internal val extendedDarkColors = ExtendedColors(
    tertiary = Neutral0,
    groupedBackground = SecondaryColor4,
    activeTint = Neutral90,
    inactiveTint = SecondaryColor4,
    inactiveText = Neutral70,
    primaryText = Neutral90,
    secondaryText = PrimaryColor1,
    success = GithubGreen,
    error = Red,
    purple = GithubPurple
)
