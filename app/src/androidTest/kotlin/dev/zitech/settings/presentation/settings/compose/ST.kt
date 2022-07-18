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

package dev.zitech.settings.presentation.settings.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.zitech.fireflow.presentation.main.view.MainActivity
import dev.zitech.settings.presentation.screen.settings.SS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@HiltAndroidTest
internal class ST {

    @get:Rule(order = 1)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    //    private val settingsViewModel = mockk<SettingsViewModel>()
//    @Inject
//    lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @Test
    fun when_state_isLoading_true_then_show_settings_progress_indicator() = runTest {
        // Arrange
//        every { settingsViewModel.state } returns MutableStateFlow(
//            SettingsState(
//                isLoading = true
//            )
//        )

        // Act
        val settingsScreen = SS(composeTestRule)
//        settingsScreen.launch(settingsViewModel)
        settingsScreen.launch()

        // Assert
        settingsScreen.assertions.run {
            settingsProgressIndicatorIsVisible()
        }
    }

    @Test
    fun when_state_isLoading_false_then_do_not_show_settings_progress_indicator_and_show_settings_content() {
        // Arrange
//        every { settingsViewModel.state } returns MutableStateFlow(
//            SettingsState(
//                isLoading = false
//            )
//        )

        // Act
        val settingsScreen = SS(composeTestRule)
//        settingsScreen.launch(settingsViewModel)
        settingsScreen.launch()

        // Assert
        settingsScreen.assertions.run {
            settingsProgressIndicatorIsNotVisible()
            settingsContentIsVisible()
        }
    }

    @Test
    fun when_state_isLoading_false_and_telemetry_is_true_then_show_telemetry_on() {
        // Arrange
//        every { settingsViewModel.state } returns MutableStateFlow(
//            SettingsState(
//                isLoading = false,
//                telemetry = true
//            )
//        )

        // Act
        val settingsScreen = SS(composeTestRule)
//        settingsScreen.launch(settingsViewModel)
        settingsScreen.launch()

        // Assert
        settingsScreen.assertions.run {
            settingsProgressIndicatorIsNotVisible()
            settingsContentIsVisible()
            telemetryTitleIsVisible()
            telemetryDescriptionIsVisible()
            telemetrySwitchIsOn()
        }
    }

    @Test
    fun when_state_isLoading_false_and_telemetry_is_false_then_show_telemetry_off() {
        // Arrange
//        every { settingsViewModel.state } returns MutableStateFlow(
//            SettingsState(
//                isLoading = false,
//                telemetry = false
//            )
//        )

        // Act
        val settingsScreen = SS(composeTestRule)
//        settingsScreen.launch(settingsViewModel)
        settingsScreen.launch()

        // Assert
        settingsScreen.assertions.run {
            settingsProgressIndicatorIsNotVisible()
            settingsContentIsVisible()
            telemetryTitleIsVisible()
            telemetryDescriptionIsVisible()
            telemetrySwitchIsOff()
        }
    }

    @Test
    fun when_state_isLoading_false_and_telemetry_is_null_then_do_not_show_telemetry() {
        // Arrange
//        every { settingsViewModel.state } returns MutableStateFlow(
//            SettingsState(
//                isLoading = false,
//                telemetry = null
//            )
//        )

        // Act
        val settingsScreen = SS(composeTestRule)
//        settingsScreen.launch(settingsViewModel)
        settingsScreen.launch()

        // Assert
        settingsScreen.assertions.run {
            settingsProgressIndicatorIsNotVisible()
            settingsContentIsVisible()
            telemetryTitleIsNotVisible()
            telemetryDescriptionIsNotVisible()
        }
    }

    // TODO: Crash Reporter tests
}
