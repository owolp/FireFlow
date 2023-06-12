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

package dev.zitech.fireflow.ds.molecules.topappbar

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
import dev.zitech.fireflow.ds.R
import dev.zitech.fireflow.ds.atoms.button.FireFlowButtons
import dev.zitech.fireflow.ds.atoms.icon.FireFlowIcons
import dev.zitech.fireflow.ds.atoms.text.FireFlowTexts
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

/**
 * An object that provides various composable functions for creating FireFlow top app bars.
 * It also includes a function for providing scroll behavior for a top app bar.
 *
 * @see CenterAlignedTopAppBar
 * @see MediumTopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
object FireFlowTopAppBars {

    /**
     * A composable function that creates a primary top app bar with a center-aligned title.
     *
     * @param title The title text to be displayed in the top app bar.
     * @param modifier The modifier to be applied to the top app bar. Default is [Modifier].
     * @param scrollBehavior The scroll behavior for the top app bar. Default is `null`.
     */
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

    /**
     * An object that provides composable functions for creating collapsing top app bars.
     */
    object Collapsing {

        /**
         * A composable function that creates a primary collapsing top app bar with a medium-sized title.
         *
         * @param title The title text to be displayed in the top app bar.
         * @param modifier The modifier to be applied to the top app bar. Default is [Modifier].
         * @param scrollBehavior The scroll behavior for the top app bar. Default is `null`.
         */
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

        /**
         * A composable function that creates a collapsing top app bar with a medium-sized title and
         * a back navigation icon.
         *
         * @param modifier The modifier to be applied to the top app bar. Default is [Modifier].
         * @param title The title text to be displayed in the top app bar. Default is `null`.
         * @param onNavigationClick The callback to be invoked when the back navigation icon is
         * clicked. Default is an empty callback.
         * @param scrollBehavior The scroll behavior for the top app bar. Default is `null`.
         */
        @Composable
        fun BackNavigation(
            modifier: Modifier = Modifier,
            title: String? = null,
            onNavigationClick: () -> Unit = {},
            scrollBehavior: TopAppBarScrollBehavior? = null
        ) {
            MediumTopAppBar(
                modifier = modifier,
                title = { title?.let { FireFlowTexts.HeadlineSmall(text = it) } },
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
    }

    /**
     * A composable function that creates a top app bar with a center-aligned title and a back
     * navigation icon.
     *
     * @param modifier The modifier to be applied to the top app bar. Default is [Modifier].
     * @param title The title text to be displayed in the top app bar. Default is `null`.
     * @param onNavigationClick The callback to be invoked when the back navigation icon is clicked.
     * Default is an empty callback.
     * @param scrollBehavior The scroll behavior for the top app bar. Default is `null`.
     */
    @Composable
    fun BackNavigation(
        modifier: Modifier = Modifier,
        title: String? = null,
        onNavigationClick: () -> Unit = {},
        scrollBehavior: TopAppBarScrollBehavior? = null
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = { title?.let { FireFlowTexts.TitleLarge(text = it) } },
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

    /**
     * A composable function that provides scroll behavior for a top app bar based on the specified
     * [scrollBehavior] parameter.
     *
     * @param scrollBehavior The scroll behavior for the top app bar. The default value is
     * [ScrollBehavior.Pinned].
     * @param canScroll A lambda function that determines if scrolling is allowed. It should return
     * `true` if scrolling is allowed,
     * and `false` otherwise. By default, scrolling is always allowed.
     * @param topAppBarState The state of the top app bar. This parameter is optional, and if not
     * provided, a default state
     * will be used by calling [rememberTopAppBarState].
     *
     * @return The scroll behavior for the top app bar based on the specified [scrollBehavior].
     */
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
