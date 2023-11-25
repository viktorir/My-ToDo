package com.example.mytodolist.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
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
    private static final int ADD_SUBTASKS_VIEW = R.layout.add_new_subtask_item_layout;

    private List<SubtaskModel> subtasksList;
    private Context context;
    private DataBaseHelper db;
    private int id;

    public SubtasksAdapter(Context context, DataBaseHelper db, int id) {
        this.context = context;
        this.db = db;
        this.id = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == SUBTASK_VIEW) viewHolder = new SubtasksAdapter.SubtaskViewHolder(view);
        if (viewType == ADD_SUBTASKS_VIEW) viewHolder = new SubtasksAdapter.AddSubtasksViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubtaskViewHolder) {
            final SubtaskModel subtask = subtasksList.get(position);

            ((SubtasksAdapter.SubtaskViewHolder)holder).titleView.setText(subtask.getTitle());
            ((SubtasksAdapter.SubtaskViewHolder)holder).titleView.setHint(String.valueOf(subtask.getIdSubtask()));
        }

        if (holder instanceof AddSubtasksViewHolder) {
            ((AddSubtasksViewHolder)holder).addNewSubtask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNewSubtask.newInstance(id).show(((AppCompatActivity)context).getSupportFragmentManager(), AddNewSubtask.TAG);
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (subtasksList.size() == 0 || subtasksList.size() == position) return ADD_SUBTASKS_VIEW;
        else return SUBTASK_VIEW;
    }

    @Override
    public int getItemCount() {
        //Toast.makeText(context, "Subtask item: " + String.valueOf(subtasksList.size()), Toast.LENGTH_LONG).show();
        if (subtasksList.size() == 0) return 1;
        return subtasksList.size() + 1;
    }

    public void setSubtasks(List<SubtaskModel> subtasksList){
        this.subtasksList = subtasksList;
        notifyDataSetChanged();
    }

    private void removeAt(int pos) {
        subtasksList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, subtasksList.size());
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView titleView;
        public SubtaskViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.TitleView);
            radioButton = itemView.findViewById(R.id.RadioButton);

            if (radioButton.isSelected()) {
                db.deleteSubtask(Integer.parseInt((String)titleView.getHint()));
                removeAt(getAdapterPosition());
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteSubtask(Integer.parseInt((String)titleView.getHint()));
                    removeAt(getAdapterPosition());
                }
            });
        }
    }

    public class AddSubtasksViewHolder extends RecyclerView.ViewHolder {
        CardView addNewSubtask;
        public AddSubtasksViewHolder(View itemView) {
            super(itemView);

            addNewSubtask = itemView.findViewById(R.id.addNewSubtask);
        }
    }
}
