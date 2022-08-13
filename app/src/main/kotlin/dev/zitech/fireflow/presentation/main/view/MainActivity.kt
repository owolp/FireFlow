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
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.zitech.fireflow.presentation.FireFlowApp
import dev.zitech.fireflow.presentation.main.viewmodel.Idle
import dev.zitech.fireflow.presentation.main.viewmodel.MainViewModel
import dev.zitech.fireflow.presentation.main.viewmodel.ShowError
import dev.zitech.fireflow.presentation.main.viewmodel.ShowErrorHandled

@Suppress("ForbiddenComment")
@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalLifecycleComposeApi::class
)
@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.splash
            }
        }

        super.onCreate(savedInstanceState)

        setContent {
            val mainState = viewModel.state.collectAsStateWithLifecycle()

            FireFlowApp(
                viewModel.state.value.splash,
                mainState.value.theme,
                calculateWindowSizeClass(this)
            )
            EventHandler()
        }
    }

    @Composable
    private fun EventHandler() {
        when (viewModel.state.value.event) {
            ShowError -> {
                // TODO
                viewModel.sendIntent(ShowErrorHandled)
            }
            Idle -> {
                // NO_OP
            }
        }
    }
}
