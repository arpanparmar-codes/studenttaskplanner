
package com.example.studenttaskplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Taskdb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_tasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tasks";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_NOTES = "notes";
    public static final String COL_DUE_DATE = "due_date";
    public static final String COL_DONE = "is_done";

    public Taskdb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_NOTES + " TEXT, " +
                COL_DUE_DATE + " TEXT, " +
                COL_DONE + " INTEGER DEFAULT 0)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // Add database changes here if needed later.
    }

    public long addTask(String title, String notes, String dueDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_NOTES, notes);
        values.put(COL_DUE_DATE, dueDate);
        values.put(COL_DONE, 0);

        return db.insert(TABLE_TASKS, null, values);
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TASKS,
                null,
                COL_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_ID));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_TITLE));

                String notes = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_NOTES));

                String dueDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_DUE_DATE));

                int done = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_DONE));

                return new Task(
                        id, title, notes, dueDate, done == 1);
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    public int setTaskDone(int id, boolean done) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DONE, done ? 1 : 0);

        return db.update(
                TABLE_TASKS,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteTask(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_TASKS,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public static class Task {
        public final int id;
        public final String title;
        public final String notes;
        public final String dueDate;
        public final boolean done;

        public Task(int id, String title, String notes,
                    String dueDate, boolean done) {
            this.id = id;
            this.title = title;
            this.notes = notes;
            this.dueDate = dueDate;
            this.done = done;
        }
    }
}
