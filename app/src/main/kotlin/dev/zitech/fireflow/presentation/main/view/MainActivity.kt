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
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.zitech.core.common.presentation.splash.SplashScreenStateController
import dev.zitech.fireflow.R
import dev.zitech.fireflow.presentation.FireFlowApp
import dev.zitech.fireflow.presentation.main.viewmodel.Idle
import dev.zitech.fireflow.presentation.main.viewmodel.MainViewModel
import dev.zitech.fireflow.presentation.main.viewmodel.ShowError
import dev.zitech.fireflow.presentation.main.viewmodel.ShowErrorHandled
import javax.inject.Inject

@Suppress("ForbiddenComment")
@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalLifecycleComposeApi::class
)
@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var splashScreenStateController: SplashScreenStateController

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            installSplashScreen().apply {
                setKeepOnScreenCondition {
                    viewModel.state.value.splash
                }
            }
        } else {
            // https://stackoverflow.com/a/69846767
            setTheme(R.style.Theme_FireFlow)
        }

        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val mainState = viewModel.state.collectAsStateWithLifecycle()

            val navController = rememberNavController()
            FireFlowApp(
                mainState.value.theme,
                calculateWindowSizeClass(this),
                navController,
                splashScreenStateController
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
