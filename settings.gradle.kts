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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core")
include(":common:data")
include(":common:domain")
include(":common:presentation")
include(":common:presentation-framework")

include(":core-component:core")
include(":core-component:authenticator")
include(":core-component:common")
include(":core-component:feature-flag")
include(":core-component:navigation")
include(":core-component:network")
include(":core-component:persistence")
include(":core-component:remote-config")
include(":core-component:reporter")
include(":design-system")
include(":feature-component:feature")
include(":feature-component:authentication")
include(":feature-component:dashboard")
include(":feature-component:onboarding")
include(":feature-component:settings")
