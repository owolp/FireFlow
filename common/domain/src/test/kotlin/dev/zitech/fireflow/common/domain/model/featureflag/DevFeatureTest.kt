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

package dev.zitech.fireflow.common.domain.model.featureflag

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DevFeatureTest {

    @Test
    fun `DevFeature properties should have correct values`() {
        val leakCanary: Feature = DevFeature.LEAK_CANARY
        val strictMode: Feature = DevFeature.STRICT_MODE

        assertThat(leakCanary.key).isEqualTo("dev-feature.leak-canary")
        assertThat(leakCanary.title).isEqualTo("Leak Canary")
        assertThat(leakCanary.explanation).isEqualTo("A memory leak detection library for Android")
        assertThat(leakCanary.defaultValue).isFalse()

        assertThat(strictMode.key).isEqualTo("dev-feature.strict-mode")
        assertThat(strictMode.title).isEqualTo("Strict mode")
        assertThat(strictMode.explanation).isEqualTo("А developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.")
        assertThat(strictMode.defaultValue).isFalse()
    }
}
