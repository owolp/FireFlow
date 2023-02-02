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

package dev.zitech.onboarding.presentation.pat.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dev.zitech.onboarding.presentation.pat.viewmodel.PatState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PatScreen(
    patState: PatState,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    onServerAddressChange: (String) -> Unit,
    onPersonalAccessTokenChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    FireFlowScaffolds.Primary(
        modifier = modifier
            .navigationBarsPadding(),
        snackbarState = snackbarState,
        topBar = {
            FireFlowTopAppBars.BackNavigation(
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        PatScreenContent(
            innerPadding,
            patState,
            onLoginClick,
            onServerAddressChange,
            onPersonalAccessTokenChange
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PatScreenContent(
    innerPadding: PaddingValues,
    state: PatState,
    onLoginClick: () -> Unit,
    onServerAddressChange: (String) -> Unit,
    onPersonalAccessTokenChange: (String) -> Unit,
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
            headlineText = stringResource(R.string.pat_server_address),
            value = state.serverAddress,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onValueChanged = onServerAddressChange
        )
        FireFlowInputForm.TitleAndInput(
            modifier = Modifier.fillMaxWidth(),
            headlineText = stringResource(R.string.pat_personal_access_token),
            value = state.pat,
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
            onValueChanged = onPersonalAccessTokenChange
        )
        FireFlowSpacers.Vertical(modifier = Modifier.weight(1F))
        FireFlowButtons.Filled.OnSurfaceTint(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.pat_login),
            enabled = state.loginEnabled,
            onClick = onLoginClick
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
        PatScreen(
            patState = PatState(),
            onLoginClick = {},
            onBackClick = {},
            onServerAddressChange = {},
            onPersonalAccessTokenChange = {}
        )
    }
}
