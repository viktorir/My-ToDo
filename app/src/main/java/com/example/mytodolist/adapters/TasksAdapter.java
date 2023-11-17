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

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

    private List<TaskModel> tasksList;
    Context context;
    DataBaseHelper db;

    public TasksAdapter(Context context, DataBaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MyViewHolder(inflater.inflate(R.layout.task_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TaskModel task = tasksList.get(position);

        if (task.getCategoryId() == 0) holder.categoryView.setText("No category");
        else holder.categoryView.setText(String.valueOf(task.getCategoryId()));
        holder.textView.setText(task.getText());

        if (task.getText() == null) holder.textView.setVisibility(View.GONE);
        else holder.textView.setVisibility(View.VISIBLE);

        switch (task.getPriority()) {
            case 1:
                holder.priorityView.setImageResource(R.drawable.priority_1);
                break;
            case 2:
                holder.priorityView.setImageResource(R.drawable.priority_2);
                break;
            case 3:
                holder.priorityView.setImageResource(R.drawable.priority_3);
                break;
            case 4:
                holder.priorityView.setImageResource(R.drawable.priority_4);
                break;
            case 5:
                holder.priorityView.setImageResource(R.drawable.priority_5);
                break;
        }

        if (task.getIsDone()) {
            holder.radioButton.setChecked(true);
            holder.radioButton.setSelected(true);
        } else {
            holder.radioButton.setChecked(false);
            holder.radioButton.setSelected(false);
        }

        holder.titleView.setText(task.getTitle());
        holder.titleView.setHint(String.valueOf(task.getIdTask()));
        holder.titleView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView textView, titleView, categoryView;
        ImageView priorityView;

        public MyViewHolder(@NonNull View itemView) {
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
}
