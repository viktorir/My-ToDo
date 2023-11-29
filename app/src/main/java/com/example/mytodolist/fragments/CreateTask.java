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
import androidx.fragment.app.FragmentResultListener;

import com.example.mytodolist.OnDialogCloseListener;
import com.example.mytodolist.R;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class CreateTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddTask";

    EditText titleTask, descriptionTask;
    MaterialButton addButton, priorityButton;

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
        priorityButton.setHint("3");

        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "priorityData",
                this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle results) {
                        int result = results.getInt("priority");
                        priorityButton.setHint(String.valueOf(result));
                        setPriorityButtonIcon(result);
                    }
                }
        );

        titleTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleTask.getText().toString().equals(""))
                {
                    addButton.setEnabled(false);
                }
                else {
                    addButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setEnabled(false);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper db = new DataBaseHelper(CreateTask.this.getContext());
                db.insertTask(titleTask.getText().toString().trim(), descriptionTask.getText().toString().trim(), Integer.parseInt((String)priorityButton.getHint()));
                dismiss();
            }
        });

        priorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePriority.newInstance().show(getActivity().getSupportFragmentManager(), ChangePriority.TAG);
            }
        });
    }

    private void setPriorityButtonIcon(int id) {
        switch (id) {
            case 1:
                priorityButton.setIcon(getContext().getDrawable(R.drawable.priority_1));
                break;
            case 2:
                priorityButton.setIcon(getContext().getDrawable(R.drawable.priority_2));
                break;
            case 3:
                priorityButton.setIcon(getContext().getDrawable(R.drawable.priority_3));
                break;
            case 4:
                priorityButton.setIcon(getContext().getDrawable(R.drawable.priority_4));
                break;
            case 5:
                priorityButton.setIcon(getContext().getDrawable(R.drawable.priority_5));
                break;
        }
        if (id == 3) priorityButton.setIconTint(ColorStateList.valueOf(getContext().getColor(R.color.white)));
        else priorityButton.setIconTint(null);
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}