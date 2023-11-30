package com.example.mytodolist.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.fragments.UpdateTask;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TASK_VIEW = R.layout.item_task;
    private static final int NO_TASKS_VIEW = R.layout.item_no_tasks;

    private List<TaskModel> tasksList;
    Context context;
    DataBaseHelper db;

    public TasksAdapter(Context context, DataBaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == TASK_VIEW) viewHolder = new TaskViewHolder(view);
        if (viewType == NO_TASKS_VIEW || viewHolder == null) viewHolder = new NoTasksViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolder) {
            final TaskModel task = tasksList.get(position);
            TaskViewHolder taskViewHolder = (TaskViewHolder)holder;

            if (task.getCategoryId() == 0) taskViewHolder.categoryView.setText(R.string.unChangeCategory);
            else taskViewHolder.categoryView.setText(task.getCategoryName());

            taskViewHolder.textView.setText(task.getText());
            if (task.getText() == null) taskViewHolder.textView.setVisibility(View.GONE);
            else taskViewHolder.textView.setVisibility(View.VISIBLE);

            taskViewHolder.priorityView.setImageResource(priorityToIcon(task.getPriority()));

            taskViewHolder.radioButton.setChecked(taskViewHolder.radioButton.isChecked());
            if (taskViewHolder.radioButton.isSelected()) {
                db.deleteTask(task.getIdTask());
                removeAt(taskViewHolder.getBindingAdapterPosition());
            }
            taskViewHolder.radioButton.setOnClickListener(v -> {
                db.deleteTask(task.getIdTask());
                removeAt(taskViewHolder.getBindingAdapterPosition());
            });

            taskViewHolder.titleView.setText(task.getTitle());
            taskViewHolder.titleView.setOnClickListener(v -> {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                UpdateTask.newInstance(task.getIdTask(),
                        task.getTitle(),
                        task.getText(),
                        task.getPriority()).show(manager, UpdateTask.TAG);
            });
        }

        if (holder instanceof NoTasksViewHolder) {
            NoTasksViewHolder noTasksViewHolder = (NoTasksViewHolder)holder;
            noTasksViewHolder.noTaskCardView.setOnClickListener(v -> Toast.makeText(v.getContext(), R.string.create_task_helper, Toast.LENGTH_LONG).show());
        }
    }

    public int getItemViewType(int position) {
        if (tasksList.size() == 0) return NO_TASKS_VIEW;
        else return TASK_VIEW;
    }

    @Override
    public int getItemCount() {
        if (tasksList.size() == 0) return 1;
        return tasksList.size();
    }

    public void setTasks(List<TaskModel> tasksList){
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    private void removeAt(int pos) {
        tasksList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, tasksList.size());
    }

    private int priorityToIcon(int priority) {
        switch (priority) {
            case 1:
                return R.drawable.priority_1;
            case 2:
                return R.drawable.priority_2;
            case 4:
                return R.drawable.priority_4;
            case 5:
                return R.drawable.priority_5;
            default:
                return R.drawable.priority_3;
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView textView, titleView, categoryView;
        ImageView priorityView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.RadioButton);
            titleView = itemView.findViewById(R.id.TitleView);
            textView = itemView.findViewById(R.id.TextView);
            categoryView = itemView.findViewById(R.id.CategoryView);
            priorityView = itemView.findViewById(R.id.PriorityView);
        }
    }

    public static class NoTasksViewHolder extends RecyclerView.ViewHolder {
        CardView noTaskCardView;
        public NoTasksViewHolder(@NonNull View itemView) {
            super(itemView);

            noTaskCardView = itemView.findViewById(R.id.noTaskCardView);
        }
    }
}
