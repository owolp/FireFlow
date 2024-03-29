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

package dev.zitech.fireflow.common.data.source.preferences

import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing and modifying key-value pairs stored in preferences.
 */
internal interface PreferencesDataSource {

    /**
     * Checks if the preferences contain a boolean value associated with the specified key.
     *
     * @param key The key to check.
     * @return A flow that emits `true` if the key exists, and `false` otherwise.
     */
    fun containsBoolean(key: String): Flow<OperationResult<Boolean>>

    /**
     * Checks if the preferences contain a float value associated with the specified key.
     *
     * @param key The key to check.
     * @return A flow that emits `true` if the key exists, and `false` otherwise.
     */
    fun containsFloat(key: String): Flow<OperationResult<Boolean>>

    /**
     * Checks if the preferences contain an integer value associated with the specified key.
     *
     * @param key The key to check.
     * @return A flow that emits `true` if the key exists, and `false` otherwise.
     */
    fun containsInt(key: String): Flow<OperationResult<Boolean>>

    /**
     * Checks if the preferences contain a long value associated with the specified key.
     *
     * @param key The key to check.
     * @return A flow that emits `true` if the key exists, and `false` otherwise.
     */
    fun containsLong(key: String): Flow<OperationResult<Boolean>>

    /**
     * Checks if the preferences contain a string value associated with the specified key.
     *
     * @param key The key to check.
     * @return A flow that emits `true` if the key exists, and `false` otherwise.
     */
    fun containsString(key: String): Flow<OperationResult<Boolean>>

    /**
     * Retrieves a boolean value associated with the specified key from the preferences.
     *
     * @param key The key of the boolean value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A flow that emits the boolean value associated with the key, or the default value if the key is not found
     * .
     */
    suspend fun getBoolean(
        key: String,
        defaultValue: Boolean = DEFAULT_VALUE_GET_BOOLEAN
    ): Flow<OperationResult<Boolean>>

    /**
     * Retrieves a float value associated with the specified key from the preferences.
     *
     * @param key The key of the float value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A flow that emits the float value associated with the key, or the default value if the key is not found.
     */
    suspend fun getFloat(
        key: String,
        defaultValue: Float = DEFAULT_VALUE_GET_FLOAT
    ): Flow<OperationResult<Float>>

    /**
     * Retrieves an integer value associated with the specified key from the preferences.
     *
     * @param key The key of the integer value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A flow that emits the integer value associated with the key, or the default value if the key is not found
     * .
     */
    suspend fun getInt(
        key: String,
        defaultValue: Int = DEFAULT_VALUE_GET_INT
    ): Flow<OperationResult<Int>>

    /**
     * Retrieves a long value associated with the specified key from the preferences.
     *
     * @param key The key of the long value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A flow that emits the long value associated with the key, or the default value if the key is not found.
     */
    suspend fun getLong(
        key: String,
        defaultValue: Long = DEFAULT_VALUE_GET_LONG
    ): Flow<OperationResult<Long>>

    /**
     * Retrieves a string value associated with the specified key from the preferences.
     *
     * @param key The key of the string value.
     * @param defaultValue The default value to return if the key is not found.
     * @return A flow that emits the string value associated with the key, or the default value if the key is not found.
     */
    suspend fun getString(
        key: String,
        defaultValue: String? = null
    ): Flow<OperationResult<String>>

    /**
     * Removes all key-value pairs from the preferences.
     *
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun removeAll(): OperationResult<Unit>

    /**
     * Removes the boolean value associated with the specified key from the preferences.
     *
     * @param key The key of the boolean value to be removed.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun removeBoolean(key: String): OperationResult<Unit>

    /**
     * Removes the float value associated with the specified key from the preferences.
     *
     * @param key The key of the float value to be removed.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun removeFloat(key: String): OperationResult<Unit>

    /**
     * Removes the integer value associated with the specified key from the preferences.
     *
     * @param key The key of the integer value to be removed.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun removeInt(key: String): OperationResult<Unit>

    /**
     * Removes the long value associated with the specified key from the preferences.
     *
     * @param key The key of the long value to be removed.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun removeLong(key: String): OperationResult<Unit>

    /**
     * Removes the string value associated with the specified key from the preferences.
     *
     * @param key The key of the string value to remove.
     */
    suspend fun removeString(key: String): OperationResult<Unit>

    /**
     * Saves a boolean value associated with the specified key to the preferences.
     *
     * @param key The key of the boolean value.
     * @param value The boolean value to be saved.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun saveBoolean(key: String, value: Boolean): OperationResult<Unit>

    /**
     * Saves a float value associated with the specified key to the preferences.
     *
     * @param key The key of the float value.
     * @param value The float value to be saved.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun saveFloat(key: String, value: Float): OperationResult<Unit>

    /**
     * Saves an integer value associated with the specified key to the preferences.
     *
     * @param key The key of the integer value.
     * @param value The integer value to be saved.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun saveInt(key: String, value: Int): OperationResult<Unit>

    /**
     * Saves a long value associated with the specified key to the preferences.
     *
     * @param key The key of the long value.
     * @param value The long value to be saved.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun saveLong(key: String, value: Long): OperationResult<Unit>

    /**
     * Saves a string value associated with the specified key to the preferences.
     *
     * @param key The key of the string value.
     * @param value The string value to be saved.
     * @return An [OperationResult] representing the result of the operation.
     */
    suspend fun saveString(key: String, value: String): OperationResult<Unit>

    companion object {
        const val DEFAULT_VALUE_GET_BOOLEAN = false
        const val DEFAULT_VALUE_GET_FLOAT = Float.MIN_VALUE
        const val DEFAULT_VALUE_GET_INT = Int.MIN_VALUE
        const val DEFAULT_VALUE_GET_LONG = Long.MIN_VALUE
    }
}
