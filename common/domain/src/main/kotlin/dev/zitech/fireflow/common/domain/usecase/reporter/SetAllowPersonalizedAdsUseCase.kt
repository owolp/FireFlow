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
import dev.zitech.fireflow.core.result.getResultOrDefault
import javax.inject.Inject

/**
 * Use case for setting the value of the "Allow Personalized Ads" setting.
 *
 * @property analyticsRepository The repository for managing analytics settings.
 * @property getAllowPersonalizedAdsValueUseCase The use case for retrieving the value of the "Allow Personalized Ads"
 * setting.
 * @property getUserLoggedStateUseCase The use case for retrieving the logged-in state of the user.
 */
class SetAllowPersonalizedAdsUseCase @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    private val getAllowPersonalizedAdsValueUseCase: GetAllowPersonalizedAdsValueUseCase,
    private val getUserLoggedStateUseCase: GetUserLoggedStateUseCase
) {
    /**
     * Invokes the use case to set the value of the "Allow Personalized Ads" setting.
     *
     * @param enabled The new value of the "Allow Personalized Ads" setting. If null, the value will be determined based
     * on the user's logged-in state.
     */
    suspend operator fun invoke(enabled: Boolean? = null) =
        (
            enabled ?: when (getUserLoggedStateUseCase()) {
                UserLoggedState.LOGGED_IN -> getAllowPersonalizedAdsValueUseCase().getResultOrDefault(
                    false
                )
                UserLoggedState.LOGGED_OUT -> false
            }
            ).run {
            analyticsRepository.setAllowPersonalizedAds(this)
        }
}
