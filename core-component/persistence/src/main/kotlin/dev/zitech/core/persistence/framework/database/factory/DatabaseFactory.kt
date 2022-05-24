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

package dev.zitech.core.persistence.framework.database.factory

import android.content.Context
import androidx.room.Room
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.model.BuildMode
import dev.zitech.core.persistence.domain.repository.database.DatabaseKeyRepository
import dev.zitech.core.persistence.framework.database.FireFlowDatabase
import javax.inject.Inject
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

internal class DatabaseFactory @Inject constructor(
    private val context: Context,
    private val appConfigProvider: AppConfigProvider,
    private val databaseKeyRepository: DatabaseKeyRepository,
) {

    suspend fun createRoomDatabase(databaseName: String): FireFlowDatabase {
        val roomBuilder = Room.databaseBuilder(
            context,
            FireFlowDatabase::class.java,
            databaseName
        )

        if (appConfigProvider.buildMode == BuildMode.RELEASE) {
            roomBuilder.openHelperFactory(
                SupportFactory(
                    SQLiteDatabase.getBytes(
                        databaseKeyRepository.getDatabaseKey().value
                    )
                )
            )
        }

        return roomBuilder.build()
    }
}
