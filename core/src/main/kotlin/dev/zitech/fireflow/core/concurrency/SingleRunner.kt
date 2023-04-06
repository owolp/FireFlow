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

package dev.zitech.fireflow.core.concurrency

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A helper class to execute tasks sequentially in coroutines.
 *
 * Calling [afterPrevious] will always ensure that all previously requested work completes prior to
 * calling the block passed. Any future calls to [afterPrevious] while the current block is running
 * will wait for the current block to complete before starting.
 */
class SingleRunner {
    /**
     * A coroutine mutex implements a lock that may only be taken by one coroutine at a time.
     */
    private val mutex = Mutex()

    /**
     * Ensure that the block will only be executed after all previous work has completed.
     *
     * When several coroutines call afterPrevious at the same time, they will queue up in the order
     * that they call afterPrevious. Then, one coroutine will enter the block at a time.
     *
     * In the following example, only one save operation (user or song) will be executing at a time.
     *
     * ```
     * class UserAndSongSaver {
     *    val singleRunner = SingleRunner()
     *
     *    fun saveUser(user: User) {
     *        singleRunner.afterPrevious { api.post(user) }
     *    }
     *
     *    fun saveSong(song: Song) {
     *        singleRunner.afterPrevious { api.post(song) }
     *    }
     * }
     * ```
     *
     * @param block the code to run after previous work is complete.
     */
    suspend fun <T> afterPrevious(block: suspend () -> T): T {
        // Before running the block, ensure that no other blocks are running by taking a lock on the
        // mutex.

        // The mutex will be released automatically when we return.

        // If any other block were already running when we get here, it will wait for it to complete
        // before entering the `withLock` block.
        mutex.withLock {
            return block()
        }
    }
}
