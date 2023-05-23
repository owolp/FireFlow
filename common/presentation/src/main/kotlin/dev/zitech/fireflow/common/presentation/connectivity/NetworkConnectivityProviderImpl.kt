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

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.TrafficStats
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.User.Remote.UrlPortFormat
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.scope.AppScopes
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.Collections
import javax.inject.Inject
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn

internal class NetworkConnectivityProviderImpl @Inject constructor(
    context: Context,
    private val appDispatchers: AppDispatchers,
    private val appScopes: AppScopes,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : NetworkConnectivityProvider {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val tag = Logger.tag(this::class.java)

    @Suppress("BlockingMethodInNonBlockingContext")
    override val networkState: Flow<NetworkState> = callbackFlow {
        val activeNetworks = Collections.synchronizedSet(HashSet<Network>())

        checkNetworkConnectionOnCollectionStart()

        val connectivityCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                appScopes.singletonLaunch(appDispatchers.io) {
                    Logger.i(tag, "onAvailable $network")

                    val urlPortFormat = getUrlPortFormat()
                    if (urlPortFormat is UrlPortFormat.Valid) {
                        val hasInternetCapability =
                            hasInternetCapability(connectivityManager, network)
                        val socket = network.socketFactory.createSocket() ?: Socket()
                        val isNetworkConnectionAvailable =
                            isNetworkConnectionAvailable(socket, urlPortFormat)

                        if (hasInternetCapability && isNetworkConnectionAvailable) {
                            activeNetworks.add(network)
                        }

                        Logger.i(
                            tag,
                            "onAvailable network, activeNetworks.size=${activeNetworks.size}"
                        )
                        if (activeNetworks.isNotEmpty()) {
                            trySend(NetworkState.Connected)
                        } else {
                            trySend(NetworkState.Disconnected)
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                appScopes.singletonLaunch(appDispatchers.io) {
                    Logger.i(tag, "onLost $network")

                    val urlPortFormat = getUrlPortFormat()
                    if (urlPortFormat is UrlPortFormat.Valid) {
                        activeNetworks.remove(network)
                        activeNetworks.forEach { network ->
                            val hasInternetCapability =
                                hasInternetCapability(connectivityManager, network)
                            if (hasInternetCapability) {
                                val socket = network.socketFactory.createSocket() ?: Socket()
                                val isNetworkConnectionAvailable =
                                    isNetworkConnectionAvailable(socket, urlPortFormat)
                                if (!isNetworkConnectionAvailable) {
                                    activeNetworks.remove(network)
                                }
                            }
                        }

                        Logger.i(tag, "onLost network, activeNetworks.size=${activeNetworks.size}")
                        if (activeNetworks.isNotEmpty()) {
                            trySend(NetworkState.Connected)
                        } else {
                            trySend(NetworkState.Disconnected)
                        }
                    }
                }
            }
        }

        Logger.d(tag, "registerNetworkCallback")
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)

        awaitClose {
            Logger.d(tag, "unregisterNetworkCallback")
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }
        .distinctUntilChanged()
        .flowOn(appDispatchers.io)
        .catch { exception -> Logger.e(tag, exception) }

    private fun ProducerScope<NetworkState>.checkNetworkConnectionOnCollectionStart() {
        appScopes.singletonLaunch(appDispatchers.io) {
            val urlPortFormat = getUrlPortFormat()
            if (urlPortFormat is UrlPortFormat.Valid) {
                val socket = Socket()
                val isNetworkConnectionAvailable =
                    isNetworkConnectionAvailable(socket, urlPortFormat)
                if (isNetworkConnectionAvailable) {
                    trySend(NetworkState.Connected)
                } else {
                    trySend(NetworkState.Disconnected)
                }
            }
        }
    }

    private fun connectToInetSocketAddress(socket: Socket, hostname: String, port: Int) {
        with(socket) {
            connect(InetSocketAddress(hostname, port), TIMEOUT)
            close()
        }
        Logger.i(tag, "Network connection available")
    }

    private suspend fun getUrlPortFormat(): UrlPortFormat =
        when (val result = getCurrentUserUseCase().first()) {
            is OperationResult.Failure -> UrlPortFormat.Error(result.error)
            is OperationResult.Success -> {
                when (val user = result.data) {
                    is User.Local -> UrlPortFormat.Error(Error.LocalUserTypeNotSupported)
                    is User.Remote -> user.extractUrlAndPort()
                }
            }
        }

    private fun hasInternetCapability(
        connectivityManager: ConnectivityManager,
        network: Network
    ): Boolean = connectivityManager
        .getNetworkCapabilities(network)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

    private fun isNetworkConnectionAvailable(
        socket: Socket,
        urlPortFormat: UrlPortFormat.Valid
    ): Boolean = try {
        Logger.i(tag, "Checking if connection is available")
        setThreadStatsTag()
        connectToInetSocketAddress(socket, urlPortFormat.hostname, urlPortFormat.port)
        true
    } catch (ioException: IOException) {
        Logger.i(tag, "Connection not available")
        false
    }

    /**
     * https://stackoverflow.com/q/47723973
     */
    private fun setThreadStatsTag() {
        TrafficStats.setThreadStatsTag(NETWORK_CONNECTION_AVAILABLE_THREAD_ID)
    }

    private companion object {
        const val NETWORK_CONNECTION_AVAILABLE_THREAD_ID = 10000
        const val TIMEOUT = 1500
    }
}
