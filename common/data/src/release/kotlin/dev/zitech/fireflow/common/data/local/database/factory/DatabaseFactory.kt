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
import dev.zitech.fireflow.core.datafactory.DataFactory
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

internal class DatabaseFactory @Inject constructor(
    private val context: Context,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val singleRunner: SingleRunner
) {

    suspend inline fun <reified T : RoomDatabase> createDatabase(
        fireFlowDatabase: FireFlowDatabase
    ): T = singleRunner.afterPrevious {
        val roomBuilder = Room.databaseBuilder(
            context,
            T::class.java,
            fireFlowDatabase.name
        )

        if (!isSecuredStorageKeySaved()) {
            saveSecuredStorageKey()
        }

        val databaseKey = securedPreferencesDataSource
            .getString(KEY_SECURED_STORAGE_SECURED_DATABASE, "")
            .first()?.toCharArray()!!

        roomBuilder.openHelperFactory(
            SupportFactory(
                SQLiteDatabase.getBytes(
                    databaseKey
                )
            )
        )

        roomBuilder.build()
    }

    suspend fun isSecuredStorageKeySaved() =
        !securedPreferencesDataSource.containsString(KEY_SECURED_STORAGE_SECURED_DATABASE).first()

    suspend fun saveSecuredStorageKey() {
        securedPreferencesDataSource.saveString(
            KEY_SECURED_STORAGE_SECURED_DATABASE,
            DataFactory.createRandomString(ENCRYPTION_LENGTH)
        )
    }

    companion object {
        private const val ENCRYPTION_LENGTH = 20
        private const val KEY_SECURED_STORAGE_SECURED_DATABASE = "secured_database"
    }
}
