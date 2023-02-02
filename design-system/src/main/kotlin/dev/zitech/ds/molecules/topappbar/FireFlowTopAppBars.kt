/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.ds.molecules.topappbar

import android.content.res.Configuration
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.R
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.PreviewFireFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
object FireFlowTopAppBars {

    @Composable
    fun Primary(
        title: String,
        modifier: Modifier = Modifier,
        scrollBehavior: TopAppBarScrollBehavior? = null
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = { FireFlowTexts.TitleLarge(text = title) },
            scrollBehavior = scrollBehavior
        )
    }

    object Collapsing {

        @Composable
        fun Primary(
            title: String,
            modifier: Modifier = Modifier,
            scrollBehavior: TopAppBarScrollBehavior? = null
        ) {
            MediumTopAppBar(
                modifier = modifier,
                title = { FireFlowTexts.HeadlineSmall(text = title) },
                scrollBehavior = scrollBehavior
            )
        }
    }

    @Composable
    fun BackNavigation(
        modifier: Modifier = Modifier,
        title: String? = null,
        onNavigationClick: () -> Unit = {},
        scrollBehavior: TopAppBarScrollBehavior? = null
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = { title?.let { FireFlowTexts.TitleLarge(text = title) } },
            navigationIcon = {
                FireFlowButtons.Icon(
                    image = FireFlowIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.cd_arrow_button_back),
                    onClick = onNavigationClick
                )
            },
            scrollBehavior = scrollBehavior
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarScrollBehavior(
        scrollBehavior: ScrollBehavior = ScrollBehavior.Pinned,
        canScroll: () -> Boolean = { true },
        topAppBarState: TopAppBarState = rememberTopAppBarState()
    ) = when (scrollBehavior) {
        ScrollBehavior.EnterAlways -> {
            TopAppBarDefaults.enterAlwaysScrollBehavior(
                state = topAppBarState,
                canScroll = canScroll
            )
        }
        is ScrollBehavior.ExitUntilCollapsed -> {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                state = topAppBarState,
                canScroll = canScroll
            )
        }
        ScrollBehavior.Pinned -> {
            TopAppBarDefaults.pinnedScrollBehavior(
                state = topAppBarState,
                canScroll = canScroll
            )
        }
    }
}

@Preview(
    name = "TopAppBar Primary Light Theme",
    showBackground = true
)
@Preview(
    name = "TopAppBar Primary Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar_Primary_Preview() {
    PreviewFireFlowTheme {
        FireFlowTopAppBars.Primary("Primary")
    }
}

@Preview(
    name = "Collapsing Primary Primary Light Theme",
    showBackground = true
)
@Preview(
    name = "Collapsing Primary Primary Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Collapsing_Primary_Primary_Preview() {
    PreviewFireFlowTheme {
        FireFlowTopAppBars.Collapsing.Primary("Primary")
    }
}

@Preview(
    name = "TopAppBar Navigation Light Theme",
    showBackground = true
)
@Preview(
    name = "TopAppBar Navigation Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar_Navigation_Preview() {
    PreviewFireFlowTheme {
        FireFlowTopAppBars.BackNavigation(
            title = "Navigation"
        )
    }
}
