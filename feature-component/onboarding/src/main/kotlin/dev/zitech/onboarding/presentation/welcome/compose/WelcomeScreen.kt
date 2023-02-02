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

package dev.zitech.onboarding.presentation.welcome.compose

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.ds.atoms.animation.FireFlowAnimations
import dev.zitech.ds.atoms.background.FireFlowBackground
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.atoms.text.FireFlowClickableTexts
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme
import dev.zitech.onboarding.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    onContinueWithOauthClick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit,
    onBackClick: () -> Unit,
    onFireflyClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    BackHandler(enabled = true) {
        onBackClick()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .navigationBarsPadding(),
        snackbarState = snackbarState
    ) { innerPadding ->
        WelcomeScreenContent(
            innerPadding,
            onContinueWithOauthClick,
            onContinueWithPatClick,
            onDemoClick,
            onFireflyClick
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun WelcomeScreenContent(
    innerPadding: PaddingValues,
    onContinueWithOauthCLick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit,
    onFireflyClick: () -> Unit
) {
    FireFlowBackground.Gradient(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(FireFlowTheme.space.m),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FireFlowAnimations.MoneyTree(
                modifier = Modifier
                    .heightIn(max = 192.dp)
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowTexts.DisplayMedium(
                text = stringResource(R.string.welcome_slogan),
                style = FireFlowTheme.typography.displayMedium.copy(
                    textAlign = TextAlign.Center
                )
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowClickableTexts.LabelSmall(
                text = getFireflyInfoAnnotatedString(),
                color = FireFlowTheme.colors.onSurface,
                style = FireFlowTheme.typography.labelSmall.copy(
                    textAlign = TextAlign.Center
                )
            ) { onFireflyClick() }
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.l)
            FireFlowButtons.Filled.OnSurfaceTint(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome_button_continue_with_oauth),
                onClick = onContinueWithOauthCLick
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowButtons.Outlined.OnSurface(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome_button_continue_with_personal_access_token),
                onClick = onContinueWithPatClick
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowButtons.Outlined.OnSurface(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome_button_demo),
                onClick = onDemoClick
            )
        }
    }
}

@Composable
private fun getFireflyInfoAnnotatedString() = buildAnnotatedString {
    val fireFlyText = stringResource(R.string.welcome_firefly_iii_text)
    val fireFlyAnnotatedText = stringResource(R.string.welcome_firefly_iii_annotated_text)
    val startPosition = fireFlyText.indexOf(fireFlyAnnotatedText)
    val lastPosition = startPosition + fireFlyAnnotatedText.length

    append(fireFlyText)
    addStringAnnotation(
        tag = "URL",
        annotation = stringResource(R.string.welcome_firefly_iii_url),
        start = startPosition,
        end = lastPosition
    )
    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), startPosition, lastPosition)
}

@Preview(
    name = "Welcome Screen Light Theme",
    showBackground = true
)
@Preview(
    name = "Welcome Screen Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun WelcomeScreen_Preview() {
    PreviewFireFlowTheme {
        WelcomeScreen(
            onContinueWithOauthClick = {},
            onContinueWithPatClick = {},
            onDemoClick = {},
            onBackClick = {},
            onFireflyClick = {}
        )
    }
}
