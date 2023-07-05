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

package dev.zitech.fireflow.authentication.presentation.accounts.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import dev.zitech.fireflow.authentication.R
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.AccountsState
import dev.zitech.fireflow.ds.atoms.button.FireFlowButtons
import dev.zitech.fireflow.ds.atoms.dropdown.DropDownMenuItem
import dev.zitech.fireflow.ds.atoms.icon.FireFlowIcons
import dev.zitech.fireflow.ds.molecules.snackbar.FireFlowSnackbarState
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.fireflow.ds.molecules.topappbar.ScrollBehavior
import dev.zitech.fireflow.ds.organisms.account.FireFlowAccounts
import dev.zitech.fireflow.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.fireflow.ds.theme.FireFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsScreen(
    isBackNavigationSupported: Boolean,
    accountsState: AccountsState,
    backClicked: (backNavigationSupported: Boolean) -> Unit,
    onMoreItemClicked: (identification: String, menuItemId: Int, userId: Long) -> Unit,
    onMoreClicked: (userId: Long) -> Unit,
    onMoreDismissed: (userId: Long) -> Unit,
    onNewAccountClicked: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarState: FireFlowSnackbarState = rememberSnackbarState()
) {
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.ExitUntilCollapsed
    )

    BackHandler(enabled = true) {
        backClicked(isBackNavigationSupported)
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        snackbarState = snackbarState,
        topBar = {
            if (isBackNavigationSupported) {
                FireFlowTopAppBars.Collapsing.BackNavigation(
                    title = stringResource(R.string.accounts),
                    scrollBehavior = topAppBarScrollBehavior,
                    onNavigationClick = { backClicked(true) }
                )
            } else {
                FireFlowTopAppBars.Collapsing.Primary(
                    title = stringResource(R.string.accounts),
                    scrollBehavior = topAppBarScrollBehavior
                )
            }
        }
    ) { innerPadding ->
        AccountsContent(
            innerPadding = innerPadding,
            state = accountsState,
            onMoreClicked = onMoreClicked,
            onMoreDismissed = onMoreDismissed,
            onMoreItemClicked = onMoreItemClicked,
            onNewAccountClicked = onNewAccountClicked
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AccountsContent(
    innerPadding: PaddingValues,
    onMoreItemClicked: (identification: String, menuItemId: Int, userId: Long) -> Unit,
    onMoreClicked: (userId: Long) -> Unit,
    onMoreDismissed: (userId: Long) -> Unit,
    onNewAccountClicked: () -> Unit,
    state: AccountsState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = FireFlowTheme.space.gutter)
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
        verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.m)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
        ) {
            items(state.accounts) { item ->
                val user = item.user
                FireFlowAccounts.User(
                    initial = user.retrieveInitial(),
                    topInfo = user.retrieveIdentifier(),
                    bottomInfo = user.retrieveServerAddress(),
                    isLogged = user.isCurrentUser,
                    more = item.more,
                    menuItems = item.menuItems.map { menuItem ->
                        DropDownMenuItem(
                            id = menuItem.id,
                            text = stringResource(menuItem.resId)
                        )
                    },
                    onMoreItemClick = { menuItemId ->
                        onMoreItemClicked(user.retrieveIdentifier(), menuItemId, user.id)
                    },
                    onMoreClick = { onMoreClicked(user.id) },
                    onMoreDismiss = { onMoreDismissed(user.id) }
                )
            }
        }
        FireFlowButtons.IconText.OnSurface(
            text = stringResource(R.string.accounts_button_add_account),
            image = FireFlowIcons.PersonAdd,
            contentDescription = stringResource(R.string.cd_accounts_button_add_account),
            onClick = onNewAccountClicked
        )
    }
}
