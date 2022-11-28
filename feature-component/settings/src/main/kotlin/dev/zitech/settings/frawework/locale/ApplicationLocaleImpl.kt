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

package dev.zitech.settings.frawework.locale

import androidx.appcompat.app.AppCompatDelegate.getApplicationLocales
import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat.forLanguageTags
import androidx.core.os.LocaleListCompat.getEmptyLocaleList
import dev.zitech.core.common.domain.model.ApplicationLanguage
import javax.inject.Inject

internal class ApplicationLocaleImpl @Inject constructor() : ApplicationLocale {

    override fun set(applicationLanguage: ApplicationLanguage) {
        val appLocale = applicationLanguage.locale?.run {
            forLanguageTags(this.toLanguageTag())
        } ?: getEmptyLocaleList()

        setApplicationLocales(appLocale)
    }

    override fun get(): ApplicationLanguage =
        ApplicationLanguage.values().firstOrNull {
            it.locale?.toLanguageTag() == getApplicationLocales().toLanguageTags()
        } ?: ApplicationLanguage.SYSTEM
}
