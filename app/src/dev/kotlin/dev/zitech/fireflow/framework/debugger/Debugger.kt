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

package dev.zitech.fireflow.framework.debugger

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin.SharedPreferencesDescriptor
import com.facebook.soloader.SoLoader
import dev.zitech.fireflow.common.data.remote.rest.debugger.NetworkDebugger.networkFlipperPlugin
import dev.zitech.fireflow.common.data.source.preferences.DEVELOPMENT_PREFERENCES_NAME
import dev.zitech.fireflow.common.data.source.preferences.SECURED_PREFERENCES_NAME
import dev.zitech.fireflow.common.data.source.preferences.STANDARD_PREFERENCES_NAME

object Debugger {

    fun init(context: Context) {
        SoLoader.init(context, false)

        val client = AndroidFlipperClient.getInstance(context)

        with(client) {
            addPlugin(CrashReporterPlugin.getInstance())
            addPlugin(DatabasesFlipperPlugin(context))
            addPlugin(NavigationFlipperPlugin.getInstance())
            addPlugin(networkFlipperPlugin)
            addPlugin(
                SharedPreferencesFlipperPlugin(
                    context,
                    getSharedPreferencesDescriptors()
                )
            )
            start()
        }
    }

    private fun getSharedPreferencesDescriptors() = listOf(
        SharedPreferencesDescriptor(DEVELOPMENT_PREFERENCES_NAME, Context.MODE_PRIVATE),
        SharedPreferencesDescriptor(SECURED_PREFERENCES_NAME, Context.MODE_PRIVATE),
        SharedPreferencesDescriptor(STANDARD_PREFERENCES_NAME, Context.MODE_PRIVATE)
    )
}
