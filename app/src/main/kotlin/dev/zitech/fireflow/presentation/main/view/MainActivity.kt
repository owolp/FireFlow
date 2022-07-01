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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import dagger.hilt.android.AndroidEntryPoint
import dev.zitech.core.common.framework.flow.collectWhenStarted
import dev.zitech.core.common.presentation.architecture.MviView
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.fireflow.presentation.main.viewmodel.Idle
import dev.zitech.fireflow.presentation.main.viewmodel.MainState
import dev.zitech.fireflow.presentation.main.viewmodel.MainViewModel
import dev.zitech.fireflow.presentation.main.viewmodel.ShowError
import dev.zitech.fireflow.presentation.main.viewmodel.ShowErrorHandled
import dev.zitech.settings.presentation.settings.compose.Settings

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
internal class MainActivity : ComponentActivity(), MviView<MainState> {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.showSplashScreen.value!!
            }
        }

        super.onCreate(savedInstanceState)

        mainViewModel.state.collectWhenStarted(this, this::render)

        setContent {
            FireFlowTheme {
                Settings()
            }
        }
    }

    override fun render(state: MainState) {
        when (state.event) {
            ShowError -> {
                // TODO
                mainViewModel.sendIntent(ShowErrorHandled)
            }
            Idle -> {
                // NO_OP
            }
        }
    }
}
