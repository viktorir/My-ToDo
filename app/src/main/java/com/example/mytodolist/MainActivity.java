package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mytodolist.adapters.TasksAdapter;
import com.example.mytodolist.fragments.CreateTask;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener, PopupMenu.OnMenuItemClickListener {

    TextView header;
    RecyclerView taskList;
    FloatingActionButton createTask;
    ImageView settingsMenuButton;
    TasksAdapter tasksAdapter;
    DataBaseHelper db;
    List<TaskModel> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyToDoList);
        setLocale(this, Locale.getDefault().getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.Header);
        taskList = findViewById(R.id.TasksList);
        createTask = findViewById(R.id.CreateTaskButton);
        settingsMenuButton = findViewById(R.id.settingsMenuButton);

        db = new DataBaseHelper(MainActivity.this);
        tasksList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(MainActivity.this, db);

        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(tasksAdapter);

        tasksList = db.readTasks();
        tasksAdapter.setTasks(tasksList);

        createTask.setOnClickListener(v -> CreateTask.newInstance().show(getSupportFragmentManager(), CreateTask.TAG));

        settingsMenuButton.setOnClickListener(v -> showMainPopupMenu(v));
    }

    public void onResume() {
        super.onResume();
        this.onDialogClose(null);
    }

    public void showMainPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v, Gravity.END);
        popup.setOnMenuItemClickListener(this);
        popup.getMenuInflater().inflate(R.menu.main_setting_menu, popup.getMenu());
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.category:
                Intent intent = new Intent(this, CategoryManagerActivity.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }

    public void onDialogClose (DialogInterface dialogInterface) {
        tasksList = db.readTasks();
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources res = activity.getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }
}