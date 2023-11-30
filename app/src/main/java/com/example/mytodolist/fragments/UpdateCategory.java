package com.example.mytodolist.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytodolist.OnDialogCloseListener;
import com.example.mytodolist.R;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class UpdateCategory extends BottomSheetDialogFragment {
    public static final String TAG = "UpdateSubtask";

    EditText nameCategory;
    MaterialButton colorChangeButton, updateButton;

    int id;
    String name, color;

    public static UpdateCategory newInstance(int id, String name, String color) {
        UpdateCategory fragment = new UpdateCategory();
        Bundle args = new Bundle();
        args.putInt("id_category", id);
        args.putString("name", name);
        args.putString("color", color);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_category, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("id_category");
            name = getArguments().getString("name");
            color = getArguments().getString("color", "#000000");
        }

        nameCategory = view.findViewById(R.id.categoryText);
        colorChangeButton = view.findViewById(R.id.colorButton);
        updateButton = view.findViewById(R.id.updateButton);

        nameCategory.setText(name);

        nameCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton.setEnabled(!nameCategory.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateButton.setEnabled(!nameCategory.getText().toString().equals(""));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper db = new DataBaseHelper(UpdateCategory.this.getContext());
                db.updateCategory(id ,nameCategory.getText().toString().trim(), Color.valueOf(Color.parseColor(color)));
                db.close();
                dismiss();
            }
        });
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
