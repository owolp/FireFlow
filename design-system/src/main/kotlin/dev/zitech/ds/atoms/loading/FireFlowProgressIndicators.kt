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
@file:Suppress("MagicNumber")

package dev.zitech.ds.atoms.loading

import android.content.res.Configuration
import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.zitech.ds.R
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowProgressIndicators {

    @Composable
    fun Settings(modifier: Modifier = Modifier) {
        ProgressIndicatorItem(
            modifier = modifier,
            resId = R.raw.animation_settings
        )
    }

    @Composable
    private fun ProgressIndicatorItem(
        modifier: Modifier = Modifier,
        @RawRes resId: Int
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(resId)
            )
            LottieAnimation(
                modifier = Modifier.size(128.dp),
                composition = composition,
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )
        }
    }

    @Composable
    fun FlashingDots(
        modifier: Modifier = Modifier,
        size: Dp = 14.dp,
        color: Color = FireFlowTheme.colors.onPrimary,
        delayUnit: Int = 300,
        minAlpha: Float = 0.0f
    ) {
        @Composable
        fun Dot(
            alpha: Float
        ) = FireFlowSpacers.Square(
            size = size,
            modifier = Modifier
                .alpha(alpha)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        val infiniteTransition = rememberInfiniteTransition()

        @Composable
        fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
            initialValue = minAlpha,
            targetValue = minAlpha,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = delayUnit * 4
                    minAlpha at delay with LinearEasing
                    1f at delay + delayUnit with LinearEasing
                    minAlpha at delay + delayUnit * 2
                }
            )
        )

        val alpha1 by animateAlphaWithDelay(0)
        val alpha2 by animateAlphaWithDelay(delayUnit)
        val alpha3 by animateAlphaWithDelay(delayUnit * 2)

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val spaceSize = 2.dp

            Dot(alpha1)
            FireFlowSpacers.Horizontal(horizontalSpace = spaceSize)
            Dot(alpha2)
            FireFlowSpacers.Horizontal(horizontalSpace = spaceSize)
            Dot(alpha3)
        }
    }
}

@Preview(
    name = "FlashingDots Light Theme",
    showBackground = true
)
@Preview(
    name = "FlashingDots Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun FlashingDots_Preview() {
    FireFlowTheme {
        FireFlowProgressIndicators.FlashingDots()
    }
}
