package com.example.mytodolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.AdapterToFragmentCommunication;
import com.example.mytodolist.R;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class CategoriesChangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int CATEGORY_VIEW = R.layout.item_category;
    private static final int NO_CATEGORY_VIEW = R.layout.item_no_category;

    private List<CategoryModel> categoriesList;
    Context context;
    DataBaseHelper db;
    AdapterToFragmentCommunication commutator;

    public CategoriesChangeAdapter(Context context, DataBaseHelper db, AdapterToFragmentCommunication commutator) {
        this.context = context;
        this.db = db;
        this.commutator = commutator;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == CATEGORY_VIEW) viewHolder = new CategoriesChangeAdapter.CategoryViewHolder(view);
        if (viewType == NO_CATEGORY_VIEW || viewHolder == null) viewHolder = new CategoriesChangeAdapter.NoCategoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoriesChangeAdapter.CategoryViewHolder) {
            CategoriesChangeAdapter.CategoryViewHolder categoryHolder = (CategoriesChangeAdapter.CategoryViewHolder)holder;
            final CategoryModel category = categoriesList.get(position - 1);

            categoryHolder.nameText.setText(category.getName());
            categoryHolder.coloredIcon.setColorFilter(Color.parseColor(category.getColor()));

            categoryHolder.nameText.setOnClickListener(v -> commutator.categoryRespond(position - 1, category.getIdCategory(), category.getName()));

            categoryHolder.deleteButton.setVisibility(View.GONE);
            categoryHolder.deleteButton.setClickable(false);
        }

        if (holder instanceof CategoriesChangeAdapter.NoCategoryViewHolder) {
            CategoriesChangeAdapter.NoCategoryViewHolder noCategoryHolder = (CategoriesChangeAdapter.NoCategoryViewHolder)holder;
            noCategoryHolder.nameText.setText(context.getResources().getString(R.string.unChangeCategory));

            noCategoryHolder.nameText.setOnClickListener(v -> commutator.categoryRespond(position, 0, context.getResources().getString(R.string.unChangeCategory)));
        }
    }

    public int getItemViewType(int position) {
        if (categoriesList.size() == 0 || position == 0) return NO_CATEGORY_VIEW;
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

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView coloredIcon, deleteButton;
        TextView nameText;
        public CategoryViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameText);
            coloredIcon = itemView.findViewById(R.id.coloredIcon);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public static class NoCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        public NoCategoryViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameText);
        }
    }
}
