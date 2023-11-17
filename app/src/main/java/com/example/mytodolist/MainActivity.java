package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mytodolist.adapters.TasksAdapter;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    TextView header;
    RecyclerView taskList;
    FloatingActionButton createTask;

    TasksAdapter tasksAdapter;
    DataBaseHelper db;
    List<TaskModel> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyToDoList);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.Header);
        taskList = findViewById(R.id.TasksList);
        createTask = findViewById(R.id.CreateTaskButton);

        db = new DataBaseHelper(MainActivity.this);
        tasksList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(MainActivity.this, db);

        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(tasksAdapter);

        tasksList = db.readTasks();
        tasksAdapter.setTasks(tasksList);

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    public void onDialogClose (DialogInterface dialogInterface) {
        tasksList = db.readTasks();
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }
}