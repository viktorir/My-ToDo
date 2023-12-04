package com.example.mytodolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.AdapterToFragmentCommunication;
import com.example.mytodolist.R;
import com.example.mytodolist.adapters.CategoriesChangeAdapter;
import com.example.mytodolist.models.CategoryModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ChangeCategory extends BottomSheetDialogFragment {
    public static final String TAG = "ChangeCategory";

    RecyclerView categoryList;
    CategoriesChangeAdapter categoriesChangeAdapter;
    DataBaseHelper db;
    List<CategoryModel> categoriesList;

    public static ChangeCategory newInstance() {
        return new ChangeCategory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_category, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList = view.findViewById(R.id.categoriesList);
        db = new DataBaseHelper(getContext());
        categoriesList = new ArrayList<>();
        categoriesChangeAdapter = new CategoriesChangeAdapter(getContext(), db, commutator);

        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList.setAdapter(categoriesChangeAdapter);

        categoriesList = db.readCategories();
        categoriesChangeAdapter.setCategories(categoriesList);
    }

    AdapterToFragmentCommunication commutator = new AdapterToFragmentCommunication() {
        @Override
        public void categoryRespond(int position, int id, String name) {
            Bundle result = new Bundle();
            result.putInt("id", id);
            result.putString("name", name);
            getActivity().getSupportFragmentManager().setFragmentResult("categoryData", result);
            dismiss();
        }
    };
}
