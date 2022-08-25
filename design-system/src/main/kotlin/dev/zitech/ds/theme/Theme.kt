/*
 * Copyright (C) 2022 Zitech Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.zitech.ds.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun FireFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    shapes: Shapes = Shape,
    space: Space = Space(),
    typography: Typography = Typography,
    content: @Composable () -> Unit
) {
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.surface,
            darkIcons = !darkTheme
        )
    }

    ProvideFireFlowTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        space = space,
        typography = typography
    ) {
        MaterialTheme(
            colorScheme = debugColors(),
            content = content
        )
    }
}

@Composable
private fun ProvideFireFlowTheme(
    colorScheme: ColorScheme,
    shapes: Shapes,
    space: Space,
    typography: Typography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalShapes provides shapes,
        LocalSpace provides space,
        LocalTypography provides typography,
        content = content
    )
}

object FireFlowTheme {

    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val space: Space
        @Composable
        @ReadOnlyComposable
        get() = LocalSpace.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

/**
 * A Material [ColorScheme] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [FireFlowTheme.colors].
 */
fun debugColors(
    debugColor: Color = Color.Magenta
) = ColorScheme(
    primary = debugColor,
    surfaceTint = debugColor,
    onErrorContainer = debugColor,
    onError = debugColor,
    errorContainer = debugColor,
    onTertiaryContainer = debugColor,
    onTertiary = debugColor,
    tertiaryContainer = debugColor,
    tertiary = debugColor,
    error = debugColor,
    outline = debugColor,
    onBackground = debugColor,
    background = debugColor,
    inverseOnSurface = debugColor,
    inverseSurface = debugColor,
    onSurfaceVariant = debugColor,
    onSurface = debugColor,
    surfaceVariant = debugColor,
    surface = debugColor,
    onSecondaryContainer = debugColor,
    onSecondary = debugColor,
    secondaryContainer = debugColor,
    secondary = debugColor,
    inversePrimary = debugColor,
    onPrimaryContainer = debugColor,
    onPrimary = debugColor,
    primaryContainer = debugColor,
    outlineVariant = debugColor,
    scrim = debugColor
)

private val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("No FireFlow ColorScheme provided, check if Theme Composable is added")
}

private val LocalShapes = staticCompositionLocalOf<Shapes> {
    error("No FireFlow Shapes provided, check if Theme Composable is added")
}

private val LocalSpace = staticCompositionLocalOf<Space> {
    error("No FireFlow Spacing provided, check if Theme Composable is added")
}

private val LocalTypography = staticCompositionLocalOf<Typography> {
    error("No FireFlow Typography provided, check if Theme Composable is added")
}
