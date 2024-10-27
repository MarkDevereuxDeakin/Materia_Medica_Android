package com.yureiapplications.materiamedica.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yureiapplications.materiamedica.model.Medicines;
import com.yureiapplications.materiamedica.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION); //Database now version 2. Added Reference Column
        this.context = context;
        if(!isDatabaseExists()){
            copyDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + Util.TABLE_NAME_1 + "("
                + Util.CATEGORIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.CATEGORIES_NAME + " TEXT, "
                + Util.CATEGORIES_DESCRIPTION + " TEXT, "
                + Util.CATEGORIES_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_MEDICINES_TABLE = "CREATE TABLE " + Util.TABLE_NAME_2 + "("
                + Util.MEDICINES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.MEDICINES_NAME + " TEXT, "
                + Util.MEDICINES_DESCRIPTION + " TEXT, "
                + Util.MEDICINES_IMAGE + " TEXT, "
                + Util.MEDICINES_CATEGORIES + " TEXT, "
                + Util.MEDICINES_REFERENCES + " TEXT " + ")";
        db.execSQL(CREATE_MEDICINES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2){
            String UPGRADE_TABLE = "ALTER TABLE " + Util.TABLE_NAME_2 + " ADD " + Util.MEDICINES_REFERENCES + " TEXT ";
            db.execSQL(UPGRADE_TABLE);
        }
    }

    private boolean isDatabaseExists(){
        File dbFile = new File(Util.DATABASE_PATH + Util.DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open("database/" + Util.DATABASE_NAME);
            String outFileName = Util.DATABASE_PATH + Util.DATABASE_NAME;

            // Create the databases directory if it doesn't exist
            File databaseDir = new File(Util.DATABASE_PATH);
            if (!databaseDir.exists()) {
                databaseDir.mkdirs();
            }

            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.i(TAG, "Database copied successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error copying database: " + e.getMessage());
        }
    }

    // Get the number of rows in a table
    public int numberOfRows(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int rows = (int) DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return rows;
    }

    // Fetch Categories data
    public List<String> fetchCategoriesName() {
        List<String> categoriesNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_1, new String[]{Util.CATEGORIES_NAME}, null, null, null, null, null);

        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex(Util.CATEGORIES_NAME);
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    do {
                        categoriesNames.add(cursor.getString(nameIndex));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return categoriesNames;
    }

    public List<String> fetchCategoriesDescription() {
        List<String> categoriesDescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_1, new String[]{Util.CATEGORIES_DESCRIPTION}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int descriptionIndex = cursor.getColumnIndex(Util.CATEGORIES_DESCRIPTION);

            // Check if the column exists
            if (descriptionIndex != -1) {
                do {
                    categoriesDescriptions.add(cursor.getString(descriptionIndex));
                } while (cursor.moveToNext());
            } else {
                // Log an error or handle the case where the column does not exist
                throw new IllegalStateException("Column " + Util.CATEGORIES_DESCRIPTION + " does not exist in the database.");
            }
        }

        cursor.close();
        db.close();
        return categoriesDescriptions;
    }

   public List<String> fetchCategoriesImagePath() {
       List<String> imagePaths = new ArrayList<>();
       SQLiteDatabase db = this.getReadableDatabase();

       Cursor cursor = db.query(Util.TABLE_NAME_1, new String[]{Util.CATEGORIES_IMAGE}, null, null, null, null, null);

       if (cursor != null && cursor.moveToFirst()) {
           int imageIndex = cursor.getColumnIndex(Util.CATEGORIES_IMAGE);
           do {
               if (imageIndex != -1) {
                   imagePaths.add(cursor.getString(imageIndex));
               }
           } while (cursor.moveToNext());
           cursor.close();
       }
       db.close();
       return imagePaths;
   }

   //Fetches an alphabetised list of Medicines selected according to Category
    public List<Medicines> fetchMedicinesByCategory(String category) {
        List<Medicines> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modify the query to sort the results by medicines_name in ascending order
        String query = "SELECT * FROM " + Util.TABLE_NAME_2 +
                " WHERE " + Util.MEDICINES_CATEGORIES + " = ?" +
                " ORDER BY " + Util.MEDICINES_NAME + " ASC";

        try (Cursor cursor = db.rawQuery(query, new String[]{category})) {
            if (cursor != null && cursor.moveToFirst()) {
                // Get column indexes safely
                int nameIndex = cursor.getColumnIndex(Util.MEDICINES_NAME);
                int descriptionIndex = cursor.getColumnIndex(Util.MEDICINES_DESCRIPTION);
                int imagePathIndex = cursor.getColumnIndex(Util.MEDICINES_IMAGE);
                int referencesIndex = cursor.getColumnIndexOrThrow(Util.MEDICINES_REFERENCES);

                do {
                    // Ensure valid column indexes before retrieving data
                    String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "";
                    String description = (descriptionIndex != -1) ? cursor.getString(descriptionIndex) : "";
                    String imagePath = (imagePathIndex != -1) ? cursor.getString(imagePathIndex) : "";
                    String references = (referencesIndex != -1) ? cursor.getString(referencesIndex) : "";

                    // Add the medicine object to the list
                    medicines.add(new Medicines(name, description, imagePath, references));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching medicines for category: " + category, e);
        }

        return medicines;
    }

    // Fetch Medicines data
    public List<String> fetchMedicinesName() {
        List<String> medicinesNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_2, new String[]{Util.MEDICINES_NAME}, null, null, null, null, null);

        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex(Util.MEDICINES_NAME);
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    do {
                        medicinesNames.add(cursor.getString(nameIndex));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return medicinesNames;
    }

    public List<String> fetchMedicinesDescription() {
        List<String> medicinesDescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_2, new String[]{Util.MEDICINES_DESCRIPTION}, null, null, null, null, null);

        if (cursor != null) {
            try {
                int descriptionIndex = cursor.getColumnIndex(Util.MEDICINES_DESCRIPTION);
                if (descriptionIndex != -1 && cursor.moveToFirst()) {
                    do {
                        medicinesDescriptions.add(cursor.getString(descriptionIndex));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return medicinesDescriptions;
    }


    public List<String> fetchMedicinesImage() {
        List<String> medicinesImages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_2, new String[]{Util.MEDICINES_IMAGE}, null, null, null, null, null);

        if (cursor != null) {
            try {
                int imageIndex = cursor.getColumnIndex(Util.MEDICINES_IMAGE);
                if (imageIndex != -1 && cursor.moveToFirst()) {
                    do {
                        medicinesImages.add(cursor.getString(imageIndex));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return medicinesImages;
    }
}
