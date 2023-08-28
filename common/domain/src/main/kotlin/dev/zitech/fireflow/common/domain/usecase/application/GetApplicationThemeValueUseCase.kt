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

package dev.zitech.fireflow.common.domain.usecase.application

import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.domain.repository.application.ApplicationRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the application theme value.
 *
 * @property applicationRepository The repository for accessing application data.
 */
class GetApplicationThemeValueUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository
) {

    /**
     * Invokes the use case to retrieve the application theme value.
     *
     * @return A [Flow] that emits the current application theme.
     */
    suspend operator fun invoke(): Flow<OperationResult<ApplicationTheme>> =
        applicationRepository.getApplicationTheme()
}
