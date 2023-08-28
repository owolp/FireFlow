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

package dev.zitech.fireflow.settings.presentation.settings.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.common.domain.model.application.ApplicationLanguage
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateUserUseCase
import dev.zitech.fireflow.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.common.presentation.navigation.ScreenDestinationProvider
import dev.zitech.fireflow.common.presentation.navigation.state.LogInState
import dev.zitech.fireflow.common.presentation.navigation.state.LoginCheckCompletedHandler
import dev.zitech.fireflow.common.presentation.navigation.state.logInState
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildFlavor
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import dev.zitech.fireflow.core.result.getResultOrDefault
import dev.zitech.fireflow.core.result.getResultOrNull
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import dev.zitech.fireflow.settings.domain.usecase.LogOutCurrentUserUseCase
import dev.zitech.fireflow.settings.domain.usecase.application.CleanApplicationUseCase
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.collection.AppearanceCollectionStates
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.collection.DataChoicesCollectionStates
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    screenDestinationProvider: ScreenDestinationProvider,
    private val appConfigProvider: AppConfigProvider,
    private val appearanceCollectionStates: AppearanceCollectionStates,
    private val cleanApplicationUseCase: CleanApplicationUseCase,
    private val dataChoicesCollectionStates: DataChoicesCollectionStates,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutCurrentUserUseCase: LogOutCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : MviViewModel<SettingsIntent, SettingsState>(SettingsState()), DeepLinkViewModel {

    private val tag = Logger.tag(this::class.java)

    override val logInState: StateFlow<LogInState> by logInState(
        screenDestinationProvider,
        loginCheckCompletedHandler,
        viewModelScope
    )

    init {
        logInState.onEach { logInState ->
            updateState {
                copy(
                    deepLinkScreenDestination = when (logInState) {
                        LogInState.InitScreen,
                        LogInState.Logged -> null
                        is LogInState.NotLogged -> logInState.destination
                    }
                )
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
                setPreferencesStateDefault()
            } else {
                setPreferencesStateLimited()
            }

            setNetworkConnectivityPreference()
        }
    }

    override fun receiveIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is AnalyticsChecked -> handleAnalyticsChecked(intent.checked)
                AnalyticsErrorHandled -> updateState { copy(analyticsError = false) }
                ConfirmLogOutClicked -> handleConfirmLogOutClicked()
                ConfirmLogOutDismissed -> updateState { copy(confirmLogOut = false) }
                ConfirmDeleteAllDataClicked -> handleDeleteAllDataClicked()
                ConfirmDeleteAllDataDismissed -> updateState { copy(confirmDeleteAll = false) }
                is ConnectivityChecked -> handleConnectivityChecked(intent.checked)
                is CrashReporterChecked -> handleCrashReporterChecked(intent.checked)
                CrashReporterErrorHandled -> updateState {
                    copy(crashReporterError = false)
                }
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                LanguageDismissed -> updateState {
                    copy(selectApplicationLanguage = null)
                }
                LanguagePreferenceClicked -> updateState {
                    copy(selectApplicationLanguage = this.applicationLanguage)
                }
                is LanguageSelected -> handleLanguageSelected(intent.id)
                LogOutClicked -> updateState { copy(confirmLogOut = true) }
                DeleteAllDataClicked -> updateState { copy(confirmDeleteAll = true) }
                is RestartApplicationClicked -> intent.restart()
                is ThemeSelected -> handleThemeSelected(intent.id)
                is PerformanceChecked -> handlePerformanceChecked(intent.checked)
                PerformanceErrorHandled -> updateState {
                    copy(performanceError = false)
                }
                is PersonalizedAdsChecked -> handlePersonalizedAdsChecked(
                    intent.checked
                )
                PersonalizedAdsErrorHandled -> updateState {
                    copy(personalizedAdsError = false)
                }
                ThemeDismissed -> updateState {
                    copy(selectApplicationTheme = null)
                }
                ThemePreferenceClicked -> updateState {
                    copy(selectApplicationTheme = this.applicationTheme)
                }
            }
        }
    }

    private suspend fun getCurrentUser() = getCurrentUserUseCase().first()

    private suspend fun getCurrentUserIdentifier() =
        when (val result = getCurrentUser()) {
            is Success -> result.data.retrieveIdentifier()
            is Failure -> ""
        }

    private suspend fun handleAnalyticsChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            dataChoicesCollectionStates.setAnalyticsCollection(checked)
            val isEnabled =
                dataChoicesCollectionStates.getAnalyticsCollectionValue().getResultOrNull()
            if (checked == isEnabled) {
                dataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
                updateState {
                    copy(
                        analytics = checked,
                        personalizedAds = checked,
                        performance = checked
                    )
                }
                dataChoicesCollectionStates.setPerformanceCollection(checked)
            } else {
                updateState { copy(analyticsError = true) }
            }
        } else {
            Logger.e(tag, "Setting analytics on FOSS build is not supported")
        }
    }

    private suspend fun handleConfirmLogOutClicked() {
        updateState { copy(confirmLogOut = false) }
        logOutCurrentUserUseCase().onFailure { error ->
            when (error) {
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private suspend fun handleConnectivityChecked(checked: Boolean) {
        getCurrentUser().onSuccess { user ->
            when (user) {
                is User.Local -> {
                    updateState { copy(fatalError = Error.LocalUserTypeNotSupported) }
                }
                is User.Remote -> {
                    updateUserUseCase(user.copy(connectivityNotification = checked))
                    updateState { copy(connectivity = checked) }
                }
            }
        }
    }

    private suspend fun handleCrashReporterChecked(checked: Boolean) {
        dataChoicesCollectionStates.setCrashReporterCollection(checked)
        val isEnabled =
            dataChoicesCollectionStates.getCrashReporterCollectionValue().getResultOrNull()
        if (checked == isEnabled) {
            updateState { copy(crashReporter = checked) }
        } else {
            updateState { copy(crashReporterError = true) }
        }
    }

    private suspend fun handleDeleteAllDataClicked() {
        updateState { copy(confirmDeleteAll = false) }
        cleanApplicationUseCase().onFailure { error ->
            when (error) {
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private fun handleLanguageSelected(id: Int) {
        updateState { copy(selectApplicationLanguage = null) }
        ApplicationLanguage.getApplicationLanguage(id).run {
            appearanceCollectionStates.setApplicationLanguageValue(this)
            updateState { copy(applicationLanguage = this@run) }
        }
    }

    private suspend fun handlePerformanceChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            dataChoicesCollectionStates.setPerformanceCollection(checked)
            val isEnabled =
                dataChoicesCollectionStates.getPerformanceCollectionValue().getResultOrNull()
            if (checked == isEnabled) {
                updateState { copy(performance = checked) }
            } else {
                updateState { copy(performanceError = true) }
            }
        } else {
            Logger.e(tag, "Setting performance on FOSS build is not supported")
        }
    }

    private suspend fun handlePersonalizedAdsChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            dataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            val isEnabled =
                dataChoicesCollectionStates.getAllowPersonalizedAdsValue().getResultOrNull()
            if (checked == isEnabled) {
                updateState { copy(personalizedAds = checked) }
            } else {
                updateState { copy(personalizedAdsError = true) }
            }
        } else {
            Logger.e(tag, "Setting personalized ads on FOSS build is not supported")
        }
    }

    private suspend fun handleThemeSelected(id: Int) {
        updateState { copy(selectApplicationTheme = null) }
        ApplicationTheme.getApplicationTheme(id).run {
            appearanceCollectionStates.setApplicationThemeValue(this)
            updateState { copy(applicationTheme = this@run) }
        }
    }

    private suspend fun setNetworkConnectivityPreference() {
        getCurrentUser().onSuccess { user ->
            if (user is User.Remote) {
                updateState {
                    copy(
                        connectivity = user.connectivityNotification
                    )
                }
            }
        }
    }

    private suspend fun setPreferencesStateDefault() {
        updateState {
            copy(
                analytics = dataChoicesCollectionStates
                    .getAnalyticsCollectionValue().getResultOrNull(),
                applicationLanguage = appearanceCollectionStates
                    .getApplicationLanguageValue(),
                applicationTheme = appearanceCollectionStates
                    .getApplicationThemeValue()
                    .getResultOrDefault(state.value.applicationTheme),
                crashReporter = dataChoicesCollectionStates
                    .getCrashReporterCollectionValue()
                    .getResultOrDefault(state.value.crashReporter),
                identifier = getCurrentUserIdentifier(),
                performance = dataChoicesCollectionStates
                    .getPerformanceCollectionValue()
                    .getResultOrNull(),
                personalizedAds = dataChoicesCollectionStates
                    .getAllowPersonalizedAdsValue().getResultOrNull(),
                version = appConfigProvider.version
            )
        }
    }

    private suspend fun setPreferencesStateLimited() {
        updateState {
            copy(
                identifier = getCurrentUserIdentifier()
            )
        }
    }
}
