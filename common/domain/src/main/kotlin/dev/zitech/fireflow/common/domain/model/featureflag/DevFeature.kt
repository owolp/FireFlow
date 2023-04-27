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

/**
 * A dev feature is something that stays in our app forever (hence it is a tool to simplify testing)
 * e.g. it is a hook into our app to allow something that a production app shouldn't allow.
 * (enable logging, test tools, etc.)
 */
enum class DevFeature(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean
) : Feature {

    LEAK_CANARY(
        key = "dev-feature.leak-canary",
        title = "Leak Canary",
        explanation = "A memory leak detection library for Android",
        defaultValue = false
    ),
    STRICT_MODE(
        "dev-feature.strict-mode",
        "Strict mode",
        "А developer tool which detects things you might be doing by accident and brings " +
            "them to your attention so you can fix them.",
        defaultValue = false
    )
}
