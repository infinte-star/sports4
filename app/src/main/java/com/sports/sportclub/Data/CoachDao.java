package com.sports.sportclub.Data;

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;


/**
 * Data access object for Coach.
 */
@Dao
public interface CoachDao {

    /**
     * Counts the number of cheeses in the table.
     *
     * @return The number of cheeses.
     */
    @Query("SELECT COUNT(*) FROM " + Coach.TABLE_NAME)
    int count();

    /**
     * Inserts a coach into the table.
     *
     * @param coach A new coach.
     * @return The row ID of the newly inserted coach.
     */
    @Insert
    long insert(Coach coach);

    /**
     * Inserts multiple coaches into the database
     *
     * @param coaches An array of new coaches.
     * @return The row IDs of the newly inserted coaches.
     */
    @Insert
    long[] insertAll(Coach[] coaches);

    /**
     * Select all cheeses.
     *
     * @return A {@link Cursor} of all the cheeses in the table.
     */
    @Query("SELECT * FROM " + Coach.TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a cheese by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected cheese.
     */
    @Query("SELECT * FROM " + Coach.TABLE_NAME + " WHERE " + Coach.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    /**
     * Delete a cheese by the ID.
     *
     * @param id The row ID.
     * @return A number of cheeses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Coach.TABLE_NAME + " WHERE " + Coach.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the coach. The coach is identified by the row ID.
     *
     * @param coach The coach to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */
    @Update
    int update(Coach coach);

}
