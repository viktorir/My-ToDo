package com.example.mytodolist.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.UpdateTask;
import com.example.mytodolist.models.TaskModel;
import com.example.mytodolist.utils.DataBaseHelper;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TASK_VIEW = R.layout.task_item_layout;
    private static final int NO_TASKS_VIEW = R.layout.no_tasks_layout;
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
        if (viewType == NO_TASKS_VIEW) viewHolder = new NoTasksViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolder) {
            final TaskModel task = tasksList.get(position);

            if (task.getCategoryId() == 0) ((TaskViewHolder)holder).categoryView.setText("No category");
            else ((TaskViewHolder)holder).categoryView.setText(task.getCategoryName());

            ((TaskViewHolder)holder).textView.setText(task.getText());

            if (task.getText() == null) ((TaskViewHolder)holder).textView.setVisibility(View.GONE);
            else ((TaskViewHolder)holder).textView.setVisibility(View.VISIBLE);

            switch (task.getPriority()) {
                case 1:
                    ((TaskViewHolder)holder).priorityView.setImageResource(R.drawable.priority_1);
                    break;
                case 2:
                    ((TaskViewHolder)holder).priorityView.setImageResource(R.drawable.priority_2);
                    break;
                case 3:
                    ((TaskViewHolder)holder).priorityView.setImageResource(R.drawable.priority_3);
                    break;
                case 4:
                    ((TaskViewHolder)holder).priorityView.setImageResource(R.drawable.priority_4);
                    break;
                case 5:
                    ((TaskViewHolder)holder).priorityView.setImageResource(R.drawable.priority_5);
                    break;
            }

            if (task.getIsDone()) {
                ((TaskViewHolder)holder).radioButton.setChecked(true);
                ((TaskViewHolder)holder).radioButton.setSelected(true);
            } else {
                ((TaskViewHolder)holder).radioButton.setChecked(false);
                ((TaskViewHolder)holder).radioButton.setSelected(false);
            }

            ((TaskViewHolder)holder).titleView.setText(task.getTitle());
            ((TaskViewHolder)holder).titleView.setHint(String.valueOf(task.getIdTask()));
            ((TaskViewHolder)holder).titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    UpdateTask.newInstance(task.getIdTask(),
                            task.getTitle(),
                            task.getText(),
                            task.getPriority()).show(manager, UpdateTask.TAG);
                }
            });
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

    public class TaskViewHolder extends RecyclerView.ViewHolder {

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

            if (radioButton.isSelected()) {
                DataBaseHelper db = new DataBaseHelper(context);
                db.deleteTask(Integer.parseInt((String)titleView.getHint()));
                removeAt(getAdapterPosition());
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHelper db = new DataBaseHelper(context);
                    db.deleteTask(Integer.parseInt((String)titleView.getHint()));
                    removeAt(getAdapterPosition());
                }
            });
        }
    }

    public class NoTasksViewHolder extends RecyclerView.ViewHolder {

        public NoTasksViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
