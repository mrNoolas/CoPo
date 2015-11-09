/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class EntriesDBHelper extends SQLiteOpenHelper{
    private static final String TAG = EntriesDBHelper.class.getSimpleName();
    private static EntriesDBHelper instance;

    // Database info
    private static final String DATABASE_NAME = "entryDatabase";
    private static final int DATABASE_VERSION = 2;

    // table names
    private static final String TABLE_ARTICLES = "articles";

    // entries table columns
    private static final String KEY_ENTRY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE_OF_LAST_EDIT = "dole";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LESSON_LEARNED = "lessonLearned";

    public static synchronized EntriesDBHelper getInstance(Context context) {
         // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
          instance = new EntriesDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private EntriesDBHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ENTRIES_TABLE = "CREATE TABLE " + TABLE_ARTICLES +
                "(" +
                    KEY_ENTRY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                    KEY_TYPE + " TEXT," +
                KEY_TITLE + " TEXT," +
                    KEY_DATE_OF_LAST_EDIT + " TEXT," + // DDMMYYYYHHMM
                    KEY_DESCRIPTION + " TEXT," +
                    KEY_LESSON_LEARNED + " TEXT" +
                ")";

        db.execSQL(CREATE_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: when the database is updated, update table instead of recreating it...
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
            onCreate(db);
        }
    }

    public int addOrUpdateEntry (Entry entry) {
        /* returns:
         * 0 - failed / exception
         * 1 - successful -> updated existing entry
         * 2 - successful -> inserted new entry
         */

        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            int primaryKey = entry.getId();
            ContentValues values = new ContentValues();
            values.put(KEY_TYPE, entry.getType());
            values.put(KEY_TITLE, entry.getName());
            values.put(KEY_DATE_OF_LAST_EDIT, entry.getDateOfLastEdit());
            values.put(KEY_DESCRIPTION, entry.getDescription());
            values.put(KEY_LESSON_LEARNED, entry.getLessonLearned());

            // First try to update the entry
            int rows = 0;
            if (entry.getId() != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_ARTICLES, values,
                        KEY_ENTRY_ID + "= ?", new String[]{Integer.toString(primaryKey)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
                return 1;
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_ARTICLES, null, values);
                db.setTransactionSuccessful();
                return 2;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
            return 0;
        } finally {
            db.endTransaction();
        }
    }

    public boolean deleteEntry (Entry entry) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_ARTICLES, KEY_ENTRY_ID + "= ?",
                    new String[]{Integer.toString(entry.getId())});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete entry from db");
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public Entry getSingleEntry(int key) {
        String ENTRY_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_ARTICLES, KEY_ENTRY_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ENTRY_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_ID));
                String type = cursor.getString(cursor.getColumnIndex(KEY_TYPE));
                String name = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_DATE_OF_LAST_EDIT));
                String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_LESSON_LEARNED));

                return new Entry(id, type, name, dateOfLastEdit, description, lessonLearned);
            } else {
                throw new Exception("No entries returned from database!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during database querry...");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public ArrayList<Entry> getMultipleEntries(String type) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        String ENTRY_SELECT_QUERY;
        if (type.equals("Alles")) {
            ENTRY_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_ARTICLES);
        } else {
            ENTRY_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_ARTICLES, KEY_TYPE, type);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ENTRY_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_ID));
                    String name = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                    String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_DATE_OF_LAST_EDIT));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                    String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_LESSON_LEARNED));

                    entries.add(
                            new Entry(id, type, name, dateOfLastEdit, description, lessonLearned));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting entries from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return entries;
    }
}
