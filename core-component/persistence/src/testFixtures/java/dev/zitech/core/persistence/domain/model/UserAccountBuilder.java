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

    private String accessToken;
    private String clientId = "123";
    private String clientSecret = "456";
    private Boolean isCurrentUserAccount = false;
    private String oauthCode;
    private String refreshToken;
    private String serverAddress = "http://localhost";
    private String state;
    private Long userId = 0L;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public UserAccountBuilder setCurrentUserAccount(Boolean currentUserAccount) {
        isCurrentUserAccount = currentUserAccount;
        return this;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserAccountBuilder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserAccount build() {
        return new UserAccount(
                accessToken,
                clientId,
                clientSecret,
                isCurrentUserAccount,
                oauthCode,
                refreshToken,
                serverAddress,
                state,
                userId
        );
    }
}
