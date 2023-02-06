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

package dev.zitech.onboarding.domain.usecase

import dev.zitech.core.common.domain.validator.Validator
import dev.zitech.onboarding.di.annotation.ValidatorPat
import dev.zitech.onboarding.di.annotation.ValidatorServerAddress
import javax.inject.Inject

internal class IsPatLoginInputValidUseCase @Inject constructor(
    @ValidatorPat private val patValidator: Validator<String>,
    @ValidatorServerAddress private val serverAddressValidator: Validator<String>
) {

    operator fun invoke(
        personalAccessToken: String,
        serverAddress: String
    ): Boolean = patValidator(personalAccessToken) &&
        serverAddressValidator(serverAddress)
}
