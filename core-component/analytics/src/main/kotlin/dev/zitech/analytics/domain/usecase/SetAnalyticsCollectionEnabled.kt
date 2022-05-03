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

package dev.zitech.analytics.domain.usecase

import dev.zitech.analytics.domain.repository.AnalyticsRepository
import dev.zitech.core.common.domain.model.BuildMode
import dev.zitech.core.common.framework.applicationconfig.AppConfigProvider
import javax.inject.Inject

class SetAnalyticsCollectionEnabled @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val analyticsRepository: AnalyticsRepository
) {

    operator fun invoke(enabled: Boolean) =
        analyticsRepository.setCollectionEnabled(
            when (appConfigProvider.buildMode) {
                BuildMode.RELEASE -> enabled
                BuildMode.DEBUG -> false
            }
        )
}
