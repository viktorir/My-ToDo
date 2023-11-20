package com.example.mytodolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        return inflater.inflate(R.layout.change_priority_layout, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        p1 = view.findViewById(R.id.P1Button);
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putInt("priority", 1);
                getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);
                dismiss();
            }
        });

        p2 = view.findViewById(R.id.P2Button);
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putInt("priority", 2);
                getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);dismiss();
            }
        });

        p3 = view.findViewById(R.id.P3Button);
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putInt("priority", 3);
                getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);
                dismiss();
            }
        });

        p4 = view.findViewById(R.id.P4Button);
        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putInt("priority", 4);
                getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);
                dismiss();
            }
        });

        p5 = view.findViewById(R.id.P5Button);
        p5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putInt("priority", 5);
                getActivity().getSupportFragmentManager().setFragmentResult("priorityData", result);
                dismiss();
            }
        });
    }
}
