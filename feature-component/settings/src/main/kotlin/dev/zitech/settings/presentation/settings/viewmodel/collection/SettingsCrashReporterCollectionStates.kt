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

package dev.zitech.settings.presentation.settings.viewmodel.collection

import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import javax.inject.Inject

class SettingsCrashReporterCollectionStates @Inject constructor(
    private val getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase,
    private val setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase
) {

    internal suspend fun setCrashReporterCollection(checked: Boolean) {
        setCrashReporterCollectionUseCase(checked)
    }

    internal suspend fun getCrashReporterCollectionValue(): Boolean =
        getCrashReporterCollectionValueUseCase()
}
