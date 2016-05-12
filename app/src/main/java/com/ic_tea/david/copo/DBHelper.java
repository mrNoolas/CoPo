/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{
    private static final String TAG = DBHelper.class.getSimpleName();
    // Database info
    private static final String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 4;

    // table names
    private static final String TABLE_PROJECTS = "projects";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_LOGS = "articles";
    private static final String TABLE_PORTFOLIOS = "portfolios";
    private static final String TABLE_GOALS = "goals";

    // projects columns
    private static final String KEY_PROJECT_ID = "id";
    private static final String KEY_PROJECT_TITLE = "title";
    private static final String KEY_PROJECT_DESCR = "description";

    // ADD: columns for table categories

    // logs table columns
    private static final String KEY_LOG_ID = "id";
    private static final String KEY_LOG_PROJECT_ID = "projectid"; // The project the log belongs to
    private static final String KEY_LOG_TITLE = "title";
    private static final String KEY_LOG_DATE = "date";
    private static final String KEY_LOG_DESCR = "description";
    private static final String KEY_LOG_ACHIEVEMENTS = "achievements";

    // portfolios columns
    private static final String KEY_PORTFOLIO_ID = "id";
    private static final String KEY_PORTFOLIO_PROJECT_ID = "projectid"; // The project the log belongs to
    private static final String KEY_PORTFOLIO_CATEGORY_ID = "categoryid";
    private static final String KEY_PORTFOLIO_TITLE = "title";
    private static final String KEY_PORTFOLIO_DATE = "date";
    private static final String KEY_PORTFOLIO_DESCR = "description";
    private static final String KEY_PORTFOLIO_ACHIEVEMENTS = "achievements";
    private static final String KEY_PORTFOLIO_LESSON_LEARNED = "lessonLearned";
    private static final String KEY_PORTFOLIO_NEXT_TIME = "whattododifferentlynexttime";


    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
         // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
          instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ADD: all tables to database
        String CREATE_ENTRIES_TABLE = "CREATE TABLE " + TABLE_LOGS +
                "(" +
                KEY_LOG_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_LOG_PROJECT_ID + " INTEGER," +
                KEY_LOG_TITLE + " TEXT," +
                KEY_LOG_DATE + " TEXT," + // DDMMYYYYHHMM
                KEY_LOG_DESCR + " TEXT," +
                KEY_LOG_ACHIEVEMENTS + " TEXT" +
                ")";

        db.execSQL(CREATE_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: when the database is updated, update table instead of recreating it...
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
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
            values.put(KEY_LOG_PROJECT_ID, entry.getType());
            values.put(KEY_LOG_TITLE, entry.getName());
            values.put(KEY_LOG_DATE, entry.getDateOfLastEdit());
            values.put(KEY_LOG_DESCR, entry.getDescription());
            values.put(KEY_LOG_ACHIEVEMENTS, entry.getLessonLearned());

            // First try to update the entry
            int rows = 0;
            if (entry.getId() != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_LOGS, values,
                        KEY_LOG_ID + "= ?", new String[]{Integer.toString(primaryKey)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
                return 1;
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_LOGS, null, values);
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
            db.delete(TABLE_LOGS, KEY_LOG_ID + "= ?",
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
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LOGS, KEY_LOG_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ENTRY_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_LOG_ID));
                int type = cursor.getInt(cursor.getColumnIndex(KEY_LOG_PROJECT_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_LOG_TITLE));
                String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE));
                String description = cursor.getString(cursor.getColumnIndex(KEY_LOG_DESCR));
                String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_LOG_ACHIEVEMENTS));

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

    public ArrayList<Entry> getMultipleEntries(int type) {
        ArrayList<Entry> entries = new ArrayList<>();

        String ENTRY_SELECT_QUERY;
        if (type == 0) {
            ENTRY_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_LOGS);
        } else {
            ENTRY_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LOGS, KEY_LOG_PROJECT_ID, type);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ENTRY_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_LOG_ID));
                    String name = cursor.getString(cursor.getColumnIndex(KEY_LOG_TITLE));
                    int personalType = cursor.getInt(cursor.getColumnIndex(KEY_LOG_PROJECT_ID));
                    String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_LOG_DESCR));
                    String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_LOG_ACHIEVEMENTS));

                    entries.add(
                            new Entry(id, personalType, name,
                                    dateOfLastEdit, description, lessonLearned));
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
