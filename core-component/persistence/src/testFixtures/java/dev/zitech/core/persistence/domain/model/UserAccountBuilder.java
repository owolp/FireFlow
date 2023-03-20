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

package dev.zitech.core.persistence.domain.model;

import dev.zitech.core.persistence.domain.model.database.UserAccount;

public class UserAccountBuilder {

    private UserAccount.AuthenticationType authenticationType;
    private String email;
    private String fireflyId;
    private Boolean isCurrentUserAccount = false;
    private String role;
    private String serverAddress = "http://localhost";
    private String state;
    private String type;
    private Long userId = 0L;

    public UserAccountBuilder setAuthenticationType(UserAccount.AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }

    public UserAccountBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserAccountBuilder setFireflyId(String fireflyId) {
        this.fireflyId = fireflyId;
        return this;
    }

    public UserAccountBuilder setCurrentUserAccount(Boolean currentUserAccount) {
        isCurrentUserAccount = currentUserAccount;
        return this;
    }

    public UserAccountBuilder setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public UserAccountBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserAccountBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public UserAccountBuilder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserAccount build() {
        return new UserAccount(
                authenticationType,
                email,
                fireflyId,
                isCurrentUserAccount,
                role,
                serverAddress,
                state,
                type,
                userId
        );
    }
}
