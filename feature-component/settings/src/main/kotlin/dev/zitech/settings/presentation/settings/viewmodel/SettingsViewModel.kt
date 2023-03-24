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

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.architecture.updateState
import dev.zitech.core.common.presentation.splash.LoginCheckCompletedHandler
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.IsCurrentUserAccount
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.presentation.extension.logInState
import dev.zitech.settings.presentation.settings.viewmodel.collection.AppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.DataChoicesCollectionStates
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    getScreenDestinationUseCase: GetScreenDestinationUseCase,
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    private val appConfigProvider: AppConfigProvider,
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val appearanceCollectionStates: AppearanceCollectionStates,
    private val dataChoicesCollectionStates: DataChoicesCollectionStates,
    private val updateCurrentUserAccountUseCase: UpdateCurrentUserAccountUseCase
) : MviViewModel<SettingsIntent, SettingsState>(SettingsState()), DeepLinkViewModel {

    private val tag = Logger.tag(this::class.java)

    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        loginCheckCompletedHandler,
        viewModelScope
    )

    init {
        viewModelScope.launch {
            if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
                setPreferencesStateDefault()
            } else {
                setPreferencesStateLimited()
            }

            updateState {
                copy(
                    viewState = SettingsState.ViewState.Success
                )
            }
        }
    }

    override fun receiveIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is AnalyticsChecked -> handleAnalyticsChecked(intent.checked)
                AnalyticsErrorHandled -> updateState { copy(analyticsError = false) }
                ConfirmLogOutClicked -> handleConfirmLogOutClicked()
                ConfirmLogOutDismissed -> updateState { copy(confirmLogOut = false) }
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

    private suspend fun getCurrentUserEmailAddress() =
        when (val result = getCurrentUserAccountUseCase().first()) {
            is WorkSuccess -> result.data.email.orEmpty()
            is WorkError -> ""
        }

    private suspend fun handleAnalyticsChecked(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            dataChoicesCollectionStates.setAnalyticsCollection(checked)
            val isEnabled = dataChoicesCollectionStates.getAnalyticsCollectionValue()
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
        updateCurrentUserAccountUseCase(
            IsCurrentUserAccount(false)
        ).onError { error ->
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

    private suspend fun handleCrashReporterChecked(checked: Boolean) {
        dataChoicesCollectionStates.setCrashReporterCollection(checked)
        val isEnabled = dataChoicesCollectionStates.getCrashReporterCollectionValue()
        if (checked == isEnabled) {
            updateState { copy(crashReporter = checked) }
        } else {
            updateState { copy(crashReporterError = true) }
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
            val isEnabled = dataChoicesCollectionStates.getPerformanceCollectionValue()
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
            val isEnabled = dataChoicesCollectionStates.getAllowPersonalizedAdsValue()
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

    private suspend fun setPreferencesStateDefault() {
        updateState {
            copy(
                analytics = dataChoicesCollectionStates
                    .getAnalyticsCollectionValue(),
                applicationLanguage = appearanceCollectionStates
                    .getApplicationLanguageValue(),
                applicationTheme = appearanceCollectionStates
                    .getApplicationThemeValue(),
                crashReporter = dataChoicesCollectionStates
                    .getCrashReporterCollectionValue(),
                email = getCurrentUserEmailAddress(),
                performance = dataChoicesCollectionStates
                    .getPerformanceCollectionValue(),
                personalizedAds = dataChoicesCollectionStates
                    .getAllowPersonalizedAdsValue(),
                version = appConfigProvider.version,
                viewState = SettingsState.ViewState.Success
            )
        }
    }

    private suspend fun setPreferencesStateLimited() {
        updateState {
            copy(
                email = getCurrentUserEmailAddress()
            )
        }
    }
}
