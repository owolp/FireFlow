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

package dev.zitech.fireflow.common.data.resource

import android.content.res.Resources
import androidx.annotation.RawRes
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * Reads and parses a raw JSON resource file into an object of type [T].
 *
 * This inline function reads a raw JSON resource file from the provided [resources]
 * and parses it into an object of type [T] using the Moshi JSON library.
 *
 * @param resources The Android resources object used to access the raw resource file.
 * @param rawResId The resource ID of the raw JSON file to be read.
 * @return An object of type [T] representing the parsed JSON data from the resource file.
 */
inline fun <reified T> readRawJson(resources: Resources, @RawRes rawResId: Int): T {
    resources.openRawResource(rawResId).bufferedReader().use {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return adapter.fromJson(it.readText())!!
    }
}
