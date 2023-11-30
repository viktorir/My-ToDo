package com.example.mytodolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytodolist.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangePriority extends BottomSheetDialogFragment {
    public static final String TAG = "ChangePriority";

    Button p1, p2, p3, p4, p5;

    public static ChangePriority newInstance() {
        return new ChangePriority();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_priority, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        p1 = view.findViewById(R.id.P1Button);
        p1.setOnClickListener(v -> setResultChange(1));

        p2 = view.findViewById(R.id.P2Button);
        p2.setOnClickListener(v -> setResultChange(2));

        p3 = view.findViewById(R.id.P3Button);
        p3.setOnClickListener(v -> setResultChange(3));

        p4 = view.findViewById(R.id.P4Button);
        p4.setOnClickListener(v -> setResultChange(4));

        p5 = view.findViewById(R.id.P5Button);
        p5.setOnClickListener(v -> setResultChange(5));
    }

    private void setResultChange(int res) {
        Bundle result = new Bundle();
        result.putInt("priority", res);
        getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);
        dismiss();
    }
}
