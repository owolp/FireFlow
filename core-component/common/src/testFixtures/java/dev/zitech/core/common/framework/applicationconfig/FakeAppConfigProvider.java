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

package dev.zitech.core.common.framework.applicationconfig;

import org.jetbrains.annotations.NotNull;

import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider;
import dev.zitech.core.common.domain.model.BuildFlavor;
import dev.zitech.core.common.domain.model.BuildMode;

public class FakeAppConfigProvider implements AppConfigProvider {

    BuildMode buildMode = BuildMode.RELEASE;
    BuildFlavor buildFlavor = BuildFlavor.DEV;

    @NotNull
    @Override
    public BuildMode getBuildMode() {
        return buildMode;
    }

    @NotNull
    @Override
    public BuildFlavor getBuildFlavor() {
        return buildFlavor;
    }

    public void setBuildMode(BuildMode buildMode) {
        this.buildMode = buildMode;
    }

    public void setBuildFlavor(BuildFlavor buildFlavor) {
        this.buildFlavor = buildFlavor;
    }
}
