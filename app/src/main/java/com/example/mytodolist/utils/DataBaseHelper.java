package com.example.mytodolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytodolist.R;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.models.SubtaskModel;
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
                        "id_category INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL UNIQUE, " +
                        "color TEXT NOT NULL DEFAULT \"#000000\")";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS Tasks(" +
                "id_task INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "category_id INTEGER REFERENCES Categories(id_category) ON DELETE CASCADE, " +
                "title TEXT NOT NULL, " +
                "text TEXT, " +
                "deadline TEXT NOT NULL DEFAULT current_timestamp, " +
                "is_done INTEGER NOT NULL DEFAULT 0 CHECK (is_done IN(0, 1)), " +
                "priority INTEGER NOT NULL DEFAULT 3 CHECK (priority IN(1, 2, 3, 4, 5)))";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS Subtasks(" +
                "id_subtask INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "task_id INTEGER NOT NULL REFERENCES Tasks(id_task) ON DELETE CASCADE, " +
                "title TEXT NOT NULL, " +
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

    public void insertTask(String title) { this.insertTask(0, title, null, new Date(), false, 3); }

    public void insertTask(String title, String text)
    { this.insertTask(0, title, text, new Date(), false, 3); }

    public void insertTask(String title, String text, int priority)
    { this.insertTask(0, title, text, new Date(), false, priority); }

    public void insertTask(int categoryId, String title, String text, int priority)
    { this.insertTask(categoryId, title, text, new Date(), false, priority); }

    public void insertTask(int categoryId, String title, String text, Date deadline , boolean isDone, int priority) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (categoryId != 0) cv.put("category_id", categoryId);
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

    public void insertSubtask(int taskId, String title) { this.insertSubtask(taskId, title, false); }

    public void insertSubtask(int taskId, String title, boolean isDone) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("task_id", taskId);
        cv.put("title", title);
        cv.put("is_done", isDone ? 1 : 0);

        long result = db.insert("Subtasks", null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New subtask create!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void insertCategory( String name)
    { this.insertCategory(name, Color.valueOf(Color.BLACK)); }

    public void insertCategory(String name, Color color)  {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("color", String.format("#%06X", (0xFFFFFF & color.toArgb())));

        long result = db.insert("Categories", null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "New category create!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public List<TaskModel> readTasks() {
        String query = "SELECT * FROM Tasks LEFT JOIN Categories on category_id = id_category";
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        List<TaskModel> tasksList = new ArrayList<>();
        if (cursor == null) return tasksList;
        if (cursor.moveToFirst()) {
             do {
                TaskModel task = new TaskModel();
                task.setIdTask(cursor.getInt(cursor.getColumnIndexOrThrow("id_task")));
                task.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                task.setText(cursor.getString(cursor.getColumnIndexOrThrow("text")));
                task.setDeadline(cursor.getString(cursor.getColumnIndexOrThrow("deadline")));
                task.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) > 0);
                task.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow("priority")));
                task.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                tasksList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasksList;
    }

    public List<SubtaskModel> readSubtasks() {
        String query = "SELECT * FROM Subtasks";
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        List<SubtaskModel> subtasksList = new ArrayList<>();
        if (cursor == null) return subtasksList;
        if (cursor.moveToFirst()) {
             do {
                SubtaskModel subtask = new SubtaskModel();
                subtask.setIdSubtask(cursor.getInt(cursor.getColumnIndexOrThrow("id_subtask")));
                subtask.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                subtask.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) > 0);
                subtasksList.add(subtask);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return subtasksList;
    }

    public List<SubtaskModel> readSubtasks(int idTask) {
        String query = "SELECT * FROM Subtasks WHERE task_id = " + idTask;
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        List<SubtaskModel> subtasksList = new ArrayList<>();
        if (cursor == null) return subtasksList;
        if (cursor.moveToFirst()) {
            do {
                SubtaskModel subtask = new SubtaskModel();
                subtask.setIdSubtask(cursor.getInt(cursor.getColumnIndexOrThrow("id_subtask")));
                subtask.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                subtask.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) != 0);
                subtasksList.add(subtask);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return subtasksList;
    }

    public List<CategoryModel> readCategories() {
        String query = "SELECT * FROM Categories";
        db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        List<CategoryModel> categoriesList = new ArrayList<>();
        if (cursor == null) return categoriesList;
        if (cursor.moveToFirst()) {
            do {
                CategoryModel category = new CategoryModel();
                category.setIdCategory(cursor.getInt(cursor.getColumnIndexOrThrow("id_category")));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                category.setColor(cursor.getString(cursor.getColumnIndexOrThrow("color")));
                categoriesList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoriesList;
    }


    public void updateTask(int id, String title, String text) { this.updateTask(id, 0, title, text, 3); }

    public void updateTask(int id, int categoryId, String title, String text, int priority) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if (categoryId != 0) cv.put("category_id", categoryId);
        cv.put("title", title);
        cv.put("text", text);
        cv.put("priority", priority);

        long result = db.update("Tasks", cv, "id_task=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void updateSubtask(int id, String title) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);

        long result = db.update("Subtasks", cv, "id_subtask=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void updateCategory(int id, String name, Color color) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("color", String.format("#%06X", (0xFFFFFF & color.toArgb())));

        long result = db.update("Categories", cv, "id_category=?", new String[]{String.valueOf(id)});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");

        long result = db.delete("Tasks", "id_task=?", new String[]{String.valueOf(id)});

        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void deleteSubtask(int id) {
        db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");

        long result = db.delete("Subtasks", "id_subtask=?", new String[]{String.valueOf(id)});

        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void deleteCategory(int id) {
        db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");

        long result = db.delete("Categories", "id_category=?", new String[]{String.valueOf(id)});

        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}