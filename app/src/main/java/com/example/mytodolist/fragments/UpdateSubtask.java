package com.example.mytodolist.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytodolist.R;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UpdateSubtask extends BottomSheetDialogFragment {
    public static final String TAG = "UpdateSubtask";

    EditText titleSubtask;
    Button updateButton;

    int id;
    String title;

    public static UpdateSubtask newInstance(int id, String title) {
        UpdateSubtask fragment = new UpdateSubtask();
        Bundle args = new Bundle();
        args.putInt("id_subtask", id);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_subtask, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("id_subtask");
            title = getArguments().getString("title");
        }

        titleSubtask = view.findViewById(R.id.titleSubtask);
        updateButton = view.findViewById(R.id.updateButton);

        titleSubtask.setText(title);

        titleSubtask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton.setEnabled(!titleSubtask.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateButton.setEnabled(!titleSubtask.getText().toString().equals(""));

        updateButton.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(UpdateSubtask.this.getContext());
            db.updateSubtask(id ,titleSubtask.getText().toString().trim());
            db.close();
            dismiss();
        });
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(UpdateTask.TAG);
        if (fragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener)fragment).onDismiss(dialog);
        }
    }
}
