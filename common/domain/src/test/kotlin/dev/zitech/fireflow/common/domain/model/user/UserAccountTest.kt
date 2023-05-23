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

internal class UserAccountTest {

    @Test
    fun `extractUrlAndPort with valid IP address and port`() {
        val userAccount = getUserAccount("192.168.1.1:8080")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("192.168.1.1")
        assertThat(result.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort with valid URL including protocol and port`() {
        val userAccount = getUserAccount("http://192.168.1.1:8080")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("192.168.1.1")
        assertThat(result.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort with valid URL including secured protocol and port`() {
        val userAccount = getUserAccount("https://192.168.1.1:8080")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("192.168.1.1")
        assertThat(result.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort with valid domain name and port`() {
        val userAccount = getUserAccount("example.com:8000")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("example.com")
        assertThat(result.port).isEqualTo(8000)
    }

    @Test
    fun `extractUrlAndPort with valid URL including protocol, www, and port`() {
        val userAccount = getUserAccount("http://www.example.com:8080")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("example.com")
        assertThat(result.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort with valid URL including secured protocol, www, and port`() {
        val userAccount = getUserAccount("https://www.example.com:8080")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("example.com")
        assertThat(result.port).isEqualTo(8080)
    }

    @Test
    fun `extractUrlAndPort with valid IP address and default port`() {
        val userAccount = getUserAccount("192.168.1.1")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("192.168.1.1")
        assertThat(result.port).isEqualTo(80)
    }

    @Test
    fun `extractUrlAndPort with valid domain name and default port`() {
        val userAccount = getUserAccount("example.com")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
        assertThat((result as User.UrlPortFormat.Valid).url).isEqualTo("example.com")
        assertThat(result.port).isEqualTo(80)
    }

    @Test
    fun `extractUrlAndPort with valid input`() {
        val userAccount = getUserAccount("valid-url")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Valid::class.java)
    }

    @Test
    fun `extractUrlAndPort with empty input`() {
        val userAccount = getUserAccount("")

        val result = userAccount.extractUrlAndPort()

        assertThat(result).isInstanceOf(User.UrlPortFormat.Invalid::class.java)
    }

    private fun getUserAccount(serverAddress: String): User =
        User(serverAddress = serverAddress, isCurrentUserAccount = true, userId = 1)
}
