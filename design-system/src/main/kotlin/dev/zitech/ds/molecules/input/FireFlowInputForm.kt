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

package dev.zitech.ds.molecules.input

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.text.FireFlowInputTexts
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowInputForm {

    @Composable
    fun TitleAndInput(
        headlineText: String,
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        supportingText: String? = null,
        isError: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.xs)
        ) {
            FireFlowTexts.HeadlineMedium(
                modifier = Modifier.fillMaxWidth(),
                text = headlineText
            )
            FireFlowInputTexts.BodyLarge(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChanged = onValueChanged,
                supportingText = supportingText,
                isError = isError,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions
            )
        }
    }
}

@Preview(
    name = "TitleAndInput Light Theme",
    showBackground = true
)
@Preview(
    name = "TitleAndInput Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun TitleAndInput_Preview() {
    PreviewFireFlowTheme {
        FireFlowInputForm.TitleAndInput(
            headlineText = "Headline",
            "Input value",
            onValueChanged = {}
        )
    }
}