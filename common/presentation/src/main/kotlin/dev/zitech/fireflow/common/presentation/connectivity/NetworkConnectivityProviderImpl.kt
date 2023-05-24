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

package dev.zitech.fireflow.common.presentation.connectivity

import android.net.TrafficStats
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.User.Remote.UrlPortFormat
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

/**
 * Implementation of [NetworkConnectivityProvider] interface for managing network connectivity state.
 *
 * @param appDispatchers The [AppDispatchers] instance for specifying the dispatchers for coroutines.
 * @param getCurrentUserUseCase The use case for retrieving the current user.
 */
internal class NetworkConnectivityProviderImpl @Inject constructor(
    appDispatchers: AppDispatchers,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : NetworkConnectivityProvider {

    private val tag = Logger.tag(this::class.java)

    /**
     * Flow representing the network connectivity state.
     */
    override val networkState: Flow<NetworkState> = callbackFlow {
        getCurrentUserUseCase().onEach { userResult ->
            when (userResult) {
                is OperationResult.Failure -> {
                    // NO_OP
                }
                is OperationResult.Success -> {
                    when (val user = userResult.data) {
                        is User.Local -> UrlPortFormat.Error(Error.LocalUserTypeNotSupported)
                        is User.Remote -> {
                            val urlPortFormat = user.extractUrlAndPort()
                            if (urlPortFormat is UrlPortFormat.Valid) {
                                while (true) {
                                    delay(PERIODIC_CHECK_DELAY)
                                    val socket = Socket()
                                    if (isNetworkConnectionAvailable(socket, urlPortFormat)) {
                                        trySend(NetworkState.Connected)
                                    } else {
                                        trySend(NetworkState.Disconnected)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.flowOn(appDispatchers.io).collect()

        awaitClose()
    }.distinctUntilChanged()
        .flowOn(appDispatchers.io)
        .catch { exception ->
            Logger.e(tag, exception)
        }

    /**
     * Connects the socket to the specified IP address and port.
     *
     * @param socket The socket for establishing a connection.
     * @param hostname The hostname or IP address of the server.
     * @param port The port number.
     */
    private fun connectToInetSocketAddress(socket: Socket, hostname: String, port: Int) {
        with(socket) {
            connect(InetSocketAddress(hostname, port), SOCKET_TIMEOUT)
            close()
        }
    }

    /**
     * Checks if a network connection is available.
     *
     * @param socket The socket for establishing a connection.
     * @param urlPortFormat The URL and port format information for the network connection.
     *
     * @return `true` if a network connection is available, `false` otherwise.
     */
    @Suppress("SwallowedException")
    private fun isNetworkConnectionAvailable(
        socket: Socket,
        urlPortFormat: UrlPortFormat.Valid
    ): Boolean = try {
        setThreadStatsTag()
        connectToInetSocketAddress(socket, urlPortFormat.hostname, urlPortFormat.port)
        true
    } catch (ioException: IOException) {
        false
    }

    /**
     * Sets the thread's stats tag for network connection availability.
     *
     * https://stackoverflow.com/q/47723973
     */
    private fun setThreadStatsTag() {
        TrafficStats.setThreadStatsTag(NETWORK_CONNECTION_AVAILABLE_THREAD_ID)
    }

    private companion object {
        const val NETWORK_CONNECTION_AVAILABLE_THREAD_ID = 10000
        const val SOCKET_TIMEOUT = 1500
        const val PERIODIC_CHECK_DELAY = 10000L
    }
}
