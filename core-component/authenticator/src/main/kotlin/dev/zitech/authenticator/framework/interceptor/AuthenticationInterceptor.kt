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

package dev.zitech.authenticator.framework.interceptor

import dev.zitech.authenticator.framework.HEADER_ACCEPT_KEY
import dev.zitech.authenticator.framework.HEADER_ACCEPT_VALUE
import dev.zitech.authenticator.framework.HEADER_AUTHORIZATION_KEY
import dev.zitech.authenticator.framework.HEADER_AUTHORIZATION_VALUE
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountAccessTokenUseCase
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthenticationInterceptor @Inject constructor(
    private val getCurrentUserAccountAccessTokenUseCase: GetCurrentUserAccountAccessTokenUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        chain.proceed(
            chain.request().newBuilder()
                .header(
                    HEADER_AUTHORIZATION_KEY,
                    "$HEADER_AUTHORIZATION_VALUE ${getCurrentUserAccountAccessTokenUseCase()}"
                )
                .header(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE)
                .build()
        )
    }
}
