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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.zitech.ds.R

@Composable
fun FireFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    shapes: Shapes = Shape,
    space: Space = Space(),
    typography: Typography = Typography,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme.copy(
            primary = colorResource(id = R.color.md_theme_dark_primary)
        )
        else -> LightColorScheme.copy(
            primary = colorResource(id = R.color.md_theme_light_primary)
        )
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.background,
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
            colorScheme = colorScheme,
            shapes = shapes,
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
