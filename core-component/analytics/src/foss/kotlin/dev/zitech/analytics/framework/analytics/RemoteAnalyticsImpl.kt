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

package dev.zitech.analytics.framework.analytics

import android.content.Context
import javax.inject.Inject

@Suppress("UnusedPrivateMember", "UNUSED_PARAMETER")
internal class RemoteAnalyticsImpl @Inject constructor(
    context: Context
) : RemoteAnalytics {

    override fun allowPersonalizedAds(enabled: Boolean) {
        // NO_OP
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        // NO_OP
    }
}
