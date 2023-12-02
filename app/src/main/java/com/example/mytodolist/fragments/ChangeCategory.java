package com.example.mytodolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangeCategory extends BottomSheetDialogFragment {
    public static final String TAG = "ChangeCategory";

    RecyclerView categoriesList;

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

        categoriesList = view.findViewById(R.id.categoriesList);
    }
}
