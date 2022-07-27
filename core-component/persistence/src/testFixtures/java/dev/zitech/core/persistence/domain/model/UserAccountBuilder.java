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

package dev.zitech.core.persistence.domain.model;

import static dev.zitech.core.common.domain.model.ApplicationTheme.SYSTEM;

import dev.zitech.core.common.domain.model.ApplicationTheme;
import dev.zitech.core.persistence.domain.model.database.UserAccount;

public class UserAccountBuilder {

    private Long id = 0L;
    private Boolean isCurrentUserAccount = false;
    private ApplicationTheme theme = SYSTEM;


    public UserAccountBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserAccountBuilder setCurrentUserAccount(Boolean currentUserAccount) {
        isCurrentUserAccount = currentUserAccount;
        return this;
    }

    public UserAccountBuilder setTheme(ApplicationTheme theme) {
        this.theme = theme;
        return this;
    }

    public UserAccount build() {
        return new UserAccount(
                id,
                isCurrentUserAccount,
                theme
        );
    }
}
