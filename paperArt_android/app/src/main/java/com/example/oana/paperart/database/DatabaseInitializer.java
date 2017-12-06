package com.example.oana.paperart.database;
/*
 * Copyright 2017, The Android Open Source Project
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

import android.os.AsyncTask;

import com.example.oana.paperart.Category;

public class DatabaseInitializer {

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void populateWithTestData(AppDatabase db) {
        db.categoryDAO().deleteAll();


        db.categoryDAO().addCategory(new Category(1, "Pure", "Origami from one sheet of paper, no cuts", "pure"));
        db.categoryDAO().addCategory(new Category(2, "Kusudama", "Flower-like spheres", "kusudama"));
        db.categoryDAO().addCategory(new Category(3, "Modular", "Origami composed of 2 or more parts", "modular"));

    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}