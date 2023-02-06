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

package dev.zitech.core.common.framework.strings;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.zitech.core.common.DataFactory;
import dev.zitech.core.common.domain.strings.StringsProvider;

public class FakeStringsProvider implements StringsProvider {

    private final Map<Integer, String> strings = new HashMap<>();

    @NotNull
    @Override
    public String invoke(int resId) {
        return Objects.requireNonNull(strings.get(resId));
    }

    @NotNull
    @Override
    public String invoke(int resId, @NotNull String... args) {
        return Objects.requireNonNull(strings.get(resId));
    }

    @SuppressLint("NewApi")
    public void addString(int resId, @Nullable String value) {
        strings.put(resId, Objects.requireNonNullElseGet(
                value, () -> DataFactory.INSTANCE.createRandomString(null))
        );
    }
}
