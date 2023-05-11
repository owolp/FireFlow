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

package dev.zitech.fireflow.common.data.local.database.factory

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.core.concurrency.SingleRunner
import javax.inject.Inject

/**
 * Factory class for creating Room databases.
 *
 * @param context The application context.
 * @param securedPreferencesDataSource The data source for accessing secured preferences.
 * @param singleRunner The single runner for executing database operations sequentially.
 */
internal class DatabaseFactory @Inject constructor(
    private val context: Context,
    @Suppress("UnusedPrivateMember") private val securedPreferencesDataSource: PreferencesDataSource,
    private val singleRunner: SingleRunner
) {

    suspend inline fun <reified T : RoomDatabase> createDatabase(
        databaseTitle: DatabaseTitle
    ): T = singleRunner.afterPrevious {
        val roomBuilder = Room.databaseBuilder(
            context,
            T::class.java,
            databaseTitle.name
        )

        roomBuilder.build()
    }
}
