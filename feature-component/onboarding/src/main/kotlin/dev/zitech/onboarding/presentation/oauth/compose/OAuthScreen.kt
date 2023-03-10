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

package dev.zitech.onboarding.presentation.oauth.compose

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.molecules.input.FireFlowInputForm
import dev.zitech.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme
import dev.zitech.onboarding.R
import dev.zitech.onboarding.presentation.oauth.viewmodel.OAuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OAuthScreen(
    oauthState: OAuthState,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    onServerAddressChange: (String) -> Unit,
    onClientIdChange: (String) -> Unit,
    onClientSecretChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    BackHandler(enabled = true) {
        if (!oauthState.loading) onBackClick()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        snackbarState = snackbarState,
        topBar = {
            FireFlowTopAppBars.BackNavigation(
                onNavigationClick = {
                    if (!oauthState.loading) onBackClick()
                }
            )
        }
    ) { innerPadding ->
        OAuthScreenContent(
            innerPadding,
            oauthState,
            onLoginClick,
            onServerAddressChange,
            onClientIdChange,
            onClientSecretChange
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OAuthScreenContent(
    innerPadding: PaddingValues,
    state: OAuthState,
    onLoginClick: () -> Unit,
    onServerAddressChange: (String) -> Unit,
    onClientIdChange: (String) -> Unit,
    onClientSecretChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
            headlineText = stringResource(R.string.oauth_server_address),
            value = state.serverAddress,
            enabled = !state.loading,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Uri
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onValueChanged = onServerAddressChange
        )
        FireFlowInputForm.TitleAndInput(
            modifier = Modifier.fillMaxWidth(),
            headlineText = stringResource(R.string.oauth_client_id),
            value = state.clientId,
            enabled = !state.loading,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onValueChanged = onClientIdChange
        )
        FireFlowInputForm.TitleAndInput(
            modifier = Modifier.fillMaxWidth(),
            headlineText = stringResource(R.string.oauth_client_secret),
            value = state.clientSecret,
            enabled = !state.loading,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (state.loginEnabled) {
                        onLoginClick()
                    }
                }
            ),
            onValueChanged = onClientSecretChange
        )
        FireFlowSpacers.Vertical(modifier = Modifier.weight(1F))
        FireFlowButtons.Filled.OnSurfaceTint(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.oauth_login),
            enabled = state.loginEnabled && !state.loading,
            loading = state.loading,
            onClick = onLoginClick
        )
        FireFlowSpacers.Vertical()
    }
}

@Preview(
    name = "Oauth Screen Light Theme",
    showBackground = true
)
@Preview(
    name = "Oauth Screen Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun OauthScreen_Preview() {
    PreviewFireFlowTheme {
        OAuthScreen(
            oauthState = OAuthState(),
            onLoginClick = {},
            onBackClick = {},
            onClientSecretChange = {},
            onClientIdChange = {},
            onServerAddressChange = {}
        )
    }
}
