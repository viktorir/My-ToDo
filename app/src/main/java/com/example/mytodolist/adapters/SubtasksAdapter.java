package com.example.mytodolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.fragments.CreateSubtask;
import com.example.mytodolist.R;
import com.example.mytodolist.fragments.UpdateSubtask;
import com.example.mytodolist.models.SubtaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class SubtasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SUBTASK_VIEW = R.layout.item_subtask;
    private static final int ADD_SUBTASKS_VIEW = R.layout.item_add_new_subtask;

    private List<SubtaskModel> subtasksList;
    Context context;
    DataBaseHelper db;
    int id;

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

        if (viewType == SUBTASK_VIEW) viewHolder = new SubtaskViewHolder(view);
        if (viewType == ADD_SUBTASKS_VIEW || viewHolder == null) viewHolder = new AddSubtasksViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SubtaskViewHolder) {
            SubtaskViewHolder subtaskViewHolder = (SubtasksAdapter.SubtaskViewHolder)holder;
            final SubtaskModel subtask = subtasksList.get(position);

            subtaskViewHolder.titleView.setText(subtask.getTitle());
            subtaskViewHolder.titleView.setOnClickListener(v -> {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                UpdateSubtask.newInstance(subtask.getIdSubtask(), subtask.getTitle()).show(manager, UpdateSubtask.TAG);
            });

            subtaskViewHolder.radioButton.setChecked(subtaskViewHolder.radioButton.isChecked());
            subtaskViewHolder.radioButton.setOnClickListener(v -> {
                db.deleteSubtask(subtask.getIdSubtask());
                removeAt(subtaskViewHolder.getBindingAdapterPosition());
            });
        }

        if (holder instanceof AddSubtasksViewHolder) {
            AddSubtasksViewHolder addSubtasksViewHolder = (AddSubtasksViewHolder)holder;
            addSubtasksViewHolder.addNewSubtask.setOnClickListener(v -> CreateSubtask.newInstance(id).show(((AppCompatActivity)context).getSupportFragmentManager(), CreateSubtask.TAG));
        }
    }

    public int getItemViewType(int position) {
        if (subtasksList.size() == 0 || subtasksList.size() == position) return ADD_SUBTASKS_VIEW;
        else return SUBTASK_VIEW;
    }

    @Override
    public int getItemCount() {
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

    public static class SubtaskViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView titleView;
        public SubtaskViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.TitleView);
            radioButton = itemView.findViewById(R.id.RadioButton);
        }
    }

    public static class AddSubtasksViewHolder extends RecyclerView.ViewHolder {
        CardView addNewSubtask;
        public AddSubtasksViewHolder(View itemView) {
            super(itemView);

            addNewSubtask = itemView.findViewById(R.id.addNewSubtask);
        }
    }
}
