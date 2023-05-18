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

package dev.zitech.fireflow.common.domain.model.configurator

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class BooleanConfigTest {

    @Test
    fun `BooleanConfig properties should have correct values`() {
        val appActive: Config<Boolean> = BooleanConfig.APP_ACTIVE
        assertThat(appActive.defaultValue).isEqualTo(true)
        assertThat(appActive.explanation).isEqualTo("If false, application won't open")
        assertThat(appActive.key).isEqualTo("app_active")
        assertThat(appActive.title).isEqualTo("Is Application Active")

        val performanceCollectionEnabled = BooleanConfig.PERFORMANCE_COLLECTION_ENABLED
        assertThat(performanceCollectionEnabled.defaultValue).isEqualTo(false)
        assertThat(performanceCollectionEnabled.explanation).isEqualTo("If true, application performance 3rd party collection tools will be enabled")
        assertThat(performanceCollectionEnabled.key).isEqualTo("performance_collection_enabled")
        assertThat(performanceCollectionEnabled.title).isEqualTo("Is Performance Collection Enabled")
    }
}
