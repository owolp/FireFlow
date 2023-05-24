/*
 * Copyright (C) 2023 Zitech Ltd.
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

package dev.zitech.fireflow.ds.templates.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.zitech.fireflow.ds.molecules.snackbar.ErrorSnackbarVisuals
import dev.zitech.fireflow.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.fireflow.ds.molecules.snackbar.FireFlowSnackbars
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.ds.theme.FireFlowTheme

object FireFlowScaffolds {

    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        snackbarState: FireFlowSnackbarState = rememberSnackbarState(),
        topBar: @Composable () -> Unit = {},
        bottomBar: @Composable () -> Unit = {},
        floatingActionButton: @Composable () -> Unit = {},
        floatingActionButtonPosition: FabPosition = FabPosition.End,
        containerColor: Color = FireFlowTheme.colors.background,
        contentColor: Color = contentColorFor(containerColor),
        contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
        content: @Composable (PaddingValues) -> Unit
    ) {
        Scaffold(
            modifier = modifier,
            snackbarHost = {
                SnackbarHost(snackbarState.snackbarHostState) { snackbarData ->
                    when (snackbarData.visuals) {
                        is ErrorSnackbarVisuals -> {
                            FireFlowSnackbars.Error(
                                message = snackbarData.visuals.message,
                                actionLabel = snackbarData.visuals.actionLabel,
                                onAction = { snackbarData.performAction() }
                            )
                        }
                        else -> {
                            FireFlowSnackbars.Primary(
                                message = snackbarData.visuals.message,
                                actionLabel = snackbarData.visuals.actionLabel,
                                onAction = { snackbarData.performAction() }
                            )
                        }
                    }
                }
            },
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content
        )
    }
}
