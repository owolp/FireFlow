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

package dev.zitech.fireflow.onboarding.presentation.pat.compose

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.fireflow.ds.atoms.button.FireFlowButtons
import dev.zitech.fireflow.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.fireflow.ds.molecules.input.FireFlowInputForm
import dev.zitech.fireflow.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.fireflow.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme
import dev.zitech.fireflow.onboarding.R
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.PatState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PatScreen(
    modifier: Modifier = Modifier,
    backClicked: () -> Unit = {},
    loginClicked: () -> Unit = {},
    patState: PatState = PatState(),
    personalAccessTokenChanged: (String) -> Unit = {},
    serverAddressChanged: (String) -> Unit = {},
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    BackHandler(enabled = true) {
        if (!patState.loading) backClicked()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        snackbarState = snackbarState,
        topBar = {
            FireFlowTopAppBars.BackNavigation(
                onNavigationClick = {
                    if (!patState.loading) backClicked()
                }
            )
        }
    ) { innerPadding ->
        PatScreenContent(
            innerPadding = innerPadding,
            state = patState,
            loginClicked = loginClicked,
            serverAddressChanged = serverAddressChanged,
            personalAccessTokenChanged = personalAccessTokenChanged
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun PatScreenContent(
    innerPadding: PaddingValues,
    state: PatState,
    loginClicked: () -> Unit,
    serverAddressChanged: (String) -> Unit,
    personalAccessTokenChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val logInViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = FireFlowTheme.space.gutter)
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
    ) {
        FireFlowInputForm.TitleAndInput(
            modifier = Modifier.fillMaxWidth(),
            headlineText = stringResource(R.string.pat_server_address),
            value = state.serverAddress,
            enabled = !state.loading,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Uri
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onValueChanged = serverAddressChanged
        )
        FireFlowInputForm.TitleAndInput(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            logInViewRequester.bringIntoView()
                        }
                    }
                },
            headlineText = stringResource(R.string.pat_personal_access_token),
            value = state.pat,
            enabled = !state.loading,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (state.loginEnabled) {
                        loginClicked()
                    }
                }
            ),
            onValueChanged = personalAccessTokenChanged
        )
        FireFlowSpacers.Vertical(modifier = Modifier.weight(1F))
        FireFlowButtons.Filled.OnSurfaceTint(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(logInViewRequester),
            text = stringResource(R.string.pat_login),
            enabled = state.loginEnabled && !state.loading,
            loading = state.loading,
            onClick = loginClicked
        )
        FireFlowSpacers.Vertical()
    }
}

@Preview(
    name = "Pat Screen Light Theme",
    showBackground = true
)
@Preview(
    name = "Pat Screen Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PatScreen_Preview() {
    PreviewFireFlowTheme {
        PatScreen()
    }
}
