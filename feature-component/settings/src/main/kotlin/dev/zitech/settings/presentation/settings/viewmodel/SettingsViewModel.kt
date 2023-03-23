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
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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
    private val updateUserAccountUseCase: UpdateUserAccountUseCase,
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState>, DeepLinkViewModel {

    private val mutableState = MutableStateFlow(SettingsState())
    private val tag = Logger.tag(this::class.java)

    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        loginCheckCompletedHandler,
        viewModelScope
    )

    override val state: StateFlow<SettingsState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
                setPreferencesStateDefault()
            } else {
                setPreferencesStateLimited()
            }

            mutableState.update {
                it.copy(
                    viewState = SettingsState.ViewState.Success
                )
            }
        }
    }

    override fun receiveIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is AnalyticsChecked -> handleAnalyticsChecked(intent.checked)
                AnalyticsErrorHandled -> mutableState.update { it.copy(analyticsError = false) }
                ConfirmLogOutClicked -> handleConfirmLogOutClicked()
                ConfirmLogOutDismissed -> mutableState.update { it.copy(confirmLogOut = false) }
                is CrashReporterChecked -> handleCrashReporterChecked(intent.checked)
                CrashReporterErrorHandled -> mutableState.update {
                    it.copy(crashReporterError = false)
                }
                LanguageDismissed -> mutableState.update {
                    it.copy(selectApplicationLanguage = null)
                }
                LanguagePreferenceClicked -> mutableState.update {
                    it.copy(selectApplicationLanguage = it.applicationLanguage)
                }
                is LanguageSelected -> handleLanguageSelected(intent.id)
                LogOutClicked -> mutableState.update { it.copy(confirmLogOut = true) }
                is RestartApplicationClicked -> intent.restart()
                is ThemeSelected -> handleThemeSelected(intent.id)
                is PerformanceChecked -> handlePerformanceChecked(intent.checked)
                PerformanceErrorHandled -> mutableState.update { it.copy(performanceError = false) }
                is PersonalizedAdsChecked -> handlePersonalizedAdsChecked(
                    intent.checked
                )
                PersonalizedAdsErrorHandled -> mutableState.update {
                    it.copy(personalizedAdsError = false)
                }
                ThemeDismissed -> mutableState.update {
                    it.copy(selectApplicationTheme = null)
                }
                ThemePreferenceClicked -> mutableState.update {
                    it.copy(selectApplicationTheme = it.applicationTheme)
                }
            }
        }
    }

    private suspend fun getCurrentUserEmailAddress() =
        when (val result = getCurrentUserAccountUseCase().first()) {
            is WorkSuccess -> result.data.email.orEmpty()
            is WorkError -> ""
        }

    private suspend fun handleAnalyticsChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAnalyticsCollectionValue()
            if (checked == isEnabled) {
                settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
                mutableState.update {
                    it.copy(
                        analytics = checked,
                        personalizedAds = checked,
                        performance = checked
                    )
                }
                settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
            } else {
                mutableState.update { it.copy(analyticsError = true) }
            }
        } else {
            Logger.e(tag, "Setting analytics on FOSS build is not supported")
        }
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleConfirmLogOutClicked() {
        mutableState.update { it.copy(confirmLogOut = false) }
        // TODO: Show loading
        getCurrentUserAccountUseCase().first().onSuccess { userAccount ->
            updateUserAccountUseCase(userAccount.copy(isCurrentUserAccount = false))
        }
    }

    private suspend fun handleCrashReporterChecked(checked: Boolean) {
        settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        val isEnabled = settingsDataChoicesCollectionStates.getCrashReporterCollectionValue()
        if (checked == isEnabled) {
            mutableState.update { it.copy(crashReporter = checked) }
        } else {
            mutableState.update { it.copy(crashReporterError = true) }
        }
    }

    private fun handleLanguageSelected(id: Int) {
        mutableState.update { it.copy(selectApplicationLanguage = null) }
        ApplicationLanguage.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationLanguageValue(this)
            mutableState.update { it.copy(applicationLanguage = this) }
        }
    }

    private suspend fun handlePerformanceChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getPerformanceCollectionValue()
            if (checked == isEnabled) {
                mutableState.update { it.copy(performance = checked) }
            } else {
                mutableState.update { it.copy(performanceError = true) }
            }
        } else {
            Logger.e(tag, "Setting performance on FOSS build is not supported")
        }
    }

    private suspend fun handlePersonalizedAdsChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue()
            if (checked == isEnabled) {
                mutableState.update { it.copy(personalizedAds = checked) }
            } else {
                mutableState.update { it.copy(personalizedAdsError = true) }
            }
        } else {
            Logger.e(tag, "Setting personalized ads on FOSS build is not supported")
        }
    }

    private suspend fun handleThemeSelected(id: Int) {
        mutableState.update { it.copy(selectApplicationTheme = null) }
        ApplicationTheme.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationThemeValue(this)
            mutableState.update { it.copy(applicationTheme = this) }
        }
    }

    private suspend fun setPreferencesStateDefault() {
        mutableState.update {
            it.copy(
                analytics = settingsDataChoicesCollectionStates
                    .getAnalyticsCollectionValue(),
                applicationLanguage = settingsAppearanceCollectionStates
                    .getApplicationLanguageValue(),
                applicationTheme = settingsAppearanceCollectionStates
                    .getApplicationThemeValue(),
                crashReporter = settingsDataChoicesCollectionStates
                    .getCrashReporterCollectionValue(),
                email = getCurrentUserEmailAddress(),
                performance = settingsDataChoicesCollectionStates
                    .getPerformanceCollectionValue(),
                personalizedAds = settingsDataChoicesCollectionStates
                    .getAllowPersonalizedAdsValue(),
                version = appConfigProvider.version,
                viewState = SettingsState.ViewState.Success
            )
        }
    }

    private suspend fun setPreferencesStateLimited() {
        mutableState.update {
            it.copy(
                email = getCurrentUserEmailAddress()
            )
        }
    }

}
