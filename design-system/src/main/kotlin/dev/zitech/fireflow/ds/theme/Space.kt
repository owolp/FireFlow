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

package dev.zitech.fireflow.ds.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class Space(
    val gutter: Dp = 16.dp,
    val xss: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 16.dp,
    val l: Dp = 32.dp,
    val xl: Dp = 64.dp,
    val xxl: Dp = 128.dp
) {

    fun copy(
        gutter: Dp = this.gutter,
        xss: Dp = this.xss,
        xs: Dp = this.xs,
        s: Dp = this.s,
        m: Dp = this.m,
        l: Dp = this.l,
        xl: Dp = this.xl,
        xxl: Dp = this.xxl
    ): Space = Space(
        xss = xss,
        xs = xs,
        s = s,
        m = m,
        l = l,
        xl = xl,
        xxl = xxl
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Space) return false
        if (gutter != other.gutter) return false
        if (xss != other.xss) return false
        if (xs != other.xs) return false
        if (s != other.s) return false
        if (m != other.m) return false
        if (l != other.l) return false
        if (xl != other.xl) return false
        if (xxl != other.xxl) return false
        return true
    }

    override fun hashCode(): Int {
        var result = gutter.hashCode()
        result = 31 * result + xss.hashCode()
        result = 31 * result + xs.hashCode()
        result = 31 * result + s.hashCode()
        result = 31 * result + m.hashCode()
        result = 31 * result + l.hashCode()
        result = 31 * result + xl.hashCode()
        result = 31 * result + xxl.hashCode()
        return result
    }

    override fun toString(): String {
        return "Space(" +
            "gutter=$gutter, " +
            "xss=$xss, " +
            "xs=$xs, " +
            "s=$s, " +
            "m=$m, " +
            "l=$l, " +
            "xl=$xl, " +
            "xxl=$xxl)"
    }
}
