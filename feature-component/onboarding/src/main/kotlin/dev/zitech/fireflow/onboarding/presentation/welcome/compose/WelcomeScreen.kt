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

package dev.zitech.fireflow.onboarding.presentation.welcome.compose

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.atoms.text.FireFlowClickableTexts
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.ds.organisms.expandable.card.FireFlowExpandableTextCards
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme
import dev.zitech.fireflow.onboarding.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    modifier: Modifier = Modifier,
    backClicked: () -> Unit = {},
    continueWithOauthClicked: () -> Unit = {},
    continueWithPatClicked: () -> Unit = {},
    fireflyClicked: () -> Unit = {},
    getStartedClicked: () -> Unit = {},
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    BackHandler(enabled = true) {
        backClicked()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .navigationBarsPadding(),
        snackbarState = snackbarState
    ) { innerPadding ->
        WelcomeScreenContent(
            innerPadding = innerPadding,
            continueWithOauthClicked = continueWithOauthClicked,
            continueWithPatClicked = continueWithPatClicked,
            fireflyClicked = fireflyClicked,
            getStartedClicked = getStartedClicked
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
private fun WelcomeScreenContent(
    innerPadding: PaddingValues,
    continueWithOauthClicked: () -> Unit,
    continueWithPatClicked: () -> Unit,
    fireflyClicked: () -> Unit,
    getStartedClicked: () -> Unit
) {
    val fireflyIIIViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

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
                .padding(horizontal = FireFlowTheme.space.gutter)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FireFlowAnimations.MoneyTree(
                modifier = Modifier
                    .heightIn(max = 192.dp)
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowTexts.DisplayMedium(
                text = stringResource(R.string.welcome_slogan),
                textAlign = TextAlign.Center
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.l)
            FireFlowButtons.Filled.OnSurfaceTint(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome_button_get_started),
                onClick = getStartedClicked
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            FireFlowExpandableTextCards.Transparent.Icon(
                modifier = Modifier
                    .bringIntoViewRequester(fireflyIIIViewRequester),
                topContent = {
                    FireFlowTexts.BodyMedium(
                        modifier = Modifier
                            .weight(1F)
                            .padding(start = FireFlowTheme.space.s),
                        text = stringResource(R.string.welcome_firefly_iii_already_using),
                        color = FireFlowTheme.colors.onSurface
                    )
                },
                bottomContent = {
                    FireFlowButtons.Filled.OnSurfaceTint(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FireFlowTheme.space.s),
                        text = stringResource(R.string.welcome_button_continue_with_oauth),
                        onClick = continueWithOauthClicked
                    )
                    FireFlowButtons.Filled.OnSurfaceTint(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FireFlowTheme.space.s),
                        text = stringResource(
                            R.string.welcome_button_continue_with_personal_access_token
                        ),
                        onClick = continueWithPatClicked
                    )
                    FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FireFlowTheme.space.s),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(FireFlowTheme.colors.onBackground),
                            imageVector = FireFlowIcons.Info,
                            contentDescription = null
                        )
                        FireFlowClickableTexts.LabelSmall(
                            text = getFireflyInfoAnnotatedString(),
                            color = FireFlowTheme.colors.onSurface
                        ) { fireflyClicked() }
                    }
                },
                onClick = { isExpanded ->
                    if (isExpanded) {
                        coroutineScope.launch {
                            fireflyIIIViewRequester.bringIntoView()
                        }
                    }
                }
            )
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
        }
    }
}

@Composable
private fun getFireflyInfoAnnotatedString() = buildAnnotatedString {
    val fireFlyText = stringResource(R.string.welcome_firefly_iii_more_info_text)
    val fireFlyAnnotatedText = stringResource(R.string.welcome_firefly_iii_more_info_annotated_text)
    val startPosition = fireFlyText.indexOf(fireFlyAnnotatedText)
    val lastPosition = startPosition + fireFlyAnnotatedText.length

    append(fireFlyText)
    addStringAnnotation(
        tag = "URL",
        annotation = stringResource(R.string.firefly_iii_home_page_url),
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
        WelcomeScreen()
    }
}
