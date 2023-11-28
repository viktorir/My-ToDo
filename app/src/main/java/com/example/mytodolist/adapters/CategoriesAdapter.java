package com.example.mytodolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.models.SubtaskModel;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int CATEGORY_VIEW = R.layout.item_subtask;
    private static final int NO_CATEGORY_VIEW = R.layout.item_no_category;
    private List<CategoryModel> categoriesList;
    Context context;
    DataBaseHelper db;

    public CategoriesAdapter(Context context, DataBaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == CATEGORY_VIEW) viewHolder = new CategoriesAdapter.CategoryViewHolder(view);
        if (viewType == NO_CATEGORY_VIEW) viewHolder = new CategoriesAdapter.NoCategoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (categoriesList.size() == 0 || position == 1) return NO_CATEGORY_VIEW;
        else return CATEGORY_VIEW;
    }

    @Override
    public int getItemCount() {
        if (categoriesList.size() == 0) return 1;
        return categoriesList.size() + 1;
    }

    public void setCategories(List<CategoryModel> categoriesList){
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public CategoryViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class NoCategoryViewHolder extends RecyclerView.ViewHolder {
        public NoCategoryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
