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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.OnDialogCloseListener;
import com.example.mytodolist.R;
import com.example.mytodolist.adapters.SubtasksAdapter;
import com.example.mytodolist.models.SubtaskModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends BottomSheetDialogFragment implements DialogInterface.OnDismissListener {
    public static final String TAG = "UpdateTask";

    EditText titleTask;
    EditText descriptionTask;
    MaterialButton updateButton, priorityButton, categoryButton;
    RecyclerView subtaskList;
    SubtasksAdapter subtasksAdapter;
    DataBaseHelper db;
    List<SubtaskModel> subtasksList;

    int id, priority, categoryId;
    String title, text, categoryName;

    public static UpdateTask newInstance(int id, String title, String text, int priority, int categoryId, String categoryName) {
        UpdateTask fragment = new UpdateTask();
        Bundle args = new Bundle();
        args.putInt("id_task", id);
        args.putString("title", title);
        args.putString("text", text);
        args.putInt("priority", priority);
        args.putInt("category_id", categoryId);
        args.putString("category_name", categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_task, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTask = view.findViewById(R.id.TitleSubtask);
        descriptionTask = view.findViewById(R.id.DescriptionTask);
        updateButton = view.findViewById(R.id.UpdateButton);
        priorityButton = view.findViewById(R.id.PriorityButton);
        subtaskList = view.findViewById(R.id.SubtasksList);
        categoryButton = view.findViewById(R.id.CategoryButton);

        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "priorityData",
                this,
                (requestKey, results) -> {
                    int result = results.getInt("priority");
                    priorityButton.setHint(String.valueOf(result));
                    setPriorityButtonIcon(result);
                }
        );

        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "categoryData",
                this,
                (requestKey, results) -> {
                    int id = results.getInt("id");
                    String name = results.getString("name");
                    categoryButton.setHint(String.valueOf(id));
                    categoryButton.setText(name);
                }
        );

        if (getArguments() != null) {
            id = getArguments().getInt("id_task");
            title = getArguments().getString("title");
            text = getArguments().getString("text");
            priority = getArguments().getInt("priority");
            categoryId = getArguments().getInt("category_id");
            categoryName = getArguments().getString("category_name");
        }

        titleTask.setText(title);
        priorityButton.setHint(String.valueOf(priority));
        descriptionTask.setText(text);

        titleTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton.setEnabled(!titleTask.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateButton.setEnabled(!titleTask.getText().toString().equals(""));
        updateButton.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(UpdateTask.this.getContext());
            db.updateTask(id, Integer.parseInt((String) categoryButton.getHint()),titleTask.getText().toString().trim(), descriptionTask.getText().toString().trim(), Integer.parseInt(String.valueOf(priorityButton.getHint())));
            db.close();
            dismiss();
        });

        setPriorityButtonIcon(priority);
        priorityButton.setOnClickListener(v -> ChangePriority.newInstance().show(getActivity().getSupportFragmentManager(), ChangePriority.TAG));

        categoryButton.setHint(String.valueOf(categoryId));
        if (categoryId != 0) categoryButton.setText(categoryName);
        else categoryButton.setText(getResources().getString(R.string.unChangeCategory));
        categoryButton.setOnClickListener(v -> ChangeCategory.newInstance().show(getActivity().getSupportFragmentManager(), ChangeCategory.TAG));

        db = new DataBaseHelper(getActivity());
        subtasksList = new ArrayList<>();
        subtasksAdapter = new SubtasksAdapter(getActivity(), db, id);

        subtaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        subtaskList.setAdapter(subtasksAdapter);

        subtasksList = db.readSubtasks(id);
        subtasksAdapter.setSubtasks(subtasksList);
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
        subtasksList = db.readSubtasks(id);
        subtasksAdapter.setSubtasks(subtasksList);
        subtasksAdapter.notifyDataSetChanged();
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            super.onDismiss(dialog);
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
