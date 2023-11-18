package com.example.mytodolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mytodolist.models.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context context;
    private static final String DATABASE_NAME = "db_todoapp";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE IF NOT EXISTS Categories(" +
                        "id_category INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL UNIQUE, " +
                        "color TEXT NOT NULL DEFAULT \"#FFFFFF\")";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS Tasks(" +
                "id_task INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "category_id INTEGER REFERENCES Categories(id_category) ON DELETE CASCADE, " +
                "title TEXT NOT NULL, " +
                "text TEXT, " +
                "deadline TEXT NOT NULL DEFAULT current_timestamp, " +
                "is_done INTEGER NOT NULL DEFAULT 0 CHECK (is_done IN(0, 1)), " +
                "priority INTEGER NOT NULL DEFAULT 3 CHECK (priority IN(1, 2, 3, 4, 5)))";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS Subtasks(" +
                "id_subtask INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "task_id INTEGER NOT NULL REFERENCES Tasks(id_task) ON DELETE CASCADE, " +
                "title TEXT NOT NULL, " +
                "text , " +
                "is_done INTEGER NOT NULL DEFAULT 0 CHECK (is_done IN(0, 1)))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Subtasks");
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Categories");
        onCreate(db);
    }

    public void insertTask(String title) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);

        long result = db.insert("Tasks", null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New task create!", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTask(String title, String text) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("text", text);

        long result = db.insert("Tasks", null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New task create!", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTask(String title, String text, int priority) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("text", text);
        cv.put("priority", priority);

        long result = db.insert("Tasks", null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New task create!", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTask(int categoryId, String title, String text, Date deadline, boolean isDone, int priority) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        cv.put("category_id", categoryId);
        cv.put("title", title);
        cv.put("text", text);
        cv.put("deadline", formatter.format(deadline));
        cv.put("is_done", isDone ? 1 : 0);
        cv.put("priority", priority);
        long result = db.insert("Tasks", null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New task create!", Toast.LENGTH_SHORT).show();
        }
    }

    public List<TaskModel> readTasks() {
        String query = "SELECT * FROM Tasks";
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        List<TaskModel> taskList = new ArrayList<>();
        if (cursor == null) return taskList;
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                TaskModel task = new TaskModel();
                task.setIdTask(cursor.getInt(cursor.getColumnIndexOrThrow("id_task")));
                task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setText(cursor.getString(cursor.getColumnIndexOrThrow("text")));
                task.setDeadline(cursor.getString(cursor.getColumnIndexOrThrow("deadline")));
                task.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) > 0);
                task.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow("priority")));
                taskList.add(task);
            }
        }
        cursor.close();
        return taskList;
    }

    public void updateTask(int id, String title, String text) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("text", text);

        long result = db.update("Tasks", cv, "id_task=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTask(int id, String title, String text, int priority) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("text", text);
        cv.put("priority", priority);

        long result = db.update("Tasks", cv, "id_task=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Tasks", "id_task=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
