package com.example.mytodolist.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.mytodolist.OnDialogCloseListener;
import com.example.mytodolist.R;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class CreateTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddTask";

    EditText titleTask, descriptionTask;
    MaterialButton addButton, priorityButton, categoryButton;

    public static CreateTask newInstance() {
        return new CreateTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTask = view.findViewById(R.id.TitleTask);
        descriptionTask = view.findViewById(R.id.DescriptionTask);
        addButton = view.findViewById(R.id.AddButton);
        priorityButton = view.findViewById(R.id.PriorityButton);
        categoryButton = view.findViewById(R.id.CategoryButton);
        priorityButton.setHint("3");

        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "priorityData",
                this,
                (requestKey, results) -> {
                    int result = results.getInt("priority");
                    priorityButton.setHint(String.valueOf(result));
                    priorityButton.setIcon(AppCompatResources.getDrawable(getContext(), priorityToIcon(result)));
                    if (result == 3) priorityButton.setIconTint(ColorStateList.valueOf(getContext().getColor(R.color.white)));
                    else priorityButton.setIconTint(null);
                }
        );

        titleTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addButton.setEnabled(!titleTask.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setEnabled(false);
        addButton.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(CreateTask.this.getContext());
            db.insertTask(titleTask.getText().toString().trim(), descriptionTask.getText().toString().trim(), Integer.parseInt((String)priorityButton.getHint()));
            db.close();
            dismiss();
        });

        priorityButton.setOnClickListener(v -> ChangePriority.newInstance().show(getActivity().getSupportFragmentManager(), ChangePriority.TAG));

        categoryButton.setOnClickListener(v -> ChangeCategory.newInstance().show(getActivity().getSupportFragmentManager(), ChangeCategory.TAG));
    }

    private int priorityToIcon(int priority) {
        switch (priority) {
            case 1:
                return R.drawable.priority_1;
            case 2:
                return R.drawable.priority_2;
            case 4:
                return R.drawable.priority_4;
            case 5:
                return R.drawable.priority_5;
            default:
                return R.drawable.priority_3;
        }
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
