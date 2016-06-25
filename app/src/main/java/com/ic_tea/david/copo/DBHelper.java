/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.Competence;
import com.ic_tea.david.copo.objects.EduLevel;
import com.ic_tea.david.copo.objects.Goal;
import com.ic_tea.david.copo.objects.PortfolioItem;
import com.ic_tea.david.copo.objects.Project;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{
    private static final String TAG = DBHelper.class.getSimpleName();
    // Database info
    private static final String DATABASE_NAME = "CoPoDatabaseOfAwesomeness";
    private static final int DATABASE_VERSION = 18;

    // table names
    private static final String TABLE_LEVELS = "levels"; // this table has all the possible user levels
    private static final String TABLE_COMPETENCES = "competences";
    //private static final String TABLE_LEVELS_COMPETENCES = "levelsCompetences"; // linking table
    private static final String TABLE_PROJECTS = "projects";
    private static final String TABLE_LOGS = "articles";
    private static final String TABLE_PORTFOLIOS = "portfolios";
    private static final String TABLE_GOALS = "goals";


    // levels columns
    private static final String KEY_LEVEL_ID = "levelId";
    private static final String KEY_LEVEL_NAME = "name";
    private static final String KEY_LEVEL_DESCR = "description";

    // competence columns
    private static final String KEY_COMPETENCE_ID = "categoryId";
    private static final String KEY_COMPETENCE_TITLE = "title";
    //private static final String KEY_COMPETENCE_DESCR = "description";

    /*/ levelsCompetences columns (linking table)
    private static final String KEY_L_C_ID = "LevelCompetenceId";
    private static final String KEY_L_C_LEVEL_ID = "levelId"; // foreign key
    private static final String KEY_L_C_COMPETENCE_ID = "competenceId"; // foreign key*/

    // projects columns
    private static final String KEY_PROJECT_ID = "projectId";
    private static final String KEY_PROJECT_TITLE = "title";
    private static final String KEY_PROJECT_DESCR = "description";

    // ADD: column which records the amount of time spent on a certain action
    // logs columns
    private static final String KEY_LOG_ID = "logId";
    private static final String KEY_LOG_PROJECT_ID = "projectId"; // foreign key
    private static final String KEY_LOG_DATE_OF_LAST_EDIT = "dateOfLastAction";
    private static final String KEY_LOG_TITLE = "title";
    private static final String KEY_LOG_DATE_OF_ACTION = "dateOfAction";
    private static final String KEY_LOG_HOURS_SPENT = "hoursSpent";
    private static final String KEY_LOG_DESCR = "description";
    private static final String KEY_LOG_ACHIEVEMENTS = "achievements";

    // portfolios columns
    private static final String KEY_PORTFOLIO_ID = "portfolioId";
    private static final String KEY_PORTFOLIO_PROJECT_ID = "projectid"; // foreign key
    private static final String KEY_PORTFOLIO_COMPETENCE_ID = "categoryid"; // foreign key
    private static final String KEY_PORTFOLIO_DATE_Of_LAST_EDIT = "DOLE";
    private static final String KEY_PORTFOLIO_TITLE = "title";
    private static final String KEY_PORTFOLIO_DATE_OF_ACTION = "dateOfAction";
    private static final String KEY_PORTFOLIO_HOURS_SPENT = "hoursSpent";
    private static final String KEY_PORTFOLIO_DESCR = "description";
    private static final String KEY_PORTFOLIO_ACHIEVEMENTS = "achievements";
    private static final String KEY_PORTFOLIO_LESSON_LEARNED = "lessonLearned";
    private static final String KEY_PORTFOLIO_NEXT_TIME = "whatToDoDifferentlyNextTime";

    // goals columns
    private static final String KEY_GOAL_ID = "goalId";
    private static final String KEY_GOAL_PARENT_ID = "goalParentId"; // foreign key
    private static final String KEY_GOAL_PROJECT_ID = "projectId"; // foreign key
    private static final String KEY_GOAL_TITLE = "title";
    private static final String KEY_GOAL_DESCR = "description";
    private static final String KEY_GOAL_DUE_DATE = "dueDate";
    private static final String KEY_GOAL_IS_FINISHED = "isFinished";


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
        db.execSQL("CREATE TABLE " + TABLE_LEVELS +
                "(" +
                KEY_LEVEL_ID + " INTEGER PRIMARY KEY," +
                KEY_LEVEL_NAME + " TEXT," +
                KEY_LEVEL_DESCR + " TEXT" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_COMPETENCES +
                "(" +
                KEY_COMPETENCE_ID + " INTEGER PRIMARY KEY," +
                KEY_COMPETENCE_TITLE + " TEXT" + //"," +
                //KEY_COMPETENCE_DESCR + " TEXT" +
                ")");

        /*db.execSQL("CREATE TABLE " + TABLE_LEVELS_COMPETENCES +
                "(" +
                KEY_L_C_ID + " INTEGER PRIMARY KEY," +
                KEY_L_C_LEVEL_ID + " INTEGER," +
                KEY_L_C_COMPETENCE_ID + " INTEGER," +

                "FOREIGN KEY(" + KEY_L_C_LEVEL_ID + ") REFERENCES " +
                TABLE_LEVELS + "(" + KEY_LEVEL_ID + ")," +
                "FOREIGN KEY(" + KEY_L_C_COMPETENCE_ID + ") REFERENCES " +
                TABLE_COMPETENCES + "(" + KEY_COMPETENCE_ID + ")" +
                ")");*/

        db.execSQL("CREATE TABLE " + TABLE_PROJECTS +
                "(" +
                KEY_PROJECT_ID + " INTEGER PRIMARY KEY," +
                KEY_PROJECT_TITLE + " TEXT," +
                KEY_PROJECT_DESCR + " TEXT" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_LOGS +
                "(" +
                KEY_LOG_ID + " INTEGER PRIMARY KEY," +
                KEY_LOG_PROJECT_ID + " INTEGER NOT NULL," +
                KEY_LOG_DATE_OF_LAST_EDIT + " TEXT," + // DDMMYYYYHHMM
                KEY_LOG_TITLE + " TEXT," +
                KEY_LOG_DATE_OF_ACTION + " TEXT," + // DDMMYYYY
                KEY_LOG_HOURS_SPENT + " REAL," +
                KEY_LOG_DESCR + " TEXT," +
                KEY_LOG_ACHIEVEMENTS + " TEXT," +

                "FOREIGN KEY(" + KEY_LOG_PROJECT_ID + ") REFERENCES " +
                TABLE_PROJECTS + "(" + KEY_PROJECT_ID + ")" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_PORTFOLIOS +
                "(" +
                KEY_PORTFOLIO_ID + " INTEGER PRIMARY KEY," +
                KEY_PORTFOLIO_PROJECT_ID + " INTEGER NOT NULL," +
                KEY_PORTFOLIO_COMPETENCE_ID + " INTEGER NOT NULL," +
                KEY_PORTFOLIO_DATE_Of_LAST_EDIT + " TEXT," + // DDMMYYYYHHMM
                KEY_PORTFOLIO_TITLE + " TEXT," +
                KEY_PORTFOLIO_DATE_OF_ACTION + " TEXT," + // DDMMYYYY
                KEY_LOG_HOURS_SPENT + " REAL," +
                KEY_PORTFOLIO_DESCR + " TEXT," +
                KEY_PORTFOLIO_ACHIEVEMENTS + " TEXT," +
                KEY_PORTFOLIO_LESSON_LEARNED + " TEXT," +
                KEY_PORTFOLIO_NEXT_TIME + " TEXT," +

                "FOREIGN KEY(" + KEY_PORTFOLIO_PROJECT_ID + ") REFERENCES " +
                TABLE_PROJECTS + "(" + KEY_PROJECT_ID + ")," +
                "FOREIGN KEY(" + KEY_PORTFOLIO_COMPETENCE_ID + ") REFERENCES " +
                TABLE_COMPETENCES + "(" + KEY_COMPETENCE_ID + ")" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_GOALS +
                "(" +
                KEY_GOAL_ID + " INTEGER PRIMARY KEY," +
                KEY_GOAL_PARENT_ID + " INTEGER," + // ID from another goal, null if primary goal
                KEY_GOAL_PROJECT_ID + " INTEGER NOT NULL," +
                KEY_GOAL_TITLE + " TEXT," +
                KEY_GOAL_DESCR + " TEXT," +
                KEY_GOAL_DUE_DATE + " TEXT," + // DDMMYYYYHHMM
                KEY_GOAL_IS_FINISHED + " INTEGER," +

                "FOREIGN KEY(" + KEY_GOAL_PROJECT_ID + ") REFERENCES " +
                TABLE_PROJECTS + "(" + KEY_PROJECT_ID + ")," +
                "FOREIGN KEY(" + KEY_GOAL_PARENT_ID + ") REFERENCES " +
                TABLE_GOALS + "(" + KEY_GOAL_ID + ")" +
                ")");

        addAllDefaults(db);
    }

    private void addAllDefaults(SQLiteDatabase db) {
        // ADD: Defaults

        // Levels
        // CHANGE: description of the levels
        addOrUpdateLevel(new EduLevel("Basis", "Het eerst te behalen niveau voor nieuwe leerlingen, die weinig ervaring hebben met projectmatig"), db);
        addOrUpdateLevel(new EduLevel("Master", "Tweede level"), db);
        addOrUpdateLevel(new EduLevel("Expert", "Derde level"), db);

        // Competences
        addOrUpdateCompetence(new Competence("Motivatie"), db);
        addOrUpdateCompetence(new Competence("Zelfbeeld"), db);
        addOrUpdateCompetence(new Competence("Houding"), db);
        addOrUpdateCompetence(new Competence("Vaardigheden"), db);

        // Dummy project
        addOrUpdateProject(new Project("Test project", "Dit is een test-project"), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: when the database is updated, update table instead of recreating it...
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            // delete from bottom to top
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PORTFOLIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPETENCES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);

            onCreate(db);
        }
    }

    // ADD: all the interaction functions for the tables
    public void addOrUpdateLevel(EduLevel level) {
        SQLiteDatabase db = getWritableDatabase();
        addOrUpdateLevel(level, db);
    }
    public void addOrUpdateLevel(EduLevel level, SQLiteDatabase db) {
        // Open db
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LEVEL_NAME, level.name);
            values.put(KEY_LEVEL_DESCR, level.descr);

            // First try to update the entry
            int rows = 0;
            if (level.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_LEVELS, values,
                        KEY_LEVEL_ID + "= ?", new String[]{Integer.toString(level.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_LEVELS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public EduLevel getLevel(int key) {
        String LEVEL_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LEVELS, KEY_LEVEL_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LEVEL_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_LEVEL_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_LEVEL_NAME));
                String description = cursor.getString(cursor.getColumnIndex(KEY_LEVEL_NAME));

                return new EduLevel(id, name, description);
            } else {
                throw new Exception("No levels returned from database!");
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

    public void addOrUpdateCompetence(Competence competence) {
        SQLiteDatabase db = getWritableDatabase();
        addOrUpdateCompetence(competence, db);
    }
    public void addOrUpdateCompetence(Competence competence, SQLiteDatabase db) {
        // Open db
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COMPETENCE_TITLE, competence.title);
            //values.put(KEY_COMPETENCE_DESCR, competence.descr);

            // First try to update the entry
            int rows = 0;
            if (competence.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_COMPETENCES, values,
                        KEY_COMPETENCE_ID + "= ?", new String[]{Integer.toString(competence.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_COMPETENCES, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public Competence getCompetence(int key) {
        String LEVEL_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LEVELS, KEY_LEVEL_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LEVEL_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_COMPETENCE_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_TITLE));
                //String description = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_DESCR));

                return new Competence(id, title);
            } else {
                throw new Exception("No levels returned from database!");
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
    public ArrayList<Competence> getMultipleCompetences(int type) {
        ArrayList<Competence> competences = new ArrayList<>();
        String COMPETENCE_SELECT_QUERY;

        if (type == 0) {
            COMPETENCE_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_COMPETENCES);
        } else {
            COMPETENCE_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_COMPETENCES,
                            KEY_COMPETENCE_ID, type);
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(COMPETENCE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_COMPETENCE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_TITLE));
                    //String description = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_DESCR));

                    competences.add(new Competence(id, title));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting competences from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return competences;
    }

    public void addOrUpdateProject (Project project) {
        // Open db
        SQLiteDatabase db = getWritableDatabase();
        addOrUpdateProject(project, db);
    }
    public void addOrUpdateProject (Project project, SQLiteDatabase db) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROJECT_TITLE, project.title);
            values.put(KEY_PROJECT_DESCR, project.descr);

            // First try to update the entry
            int rows = 0;
            if (project.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_PROJECTS, values,
                        KEY_PROJECT_ID + "= ?", new String[]{Integer.toString(project.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_PROJECTS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public Project getProject (int key) {
        String PROJECT_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'",
                        TABLE_PROJECTS, KEY_PROJECT_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PROJECT_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DESCR));

                return new Project(id, title, description);
            } else {
                throw new Exception("No projects returned from database!");
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
    public void deleteProject (int projectId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_PROJECTS, KEY_PROJECT_ID + "= ?",
                    new String[]{Integer.toString(projectId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete project from db");
        } finally {
            db.endTransaction();
        }
    }
    public ArrayList<Project> getMultipleProjects(int type) {
        SQLiteDatabase db = getReadableDatabase();
        return getMultipleProjects(type, db);
    }
    public ArrayList<Project> getMultipleProjects(int type, SQLiteDatabase db) {
        ArrayList<Project> projects = new ArrayList<>();
        String PROJECT_SELECT_QUERY;

        if (type == 0) {
            PROJECT_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_PROJECTS);
        } else {
            PROJECT_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_PROJECTS, KEY_PROJECT_ID, type);
        }


        Cursor cursor = db.rawQuery(PROJECT_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DESCR));

                    projects.add(new Project(id, title, description));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting projects from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return projects;
    }
    public double getHoursSpentOnProject(int id) {
        SQLiteDatabase db = getReadableDatabase();
        return getHoursSpentOnProject(id, db);
    }
    public double getHoursSpentOnProject(int id, SQLiteDatabase db) {
        double hoursSpent = 0;

        // FIXME: possible double count of hours due to similarities between log- and portfolio-entries
        // This is now (hopefully temporarily) fixed by only counting portfolio hours
        String LOG_SELECT_QUERY;
        if (id == 0) {
            LOG_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_LOGS);
        } else {
            LOG_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = '%s'",
                    TABLE_LOGS, KEY_LOG_PROJECT_ID, id);
        }
        Cursor cursor = db.rawQuery(LOG_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    hoursSpent += cursor.getDouble(cursor.getColumnIndex(KEY_LOG_HOURS_SPENT));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting logs from db");
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return hoursSpent;
    }

    public void addOrUpdateLog (ActionLog log) {
        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LOG_PROJECT_ID, log.projectId);
            values.put(KEY_LOG_DATE_OF_LAST_EDIT, log.DOLE);
            values.put(KEY_LOG_TITLE, log.title);
            values.put(KEY_LOG_DATE_OF_ACTION, log.date);
            values.put(KEY_LOG_HOURS_SPENT, log.hoursSpent);
            values.put(KEY_LOG_DESCR, log.descr);
            values.put(KEY_LOG_ACHIEVEMENTS, log.achievements);

            // First try to update the entry
            int rows = 0;
            if (log.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_LOGS, values,
                        KEY_LOG_ID + "= ?", new String[]{Integer.toString(log.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_LOGS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public void deleteLog (ActionLog log) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_LOGS, KEY_LOG_ID + "= ?", new String[]{Integer.toString(log.id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete entry from db");
        } finally {
            db.endTransaction();
        }
    }
    public ActionLog getSingleLog (int key) {
        String ENTRY_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LOGS, KEY_LOG_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ENTRY_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_LOG_ID));
                int projectId = cursor.getInt(cursor.getColumnIndex(KEY_LOG_PROJECT_ID));
                String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_LAST_EDIT));
                String title = cursor.getString(cursor.getColumnIndex(KEY_LOG_TITLE));
                String dateOfAction = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_ACTION));
                double hoursSpent = cursor.getDouble(cursor.getColumnIndex(KEY_LOG_HOURS_SPENT));
                String description = cursor.getString(cursor.getColumnIndex(KEY_LOG_DESCR));
                String achievements = cursor.getString(cursor.getColumnIndex(KEY_LOG_ACHIEVEMENTS));

                return new ActionLog(id, projectId, dateOfLastEdit, title, dateOfAction, hoursSpent,
                        description, achievements);
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
    public ArrayList<ActionLog> getMultipleLogs (int projectKey) {
        SQLiteDatabase db = getReadableDatabase();
        return getMultipleLogs(projectKey, db);
    }
    public ArrayList<ActionLog> getMultipleLogs (int projectKey, SQLiteDatabase db) {
        ArrayList<ActionLog> logs = new ArrayList<>();

        String LOG_SELECT_QUERY;
        if (projectKey <= 0) {
            LOG_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_LOGS);
        } else {
            LOG_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'",
                            TABLE_LOGS, KEY_LOG_PROJECT_ID, projectKey);
        }

        Cursor cursor = db.rawQuery(LOG_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_LOG_ID));
                    int projectId = cursor.getInt(cursor.getColumnIndex(KEY_LOG_PROJECT_ID));
                    String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_LAST_EDIT));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_LOG_TITLE));
                    String dateOfAction = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_ACTION));
                    double hoursSpent = cursor.getDouble(cursor.getColumnIndex(KEY_LOG_HOURS_SPENT));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_LOG_DESCR));
                    String achievements = cursor.getString(cursor.getColumnIndex(KEY_LOG_ACHIEVEMENTS));

                    logs.add(new ActionLog(id, projectId, dateOfLastEdit, title, dateOfAction,
                            hoursSpent, description, achievements));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting logs from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return logs;
    }

    public void addOrUpdatePortfolioItem (PortfolioItem pItem) {
        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PORTFOLIO_PROJECT_ID, pItem.projectId);
            values.put(KEY_PORTFOLIO_COMPETENCE_ID, pItem.competenceId);
            values.put(KEY_PORTFOLIO_DATE_Of_LAST_EDIT, pItem.DOLE);
            values.put(KEY_PORTFOLIO_TITLE, pItem.title);
            values.put(KEY_PORTFOLIO_DATE_OF_ACTION, pItem.date);
            values.put(KEY_PORTFOLIO_HOURS_SPENT, pItem.hoursSpent);
            values.put(KEY_PORTFOLIO_DESCR, pItem.descr);
            values.put(KEY_PORTFOLIO_ACHIEVEMENTS, pItem.achievements);
            values.put(KEY_PORTFOLIO_LESSON_LEARNED, pItem.lessonLearned);
            values.put(KEY_PORTFOLIO_NEXT_TIME, pItem.nextTime);

            // First try to update the entry
            int rows = 0;
            if (pItem.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_PORTFOLIOS, values,
                        KEY_PORTFOLIO_ID + "= ?", new String[]{Integer.toString(pItem.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_PORTFOLIOS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public boolean deletePortfolioItem (PortfolioItem pItem) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_PORTFOLIOS, KEY_PORTFOLIO_ID + "= ?",
                    new String[]{Integer.toString(pItem.id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete portfolio item from db");
            return false;
        } finally {
            db.endTransaction();
        }
    }
    public PortfolioItem getSinglePortfolioEntry (int key) {
        String PORTFOLIO_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TABLE_PORTFOLIOS, KEY_PORTFOLIO_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PORTFOLIO_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_ID));
                int projectId = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_PROJECT_ID));
                int competenceId = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_COMPETENCE_ID));
                String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DATE_Of_LAST_EDIT));
                String title = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DATE_OF_ACTION));
                double hoursSpent = cursor.getDouble(cursor.getColumnIndex(KEY_PORTFOLIO_HOURS_SPENT));
                String description = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DESCR));
                String achievements = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_ACHIEVEMENTS));
                String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_LESSON_LEARNED));
                String nextTime = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_NEXT_TIME));

                return new PortfolioItem(id, projectId, competenceId, dateOfLastEdit, title, date,
                        hoursSpent, description, achievements, lessonLearned, nextTime);
            } else {
                throw new Exception("No portfolio items returned from database!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during database query...");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }
    public ArrayList<PortfolioItem> getMultiplePortfolioEntries (int projectId) {
        SQLiteDatabase db = getReadableDatabase();
        return getMultiplePortfolioEntries(projectId, db);
    }
    public ArrayList<PortfolioItem> getMultiplePortfolioEntries (int projectId, SQLiteDatabase db) {
        ArrayList<PortfolioItem> portfolioItems = new ArrayList<>();

        String PORTFOLIOS_SELECT_QUERY;
        if (projectId == 0) {
            PORTFOLIOS_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_PORTFOLIOS);
        } else {
            PORTFOLIOS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_PORTFOLIOS, KEY_PORTFOLIO_PROJECT_ID, projectId);
        }

        Cursor cursor = db.rawQuery(PORTFOLIOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_ID));
                    int pId = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_PROJECT_ID));
                    int competenceId = cursor.getInt(cursor.getColumnIndex(KEY_PORTFOLIO_COMPETENCE_ID));
                    String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DATE_Of_LAST_EDIT));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_TITLE));
                    String date = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DATE_OF_ACTION));
                    double hoursSpent = cursor.getDouble(cursor.getColumnIndex(KEY_PORTFOLIO_HOURS_SPENT));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_DESCR));
                    String achievements = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_ACHIEVEMENTS));
                    String lessonLearned = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_LESSON_LEARNED));
                    String nextTime = cursor.getString(cursor.getColumnIndex(KEY_PORTFOLIO_NEXT_TIME));

                    portfolioItems.add(new PortfolioItem(id, pId, competenceId,  dateOfLastEdit,
                            title, date, hoursSpent, description, achievements, lessonLearned, nextTime));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting portfolioItems from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return portfolioItems;
    }

    public void addOrUpdateGoal (Goal goal) {
        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_GOAL_PARENT_ID, goal.parentId);
            values.put(KEY_GOAL_PROJECT_ID, goal.projectId);
            values.put(KEY_GOAL_TITLE, goal.title);
            values.put(KEY_GOAL_DESCR, goal.description);
            values.put(KEY_GOAL_DUE_DATE, goal.dueDate);
            values.put(KEY_GOAL_IS_FINISHED, goal.getFinishedInt());

            // First try to update the entry
            int rows = 0;
            if (goal.id != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_GOALS, values,
                        KEY_GOAL_ID + "= ?", new String[]{Integer.toString(goal.id)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_LOGS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add a goal to db");
        } finally {
            db.endTransaction();
        }
    }
    public boolean deleteGoal (Goal goal) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_GOALS, KEY_GOAL_ID + "= ?",
                    new String[]{Integer.toString(goal.id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete goal from db");
            return false;
        } finally {
            db.endTransaction();
        }
    }
    public Goal getSingleGoal (int key) {
        String GOAL_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_GOALS, KEY_GOAL_ID, key);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GOAL_SELECT_QUERY, null);
        if (cursor.getCount() > 1) { Log.e(TAG, "more than one row is associated with a key");}
        try {
            if (cursor.moveToFirst()) {
                // This is not a efficient way of querrying (use predetermined index) but the db is small so, not huge deal...
                int id = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_ID));
                int parentId = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_PARENT_ID));
                int projectId = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_PROJECT_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_GOAL_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(KEY_GOAL_DESCR));
                String dueDate = cursor.getString(cursor.getColumnIndex(KEY_GOAL_DUE_DATE));
                int isfinished = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_IS_FINISHED));

                return new Goal(id, parentId, projectId, title, description, dueDate, isfinished);
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
    public ArrayList<Goal> getMultipleGoals (int type) {
        ArrayList<Goal> goals = new ArrayList<>();

        String GOAL_SELECT_QUERY;
        if (type == 0) {
            GOAL_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_GOALS);
        } else {
            GOAL_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_GOALS, KEY_GOAL_ID, type);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GOAL_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_ID));
                    int parentId = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_PARENT_ID));
                    int projectId = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_PROJECT_ID));
                    String title = cursor.getString(cursor.getColumnIndex(KEY_GOAL_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_GOAL_DESCR));
                    String dueDate = cursor.getString(cursor.getColumnIndex(KEY_GOAL_DUE_DATE));
                    int isfinished = cursor.getInt(cursor.getColumnIndex(KEY_GOAL_IS_FINISHED));

                    goals.add(new Goal(id, parentId, projectId, title, description, dueDate, isfinished));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while getting goals from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return goals;
    }

    public int getPoints(int type) {
        int points = 0;
        int logWeight = 10;
        int portfolioWeight = 15;
        int hourWeight = 1;

        switch (type) {
            case 0: // only logs
                points += (getMultipleLogs(0).size()) * logWeight;
                break;
            case 1: // only portfolio's
                points += getMultiplePortfolioEntries(0).size() * portfolioWeight;
                break;
            case 2: // portfolios and logs
                points += getMultiplePortfolioEntries(0).size() * portfolioWeight;
                points += getMultipleLogs(0).size() * logWeight;
                break;
            case 3: // portfolios, logs and hours
                points += getMultiplePortfolioEntries(0).size() * portfolioWeight;
                points += getMultipleLogs(0).size() * logWeight;
                points += getHoursSpentOnProject(0) * hourWeight; // all hours spent
                break;
            default:
                points = -1;
                break;
        }

        return points;
    }

    /*public void addOrUpdateLevelToCompetenceRelation(int key, Competence competence, EduLevel level) {
        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_L_C_LEVEL_ID, level.id);
            values.put(KEY_L_C_COMPETENCE_ID, competence.id);

            // First try to update the entry
            int rows = 0;
            if (key != -1) { // when the id is -1, it has been manually set.
                rows = db.update(TABLE_LEVELS_COMPETENCES, values,
                        KEY_L_C_ID + "= ?", new String[]{Integer.toString(key)});
            }
            if (rows == 1) {
                db.setTransactionSuccessful();
            } else {
                // insert new entry
                db.insertOrThrow(TABLE_LEVELS_COMPETENCES, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add an entry to db");
        } finally {
            db.endTransaction();
        }
    }
    public ArrayList<Competence> getCompetencesFromLevel(EduLevel level) {
        ArrayList<Competence> competences = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        if (level == null) {
            // return all
            String COMPETENCE_SELECT_QUERY =
                    String.format("SELECT * FROM %s", TABLE_COMPETENCES);

            Cursor cursor = db.rawQuery(COMPETENCE_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(KEY_COMPETENCE_ID));
                        String title = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_TITLE));
                        String description = cursor.getString(cursor.getColumnIndex(KEY_COMPETENCE_DESCR));

                        competences.add(new Competence(id, title, description));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while getting competences from db");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } else {
            // First get the Competence id's
            String COMPETENCE_ID_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_LEVELS_COMPETENCES,
                            KEY_L_C_LEVEL_ID, level.id);

            ArrayList<Integer> competenceIds = new ArrayList<>();
            Cursor cursor = db.rawQuery(COMPETENCE_ID_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        competenceIds.add(cursor.getInt(cursor.getColumnIndex(KEY_L_C_COMPETENCE_ID)));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while getting competences from db");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }

            for(int key : competenceIds) {
                competences.add(getCompetence(key));
            }
        }
        return competences;
    }*/


    // default methods
    /*
    public int addOrUpdateEntry (Entry entry) {
        /* returns:
         * 0 - failed / exception
         * 1 - successful -> updated existing entry
         * 2 - successful -> inserted new entry
         *

        // Open db
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            int primaryKey = entry.getId();
            ContentValues values = new ContentValues();
            values.put(KEY_LOG_PROJECT_ID, entry.getType());
            values.put(KEY_LOG_TITLE, entry.getName());
            values.put(KEY_LOG_DATE_OF_ACTION, entry.getDateOfLastEdit());
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
                String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_ACTION));
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
                    String dateOfLastEdit = cursor.getString(cursor.getColumnIndex(KEY_LOG_DATE_OF_ACTION));
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
    } */
}
