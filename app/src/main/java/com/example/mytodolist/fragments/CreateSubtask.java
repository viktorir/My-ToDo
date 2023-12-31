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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytodolist.R;
import com.example.mytodolist.utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateSubtask extends BottomSheetDialogFragment {
    public static final String TAG = "AddSubtask";
    EditText titleSubtask;
    Button addButton;

    public static CreateSubtask newInstance(int id) {
        CreateSubtask addNewSubtask = new CreateSubtask();
        Bundle result = new Bundle();
        result.putInt("id_task", id);
        addNewSubtask.setArguments(result);
        return addNewSubtask;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_subtask, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleSubtask = view.findViewById(R.id.TitleSubtask);
        addButton = view.findViewById(R.id.AddButton);

        titleSubtask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addButton.setEnabled(!titleSubtask.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setEnabled(false);
        addButton.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(CreateSubtask.this.getContext());

            if (getArguments() != null) db.insertSubtask(getArguments().getInt("id_task"), titleSubtask.getText().toString().trim());
            else Toast.makeText(getContext(), "no arguments!", Toast.LENGTH_LONG).show();

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
