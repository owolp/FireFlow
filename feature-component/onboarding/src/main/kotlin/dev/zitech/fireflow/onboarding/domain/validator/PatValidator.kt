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

package dev.zitech.fireflow.onboarding.domain.validator

import dev.zitech.fireflow.common.presentation.validator.Validator
import javax.inject.Inject

/**
 * Validator for validating the Personal Access Token (PAT).
 */
internal class PatValidator @Inject constructor() : Validator<String> {

    /**
     * Validates the Personal Access Token (PAT).
     *
     * @param input The Personal Access Token to validate.
     * @return `true` if the PAT is valid, `false` otherwise.
     */
    override fun invoke(input: String): Boolean =
        input.isNotBlank() && input.isNotEmpty()
}
