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

package dev.zitech.fireflow.common.domain.usecase.reporter

import dev.zitech.fireflow.common.domain.model.user.UserLoggedState
import dev.zitech.fireflow.common.domain.repository.reporter.AnalyticsRepository
import dev.zitech.fireflow.common.domain.usecase.user.GetUserLoggedStateUseCase
import javax.inject.Inject

class SetAllowPersonalizedAdsUseCase @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    private val getAllowPersonalizedAdsValueUseCase: GetAllowPersonalizedAdsValueUseCase,
    private val getUserLoggedStateUseCase: GetUserLoggedStateUseCase
) {

    suspend operator fun invoke(enabled: Boolean? = null) =
        (
            enabled ?: when (getUserLoggedStateUseCase()) {
                UserLoggedState.LOGGED_IN -> getAllowPersonalizedAdsValueUseCase()
                UserLoggedState.LOGGED_OUT -> false
            }
            ).run {
            analyticsRepository.setAllowPersonalizedAds(this)
        }
}
