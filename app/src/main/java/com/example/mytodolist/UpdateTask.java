package com.example.mytodolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.adapters.SubtasksAdapter;
import com.example.mytodolist.models.SubtaskModel;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends BottomSheetDialogFragment implements OnDialogCloseListener {
    public static final String TAG = "UpdateTask";

    EditText titleTask;
    EditText descriptionTask;
    Button updateButton, priorityButton;
    RecyclerView subtaskList;
    SubtasksAdapter subtasksAdapter;
    DataBaseHelper db;
    List<SubtaskModel> subtasksList;

    int id, priority;
    String title, text;

    public static UpdateTask newInstance(int id, String title, String text, int priority) {
        UpdateTask fragment = new UpdateTask();
        Bundle args = new Bundle();
        args.putInt("id_task", id);
        args.putString("title", title);
        args.putString("text", text);
        args.putInt("priority", priority);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_task_layout, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        if (getArguments() != null) {
            id = getArguments().getInt("id_task");
            title = getArguments().getString("title");
            text = getArguments().getString("text");
            priority = getArguments().getInt("priority");
        }

        titleTask = view.findViewById(R.id.TitleSubtask);
        descriptionTask = view.findViewById(R.id.DescriptionTask);
        updateButton = view.findViewById(R.id.UpdateButton);
        priorityButton = view.findViewById(R.id.PriorityButton);
        subtaskList = view.findViewById(R.id.SubtasksList);

        titleTask.setText(title);
        priorityButton.setHint(String.valueOf(priority));
        descriptionTask.setText(text);

        titleTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleTask.getText().toString().equals(""))
                {
                    updateButton.setEnabled(false);
                }
                else {
                    updateButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (titleTask.getText().toString().equals(""))
        {
            updateButton.setEnabled(false);
        }
        else {
            updateButton.setEnabled(true);
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper db = new DataBaseHelper(UpdateTask.this.getContext());
                db.updateTask(id ,titleTask.getText().toString().trim(), descriptionTask.getText().toString().trim(), Integer.parseInt(String.valueOf(priorityButton.getHint())));
                dismiss();
            }
        });

        setPriorityButtonIcon(priority);
        priorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePriority.newInstance().show(getActivity().getSupportFragmentManager(), ChangePriority.TAG);
            }
        });

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
                priorityButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.priority_1, 0, 0, 0);
                break;
            case 2:
                priorityButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.priority_2, 0, 0, 0);
                break;
            case 3:
                priorityButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.priority_3, 0, 0, 0);
                break;
            case 4:
                priorityButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.priority_4, 0, 0, 0);
                break;
            case 5:
                priorityButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.priority_5, 0, 0, 0);
                break;
        }
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }

    public void onDialogClose(DialogInterface dialogInterface) {
        subtasksList = db.readSubtasks(id);
        subtasksAdapter.setSubtasks(subtasksList);
        subtasksAdapter.notifyDataSetChanged();
    }
}
