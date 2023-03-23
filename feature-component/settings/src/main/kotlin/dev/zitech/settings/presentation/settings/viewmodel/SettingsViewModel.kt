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

package dev.zitech.settings.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.model.onSuccess
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.LoginCheckCompletedHandler
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.presentation.extension.logInState
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsDataChoicesCollectionStates
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    getScreenDestinationUseCase: GetScreenDestinationUseCase,
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    private val appConfigProvider: AppConfigProvider,
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val settingsAppearanceCollectionStates: SettingsAppearanceCollectionStates,
    private val settingsDataChoicesCollectionStates: SettingsDataChoicesCollectionStates,
    private val stateHandler: SettingsStateHandler,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState>, DeepLinkViewModel {

    private val tag = Logger.tag(this::class.java)

    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        loginCheckCompletedHandler,
        viewModelScope
    )

    override val screenState: StateFlow<SettingsState> = stateHandler.state

    init {
        getPreferencesState()
    }

    override fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnCrashReporterCheckChange -> handleOnCrashReporterCheckChange(intent.checked)
                is OnAnalyticsCheckChange -> handleOnAnalyticsCheckChange(intent.checked)
                is OnPersonalizedAdsCheckChange -> handleOnPersonalizedAdsCheckChange(
                    intent.checked
                )
                is OnPerformanceCheckChange -> handleOnPerformanceCheckChange(intent.checked)
                is OnThemeSelect -> handleOnThemeSelect(intent.id)
                is OnLanguageSelect -> handleOnLanguageSelect(intent.id)
                OnThemePreferenceClick -> stateHandler.setSelectApplicationTheme(
                    stateHandler.state.value.applicationTheme
                )
                OnLanguagePreferenceClick -> stateHandler.setSelectApplicationLanguage(
                    stateHandler.state.value.applicationLanguage
                )
                OnThemeDismiss -> stateHandler.setSelectApplicationTheme(null)
                OnLanguageDismiss -> stateHandler.setSelectApplicationLanguage(null)
                OnConfirmLogOutDismiss -> stateHandler.setConfirmLogOut(false)
                is OnRestartApplication -> intent.restart()
                OnLogOutClick -> stateHandler.setConfirmLogOut(true)
                OnConfirmLogOutClick -> handleOnConfirmLogOutClick()
                AnalyticsErrorHandled -> stateHandler.setAnalyticsError(false)
                CrashReporterErrorHandled -> stateHandler.setCrashReporterError(false)
                PerformanceErrorHandled -> stateHandler.setPerformanceError(false)
                PersonalizedAdsErrorHandled -> stateHandler.setPersonalizedAdsError(false)
            }
        }
    }

    private fun getPreferencesState() = viewModelScope.launch {
        stateHandler.run {
            setAnalyticsState(
                settingsDataChoicesCollectionStates.getAnalyticsCollectionValue(),
                appConfigProvider.buildFlavor
            )
            setPersonalizedAds(
                settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue(),
                appConfigProvider.buildFlavor
            )
            setPerformance(
                settingsDataChoicesCollectionStates.getPerformanceCollectionValue(),
                appConfigProvider.buildFlavor
            )
            setCrashReporter(
                settingsDataChoicesCollectionStates.getCrashReporterCollectionValue()
            )
            setApplicationThemeState(settingsAppearanceCollectionStates.getApplicationThemeValue())
            setApplicationLanguage(settingsAppearanceCollectionStates.getApplicationLanguageValue())
            setAppVersionState(appConfigProvider.version)
            setViewState(SettingsState.ViewState.Success)

            getCurrentUserAccountUseCase()
                .onEach { it.onSuccess { userAccount -> setEmail(userAccount.email.orEmpty()) } }
                .launchIn(viewModelScope)
        }
    }

    private suspend fun handleOnAnalyticsCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAnalyticsCollectionValue()
            if (checked == isEnabled) {
                stateHandler.setAnalyticsState(checked, appConfigProvider.buildFlavor)
                settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
                stateHandler.setPersonalizedAds(checked, appConfigProvider.buildFlavor)
                settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
                stateHandler.setPerformance(checked, appConfigProvider.buildFlavor)
            } else {
                stateHandler.setAnalyticsError(true)
            }
        } else {
            Logger.e(tag, "Setting analytics on FOSS build is not supported")
        }
    }

    private suspend fun handleOnConfirmLogOutClick() {
        stateHandler.setConfirmLogOut(false)
        // TODO: Show loading
        stateHandler
        getCurrentUserAccountUseCase().first().onSuccess { userAccount ->
            updateUserAccountUseCase(userAccount.copy(isCurrentUserAccount = false))
        }
    }

    private suspend fun handleOnCrashReporterCheckChange(checked: Boolean) {
        settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        val isEnabled = settingsDataChoicesCollectionStates.getCrashReporterCollectionValue()
        if (checked == isEnabled) {
            stateHandler.setCrashReporter(checked)
        } else {
            stateHandler.setCrashReporterError(true)
        }
    }

    private fun handleOnLanguageSelect(id: Int) {
        stateHandler.setSelectApplicationLanguage(null)
        ApplicationLanguage.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationLanguageValue(this)
            stateHandler.setApplicationLanguage(this)
        }
    }

    private suspend fun handleOnPerformanceCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getPerformanceCollectionValue()
            if (checked == isEnabled) {
                stateHandler.setPerformance(checked, appConfigProvider.buildFlavor)
            } else {
                stateHandler.setPerformanceError(true)
            }
        } else {
            Logger.e(tag, "Setting performance on FOSS build is not supported")
        }
    }

    private suspend fun handleOnPersonalizedAdsCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue()
            if (checked == isEnabled) {
                stateHandler.setPersonalizedAds(checked, appConfigProvider.buildFlavor)
            } else {
                stateHandler.setPersonalizedAdsError(true)
            }
        } else {
            Logger.e(tag, "Setting personalized ads on FOSS build is not supported")
        }
    }

    private suspend fun handleOnThemeSelect(id: Int) {
        stateHandler.setSelectApplicationTheme(null)
        ApplicationTheme.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationThemeValue(this)
            stateHandler.setApplicationThemeState(this)
        }
    }
}
