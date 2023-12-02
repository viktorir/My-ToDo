package com.example.mytodolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.fragments.UpdateCategory;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class CategoriesManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int CATEGORY_VIEW = R.layout.item_category;
    private static final int NO_CATEGORY_VIEW = R.layout.item_no_category;
    private List<CategoryModel> categoriesList;
    Context context;
    DataBaseHelper db;

    public CategoriesManagerAdapter(Context context, DataBaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == CATEGORY_VIEW) viewHolder = new CategoriesManagerAdapter.CategoryViewHolder(view);
        if (viewType == NO_CATEGORY_VIEW || viewHolder == null) viewHolder = new CategoriesManagerAdapter.NoCategoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            CategoryViewHolder categoryHolder = (CategoryViewHolder)holder;
            final CategoryModel category = categoriesList.get(position - 1);

            categoryHolder.nameText.setText(category.getName());
            categoryHolder.coloredIcon.setColorFilter(Color.parseColor(category.getColor()));

            categoryHolder.nameText.setOnClickListener(v -> {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                UpdateCategory.newInstance(category.getIdCategory(),
                        category.getName(),
                        category.getColor()).show(manager, UpdateCategory.TAG);
            });

            categoryHolder.deleteButton.setOnClickListener(v -> {
                db.deleteCategory(category.getIdCategory());
                removeAt(holder.getBindingAdapterPosition());
            });
        }

        if (holder instanceof NoCategoryViewHolder) {
            NoCategoryViewHolder noCategoryHolder = (NoCategoryViewHolder)holder;
            noCategoryHolder.nameText.setText(context.getResources().getString(R.string.unChangeCategory));

            noCategoryHolder.nameText.setOnClickListener(v -> Toast.makeText(context, "This base ¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show());
        }
    }

    @Override
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

    private void removeAt(int pos) {
        categoriesList.remove(pos - 1);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos - 1, categoriesList.size());
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
