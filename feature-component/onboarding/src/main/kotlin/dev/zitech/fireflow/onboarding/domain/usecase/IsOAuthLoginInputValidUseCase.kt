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

package dev.zitech.fireflow.onboarding.domain.usecase

import dev.zitech.fireflow.common.presentation.validator.Validator
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorClientId
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorClientSecret
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorServerAddress
import javax.inject.Inject

/**
 * Use case for validating OAuth login input.
 *
 * @property clientIdValidator The validator for the client ID.
 * @property clientSecretValidator The validator for the client secret.
 * @property serverAddressValidator The validator for the server address.
 */
internal class IsOAuthLoginInputValidUseCase @Inject constructor(
    @ValidatorClientId private val clientIdValidator: Validator<String>,
    @ValidatorClientSecret private val clientSecretValidator: Validator<String>,
    @ValidatorServerAddress private val serverAddressValidator: Validator<String>
) {

    /**
     * Validates the provided OAuth login input.
     *
     * @param clientId The client ID to validate.
     * @param clientSecret The client secret to validate.
     * @param serverAddress The server address to validate.
     * @return `true` if the input is valid, `false` otherwise.
     */
    operator fun invoke(
        clientId: String,
        clientSecret: String,
        serverAddress: String
    ): Boolean = clientIdValidator(clientId) &&
        clientSecretValidator(clientSecret) &&
        serverAddressValidator(serverAddress)
}
