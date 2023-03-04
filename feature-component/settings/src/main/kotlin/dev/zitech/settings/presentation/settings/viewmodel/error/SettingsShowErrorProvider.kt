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

package dev.zitech.settings.presentation.settings.viewmodel.error

import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.settings.R
import dev.zitech.settings.presentation.settings.viewmodel.ShowError
import javax.inject.Inject

internal class SettingsShowErrorProvider @Inject constructor(
    stringsProvider: StringsProvider
) {

    val analyticsError = ShowError(
        messageResId = R.string.data_choices_analytics_error,
        action = stringsProvider(R.string.action_restart)
    )

    val crashReporterError = ShowError(
        messageResId = R.string.data_choices_crash_reporter_error,
        action = stringsProvider(R.string.action_restart)
    )

    val personalizedAdsError = ShowError(
        messageResId = R.string.data_choices_personalized_ads_error,
        action = stringsProvider(R.string.action_restart)
    )

    val performanceError = ShowError(
        messageResId = R.string.data_choices_performance_error,
        action = stringsProvider(R.string.action_restart)
    )
}
