package com.example.mytodolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.AddNewSubtask;
import com.example.mytodolist.R;
import com.example.mytodolist.models.SubtaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class SubtasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SUBTASK_VIEW = R.layout.subtask_item_layout;
    private static final int NO_SUBTASKS_VIEW = R.layout.add_new_subtask_item_layout;

    private List<SubtaskModel> subtasksList;
    private Context context;
    private DataBaseHelper db;

    public SubtasksAdapter(Context context, DataBaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == SUBTASK_VIEW) viewHolder = new SubtasksAdapter.SubtaskViewHolder(view);
        if (viewType == NO_SUBTASKS_VIEW) viewHolder = new SubtasksAdapter.NoSubtasksViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubtaskViewHolder) {

        }

        if (holder instanceof NoSubtasksViewHolder) {
            ((NoSubtasksViewHolder)holder).addNewSubtask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNewSubtask.newInstance().show(((AppCompatActivity)context).getSupportFragmentManager(), AddNewSubtask.TAG);
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (subtasksList.size() == 0) return NO_SUBTASKS_VIEW;
        else return SUBTASK_VIEW;
    }

    @Override
    public int getItemCount() {
        Toast.makeText(context, "Subtask item: " + String.valueOf(subtasksList.size()), Toast.LENGTH_LONG).show();
        if (subtasksList.size() == 0) return 1;
        return subtasksList.size();
    }

    public void setSubtasks(List<SubtaskModel> subtasksList){
        this.subtasksList = subtasksList;
        notifyDataSetChanged();
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder {
        public SubtaskViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class NoSubtasksViewHolder extends RecyclerView.ViewHolder {

        CardView addNewSubtask;
        public NoSubtasksViewHolder(View itemView) {
            super(itemView);

            addNewSubtask = itemView.findViewById(R.id.addNewSubtask);
        }
    }
}
