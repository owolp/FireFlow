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

package dev.zitech.settings.presentation.screen.settings

import android.content.res.Resources
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.test.platform.app.InstrumentationRegistry
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.settings.R
import dev.zitech.settings.presentation.settings.compose.Settings
import dev.zitech.settings.presentation.settings.compose.TAG_SETTINGS_CONTENT
import dev.zitech.settings.presentation.settings.compose.TAG_TELEMETRY

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
internal class SS(
    private val composeTestRule: ComposeContentTestRule
) {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val screenResources = ScreenResources(context.resources)

    val assertions = Assertions(composeTestRule, screenResources)

    //    fun launch(viewModel: SettingsViewModel) {
    fun launch() {
        composeTestRule.setContent {
            FireFlowTheme {
//                Settings(viewModel)
                Settings()
            }
        }
    }

    class ScreenResources(resources: Resources) {
        val telemetryTitle = resources.getString(R.string.data_choices_telemetry_title)
        val telemetryDescription = resources.getString(R.string.data_choices_telemetry_title)
    }

    class Assertions(
        private val composeTestRule: ComposeContentTestRule,
        private val screenResources: ScreenResources
    ) {

        fun settingsProgressIndicatorIsVisible() {
            composeTestRule.onNodeWithTag(FireFlowProgressIndicators.PROGRESS_INDICATOR_ITEM)
                .assertIsDisplayed()
        }

        fun settingsProgressIndicatorIsNotVisible() {
            composeTestRule.onNodeWithTag(FireFlowProgressIndicators.PROGRESS_INDICATOR_ITEM)
                .assertDoesNotExist()
        }

        fun settingsContentIsVisible() {
            composeTestRule.onNodeWithTag(TAG_SETTINGS_CONTENT)
                .assertIsDisplayed()
        }

        fun telemetryTitleIsVisible() {
            composeTestRule.onNodeWithText(screenResources.telemetryTitle)
                .assertIsDisplayed()
        }

        fun telemetryDescriptionIsVisible() {
            composeTestRule.onNodeWithText(screenResources.telemetryDescription)
                .assertIsDisplayed()
        }

        fun telemetrySwitchIsOn() {
            composeTestRule.onNode(
                hasAnyAncestor(hasTestTag(TAG_TELEMETRY)) and isToggleable()
            ).assertIsOn()
        }

        fun telemetrySwitchIsOff() {
            composeTestRule.onNode(
                hasAnyAncestor(hasTestTag(TAG_TELEMETRY)) and isToggleable()
            ).assertIsOff()
        }

        fun telemetryTitleIsNotVisible() {
            composeTestRule.onNodeWithText(screenResources.telemetryDescription)
                .assertDoesNotExist()
        }

        fun telemetryDescriptionIsNotVisible() {
            composeTestRule.onNodeWithText(screenResources.telemetryDescription)
                .assertDoesNotExist()
        }
    }
}
