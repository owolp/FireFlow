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

package dev.zitech.fireflow.onboarding.data.source

import android.content.res.Resources
import dev.zitech.fireflow.common.data.resource.readRawJson
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.onboarding.R
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.withContext

internal class RawResourceSource @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val resources: Resources
) : ResourceSource {

    override suspend fun getAdjectives(): OperationResult<List<String>> =
        withContext(appDispatchers.io) {
            try {
                OperationResult.Success(readRawJson(resources, R.raw.adjectives))
            } catch (e: IOException) {
                OperationResult.Failure(
                    Error.Fatal(e, Error.Fatal.Type.DISK)
                )
            }
        }

    override suspend fun getNouns(): OperationResult<List<String>> =
        withContext(appDispatchers.io) {
            try {
                OperationResult.Success(readRawJson(resources, R.raw.nouns))
            } catch (e: IOException) {
                OperationResult.Failure(
                    Error.Fatal(e, Error.Fatal.Type.DISK)
                )
            }
        }
}
