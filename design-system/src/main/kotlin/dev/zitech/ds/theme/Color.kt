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

@file:Suppress("MagicNumber")

package dev.zitech.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val md_theme_light_primary = Color(0xFF006876)
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFF9EEFFF)
private val md_theme_light_onPrimaryContainer = Color(0xFF001F24)
private val md_theme_light_secondary = Color(0xFF006874)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFF97F0FF)
private val md_theme_light_onSecondaryContainer = Color(0xFF001F24)
private val md_theme_light_tertiary = Color(0xFF695F00)
private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
private val md_theme_light_tertiaryContainer = Color(0xFFF5E568)
private val md_theme_light_onTertiaryContainer = Color(0xFF201C00)
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_onErrorContainer = Color(0xFF410002)
private val md_theme_light_background = Color(0xFFFBFCFD)
private val md_theme_light_onBackground = Color(0xFF191C1D)
private val md_theme_light_surface = Color(0xFFFBFCFD)
private val md_theme_light_onSurface = Color(0xFF191C1D)
private val md_theme_light_surfaceVariant = Color(0xFFDBE4E6)
private val md_theme_light_onSurfaceVariant = Color(0xFF3F484A)
private val md_theme_light_outline = Color(0xFF6F797B)
private val md_theme_light_inverseOnSurface = Color(0xFFEFF1F2)
private val md_theme_light_inverseSurface = Color(0xFF2E3132)
private val md_theme_light_inversePrimary = Color(0xFF51D7EE)
private val md_theme_light_surfaceTint = Color(0xFF006876)

private val md_theme_dark_primary = Color(0xFF51D7EE)
private val md_theme_dark_onPrimary = Color(0xFF00363E)
private val md_theme_dark_primaryContainer = Color(0xFF004E59)
private val md_theme_dark_onPrimaryContainer = Color(0xFF9EEFFF)
private val md_theme_dark_secondary = Color(0xFF4FD8EB)
private val md_theme_dark_onSecondary = Color(0xFF00363D)
private val md_theme_dark_secondaryContainer = Color(0xFF004F58)
private val md_theme_dark_onSecondaryContainer = Color(0xFF97F0FF)
private val md_theme_dark_tertiary = Color(0xFFD8C84F)
private val md_theme_dark_onTertiary = Color(0xFF373100)
private val md_theme_dark_tertiaryContainer = Color(0xFF4F4700)
private val md_theme_dark_onTertiaryContainer = Color(0xFFF5E568)
private val md_theme_dark_error = Color(0xFFFFB4AB)
private val md_theme_dark_errorContainer = Color(0xFF93000A)
private val md_theme_dark_onError = Color(0xFF690005)
private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
private val md_theme_dark_background = Color(0xFF191C1D)
private val md_theme_dark_onBackground = Color(0xFFE1E3E3)
private val md_theme_dark_surface = Color(0xFF191C1D)
private val md_theme_dark_onSurface = Color(0xFFE1E3E3)
private val md_theme_dark_surfaceVariant = Color(0xFF3F484A)
private val md_theme_dark_onSurfaceVariant = Color(0xFFBFC8CA)
private val md_theme_dark_outline = Color(0xFF899294)
private val md_theme_dark_inverseOnSurface = Color(0xFF191C1D)
private val md_theme_dark_inverseSurface = Color(0xFFE1E3E3)
private val md_theme_dark_inversePrimary = Color(0xFF006876)
private val md_theme_dark_surfaceTint = Color(0xFF51D7EE)

internal val LightColorScheme = lightColorScheme(
    surfaceTint = md_theme_light_surfaceTint,
    onErrorContainer = md_theme_light_onErrorContainer,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    tertiary = md_theme_light_tertiary,
    error = md_theme_light_error,
    outline = md_theme_light_outline,
    onBackground = md_theme_light_onBackground,
    background = md_theme_light_background,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    surface = md_theme_light_surface,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    secondary = md_theme_light_secondary,
    inversePrimary = md_theme_light_inversePrimary,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    onPrimary = md_theme_light_onPrimary,
    primary = md_theme_light_primary,
    primaryContainer = md_theme_light_primaryContainer
)

internal val DarkColorScheme = darkColorScheme(
    surfaceTint = md_theme_dark_surfaceTint,
    onErrorContainer = md_theme_dark_onErrorContainer,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    tertiary = md_theme_dark_tertiary,
    error = md_theme_dark_error,
    outline = md_theme_dark_outline,
    onBackground = md_theme_dark_onBackground,
    background = md_theme_dark_background,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    surface = md_theme_dark_surface,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    secondary = md_theme_dark_secondary,
    inversePrimary = md_theme_dark_inversePrimary,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    onPrimary = md_theme_dark_onPrimary,
    primary = md_theme_dark_primary,
    primaryContainer = md_theme_dark_primaryContainer
)
