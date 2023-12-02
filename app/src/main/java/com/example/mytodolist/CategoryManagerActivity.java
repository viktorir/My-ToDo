package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mytodolist.adapters.CategoriesManagerAdapter;
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
    CategoriesManagerAdapter categoriesManagerAdapter;
    DataBaseHelper db;
    List<CategoryModel> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        categoryList = findViewById(R.id.categoriesList);
        backButton = findViewById(R.id.backButton);
        createCategoryButton = findViewById(R.id.createCategoryButton);

        backButton.setOnClickListener(v -> finish());

        createCategoryButton.setOnClickListener(v -> CreateCategory.newInstance().show(getSupportFragmentManager(), CreateCategory.TAG));

        db = new DataBaseHelper(CategoryManagerActivity.this);
        categoriesList = new ArrayList<>();
        categoriesManagerAdapter = new CategoriesManagerAdapter(CategoryManagerActivity.this, db);

        categoryList.setLayoutManager(new LinearLayoutManager(this));
        categoryList.setAdapter(categoriesManagerAdapter);

        categoriesList = db.readCategories();
        categoriesManagerAdapter.setCategories(categoriesList);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        categoriesList = db.readCategories();
        categoriesManagerAdapter.setCategories(categoriesList);
        categoriesManagerAdapter.notifyDataSetChanged();
    }
}