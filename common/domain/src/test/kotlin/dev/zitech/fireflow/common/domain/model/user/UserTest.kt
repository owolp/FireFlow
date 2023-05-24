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

package dev.zitech.fireflow.common.domain.model.user

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserTest {

    @Test
    fun `local user should have correct isCurrentUser value`() {
        val localUser = User.Local(isCurrentUser = true)

        assertThat(localUser.isCurrentUser).isEqualTo(true)
    }

    @Test
    fun `remote user should have correct isCurrentUser value`() {
        val serverAddress = "https://example.com"

        val remoteUser = getRemoteUser(serverAddress)

        assertThat(remoteUser.isCurrentUser).isTrue()
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for secured protocol, domain and port`() {
        val serverAddress = "https://example.com:8080"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for secured protocol, www, domain and port`() {
        val serverAddress = "https://www.example.com:8080"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for unsecured protocol, domain and port`() {
        val serverAddress = "http://example.com:8080"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for unsecured protocol, www, domain and port`() {
        val serverAddress = "http://www.example.com:8080"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for secured protocol, domain and default port`() {
        val serverAddress = "https://example.com"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(80)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for unsecured protocol, domain and default port`() {
        val serverAddress = "http://example.com"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
        val validUrlPortFormat = urlPortFormat as User.Remote.UrlPortFormat.Valid
        assertThat(validUrlPortFormat.hostname).isEqualTo("example.com")
        assertThat(validUrlPortFormat.port).isEqualTo(80)
    }

    @Test
    fun `extractUrlAndPort should return valid UrlPortFormat for valid server address`() {
        val serverAddress = "valid-server-address"
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Valid::class.java)
    }

    @Test
    fun `extractUrlAndPort should return invalid UrlPortFormat when server address is empty`() {
        val serverAddress = ""
        val remoteUser = getRemoteUser(serverAddress)

        val urlPortFormat = remoteUser.extractUrlAndPort()

        assertThat(urlPortFormat).isInstanceOf(User.Remote.UrlPortFormat.Invalid::class.java)
    }

    @Test
    fun `getRandomState should return a string with correct length`() {
        val randomState = User.getRandomState()

        assertThat(randomState.length).isEqualTo(10)
    }

    private fun getRemoteUser(serverAddress: String): User.Remote =
        User.Remote(isCurrentUser = true, serverAddress = serverAddress, userId = 123)
}
