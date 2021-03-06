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

package dev.zitech.fireflow.presentation.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.fireflow.presentation.main.viewmodel.Idle
import dev.zitech.fireflow.presentation.main.viewmodel.MainState
import dev.zitech.fireflow.presentation.main.viewmodel.MainViewModel
import dev.zitech.fireflow.presentation.main.viewmodel.ShowError
import dev.zitech.fireflow.presentation.main.viewmodel.ShowErrorHandled
import dev.zitech.settings.presentation.settings.compose.Settings

@Suppress("ForbiddenComment")
@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.state.value.splash
            }
        }

        super.onCreate(savedInstanceState)

        setContent {
            val mainState = mainViewModel.state.collectAsStateWithLifecycle()
            FireFlowTheme(
                darkTheme = isDarkTheme(mainState.value.theme)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FireFlowTheme.colors.background),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // TODO: Use navigation
                    if (!mainState.value.splash) {
                        Settings()
                    }
                }
                EventHandler(mainState)
            }
        }
    }

    @Composable
    private fun EventHandler(mainState: State<MainState>) {
        when (mainState.value.event) {
            ShowError -> {
                // TODO
                mainViewModel.sendIntent(ShowErrorHandled)
            }
            Idle -> {
                // NO_OP
            }
        }
    }

    @Composable
    private fun isDarkTheme(applicationTheme: ApplicationTheme?): Boolean =
        when (applicationTheme) {
            ApplicationTheme.DARK -> true
            ApplicationTheme.LIGHT -> false
            else -> isSystemInDarkTheme()
        }
}
