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

package dev.zitech.fireflow.presentation.main.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.zitech.fireflow.R
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.presentation.FireFlowApp
import dev.zitech.fireflow.presentation.main.viewmodel.MainViewModel
import dev.zitech.fireflow.presentation.main.viewmodel.ScreenResumed

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
// Using AppCompatActivity, since ComponentActivity doesn't support language change
internal class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val tag = Logger.tag(this::class.java)

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
            val mainState by viewModel.state.collectAsStateWithLifecycle()

            val navController = rememberNavController()

            FireFlowApp(
                theme = mainState.theme,
                windowSizeClass = calculateWindowSizeClass(this),
                navController = navController,
                connectivity = mainState.connectivity,
                splash = mainState.splash
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.d(tag, "onResume, `intent.data=${intent.data}`")
        viewModel.receiveIntent(
            ScreenResumed(
                code = intent.data?.getQueryParameter(QUERY_PARAMETER_CODE),
                host = intent.data?.host,
                scheme = intent.data?.scheme,
                state = intent.data?.getQueryParameter(QUERY_PARAMETER_STATE)
            )
        )
    }

    private companion object {
        const val QUERY_PARAMETER_CODE = "code"
        const val QUERY_PARAMETER_STATE = "state"
    }
}
