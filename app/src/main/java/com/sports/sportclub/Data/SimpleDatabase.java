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

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.VisibleForTesting;

/**
 * The Room database.
 */
@Database(entities = {Coach.class}, version = 1)
public abstract class SimpleDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Coach table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract CoachDao coach();

    /** The only instance */
    private static SimpleDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized SimpleDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), SimpleDatabase.class, "ex")
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                SimpleDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        if (coach().count() == 0) {
            Coach coach = new Coach();
            beginTransaction();
            try {
                for (int i = 0; i < Coach.COACHES.length; i++) {
                    coach.name = Coach.COACHES[i];
                    coach().insert(coach);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
