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

package dev.zitech.ds.molecules.snackbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberSnackbarState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, coroutineScope) {
    FireFlowSnackbarState(snackbarHostState, coroutineScope)
}

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class FireFlowSnackbarState(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {

    fun showMessage(message: BottomNotifierMessage) {
        coroutineScope.launch {
            val result = when (message.state) {
                BottomNotifierMessage.State.DEFAULT -> {
                    snackbarHostState.showSnackbar(
                        actionLabel = message.action?.label,
                        duration = getDuration(message.duration),
                        message = message.text,
                        withDismissAction = getWithDismissAction(message.duration)
                    )
                }
                BottomNotifierMessage.State.ERROR -> {
                    snackbarHostState.showSnackbar(
                        ErrorSnackbarVisuals(
                            actionLabel = message.action?.label,
                            duration = getDuration(message.duration),
                            message = message.text,
                            withDismissAction = getWithDismissAction(message.duration)
                        )
                    )
                }
            }

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    message.action?.onAction?.invoke()
                }
                SnackbarResult.Dismissed -> {
                    message.onDismiss?.invoke()
                }
            }
        }
    }

    private fun getWithDismissAction(duration: BottomNotifierMessage.Duration) =
        when (duration) {
            BottomNotifierMessage.Duration.INDEFINITE -> true
            else -> false
        }

    private fun getDuration(duration: BottomNotifierMessage.Duration) =
        when (duration) {
            BottomNotifierMessage.Duration.SHORT -> SnackbarDuration.Short
            BottomNotifierMessage.Duration.LONG -> SnackbarDuration.Long
            BottomNotifierMessage.Duration.INDEFINITE -> SnackbarDuration.Indefinite
        }
}
