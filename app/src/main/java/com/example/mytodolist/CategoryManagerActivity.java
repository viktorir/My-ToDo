package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mytodolist.adapters.CategoriesAdapter;
import com.example.mytodolist.fragments.CreateCategory;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagerActivity extends AppCompatActivity implements OnDialogCloseListener{

    FloatingActionButton createCategoryButton;
    TextView backButton;
    RecyclerView categoryList;
    CategoriesAdapter categoriesAdapter;
    DataBaseHelper db;
    List<CategoryModel> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        categoryList = findViewById(R.id.categoriesList);
        backButton = findViewById(R.id.backButton);
        createCategoryButton = findViewById(R.id.createCategoryButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCategory.newInstance().show(getSupportFragmentManager(), CreateCategory.TAG);
            }
        });


        db = new DataBaseHelper(CategoryManagerActivity.this);
        categoriesList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(CategoryManagerActivity.this, db);

        categoryList.setLayoutManager(new LinearLayoutManager(this));
        categoryList.setAdapter(categoriesAdapter);

        categoriesList = db.readCategories();
        categoriesAdapter.setCategories(categoriesList);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        categoriesList = db.readCategories();
        categoriesAdapter.setCategories(categoriesList);
        categoriesAdapter.notifyDataSetChanged();
    }
}