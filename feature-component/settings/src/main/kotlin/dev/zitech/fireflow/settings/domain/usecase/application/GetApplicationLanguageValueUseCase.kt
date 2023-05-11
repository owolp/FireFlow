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

package dev.zitech.fireflow.settings.domain.usecase.application

import dev.zitech.fireflow.common.domain.model.application.ApplicationLanguage
import dev.zitech.fireflow.settings.frawework.locale.ApplicationLocale
import javax.inject.Inject

/**
 * Use case responsible for retrieving the current application language value.
 *
 * @param applicationLocale The implementation of [ApplicationLocale] used to retrieve the application language.
 */
internal class GetApplicationLanguageValueUseCase @Inject constructor(
    private val applicationLocale: ApplicationLocale
) {

    /**
     * Retrieves the current application language value.
     *
     * @return The [ApplicationLanguage] representing the current application language.
     */
    operator fun invoke(): ApplicationLanguage = applicationLocale.get()
}
