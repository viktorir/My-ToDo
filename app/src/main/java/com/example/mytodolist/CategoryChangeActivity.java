package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mytodolist.adapters.CategoriesAdapter;
import com.example.mytodolist.adapters.TasksAdapter;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryChangeActivity extends AppCompatActivity {
    TextView backButton;
    RecyclerView categoryList;
    CategoriesAdapter categoriesAdapter;
    DataBaseHelper db;
    List<CategoryModel> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_change);

        categoryList = findViewById(R.id.categoriesList);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = new DataBaseHelper(CategoryChangeActivity.this);
        categoriesList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(CategoryChangeActivity.this, db);

        categoryList.setLayoutManager(new LinearLayoutManager(this));
        categoryList.setAdapter(categoriesAdapter);

        //categoriesList = db.readTasks();
        categoriesAdapter.setCategories(categoriesList);
    }
}