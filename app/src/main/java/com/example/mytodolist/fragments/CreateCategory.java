package com.example.mytodolist.fragments;

import android.app.Activity;
import android.content.DialogInterface;
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

public class CreateCategory extends BottomSheetDialogFragment {
    public static final String TAG = "AddCategory";

    EditText nameCategory;
    MaterialButton colorChangeButton, addButton;

    public static CreateCategory newInstance() {
        return new CreateCategory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_category, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameCategory = view.findViewById(R.id.categoryText);
        addButton = view.findViewById(R.id.addButton);
        colorChangeButton = view.findViewById(R.id.colorButton);

        nameCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addButton.setEnabled(!nameCategory.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setEnabled(false);
        addButton.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(CreateCategory.this.getContext());
            db.insertCategory(nameCategory.getText().toString().trim());
            db.close();
            dismiss();
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
