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

package dev.zitech.fireflow.settings.frawework.locale

import androidx.appcompat.app.AppCompatDelegate.getApplicationLocales
import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat.forLanguageTags
import androidx.core.os.LocaleListCompat.getEmptyLocaleList
import dev.zitech.fireflow.common.domain.model.application.ApplicationLanguage
import javax.inject.Inject

/**
 * Implementation of the [ApplicationLocale] interface for managing the application locale.
 */
internal class ApplicationLocaleImpl @Inject constructor() : ApplicationLocale {

    /**
     * Sets the application language based on the provided [applicationLanguage].
     *
     * @param applicationLanguage The application language to be set.
     */
    override fun set(applicationLanguage: ApplicationLanguage) {
        val appLocale = applicationLanguage.locale?.run {
            forLanguageTags(this.toLanguageTag())
        } ?: getEmptyLocaleList()

        setApplicationLocales(appLocale)
    }

    /**
     * Retrieves the current application language based on the configured locales.
     *
     * @return The current application language.
     */
    override fun get(): ApplicationLanguage =
        ApplicationLanguage.values().firstOrNull {
            it.locale?.toLanguageTag() == getApplicationLocales().toLanguageTags()
        } ?: ApplicationLanguage.SYSTEM
}
