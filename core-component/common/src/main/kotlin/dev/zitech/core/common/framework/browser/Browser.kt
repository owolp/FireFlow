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

package dev.zitech.core.common.framework.browser

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.exception.NoBrowserInstalledException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object Browser {

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun openUrl(
        context: Context,
        url: String,
        @ColorInt toolbarColor: Int,
        activityResultLauncher: ActivityResultLauncher<Intent>? = null
    ) = callbackFlow {
        try {
            val customTabBarColor = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(toolbarColor).build()
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setDefaultColorSchemeParams(customTabBarColor)
                .build()

            if (activityResultLauncher == null) {
                customTabsIntent.launchUrl(context, url.toUri())
            } else {
                customTabsIntent.intent
                    .setAction(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_BROWSABLE)
                    .data = url.toUri()

                activityResultLauncher.launch(customTabsIntent.intent)
            }

            trySend(DataResult.Success(Unit))
            close()
        } catch (e: ActivityNotFoundException) {
            trySend(DataResult.Error(cause = NoBrowserInstalledException))
            close()
        } catch (e: Exception) {
            trySend(DataResult.Error(cause = e))
            close()
        }

        awaitClose()
    }
}
