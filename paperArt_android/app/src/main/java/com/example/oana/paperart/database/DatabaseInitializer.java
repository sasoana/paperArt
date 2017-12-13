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
import com.example.oana.paperart.PaperItem;
import com.example.oana.paperart.Rating;

import java.util.Date;

public class DatabaseInitializer {

    public static void populate(final AppDatabase db) {
        populateWithTestData(db);
    }

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void populateWithTestData(AppDatabase db) {
        db.ratingDAO().deleteAll();
        db.paperItemDAO().deleteAll();
        db.categoryDAO().deleteAll();

        Integer newId = 0;
        long cat1 = db.categoryDAO().addCategory(new Category(newId, "Pure", "Origami from one sheet of paper, no cuts", "pure"));
        long cat2 = db.categoryDAO().addCategory(new Category(newId, "Kusudama", "Flower-like spheres", "kusudama"));
        long cat3 = db.categoryDAO().addCategory(new Category(newId, "Modular", "Origami composed of 2 or more parts", "modular"));

        long mod1 = db.paperItemDAO().addItem(new PaperItem(newId, (int)cat1, "Cat", "Regular", "Grey", 25, new Date()));
        long mod2 = db.paperItemDAO().addItem(new PaperItem(newId, (int)cat2, "Dragon", "Tant", "Red", 225, new Date()));
        long mod3 = db.paperItemDAO().addItem(new PaperItem(newId, (int)cat2, "Boat", "Kami", "Blue", 20, new Date()));
        long mod4 = db.paperItemDAO().addItem(new PaperItem(newId, (int)cat3, "Lilly", "Regular", "White", 15, new Date()));

        db.ratingDAO().add(new Rating(newId, (int)mod1, 3, "", new Date()));
        db.ratingDAO().add(new Rating(newId, (int)mod1, 2, "", new Date(System.currentTimeMillis()-24*60*60*1000)));

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