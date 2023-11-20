package com.example.mytodolist.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskModel {
    private int idTask, categoryId, priority;
    private String title, text, deadline, categoryName;
    private boolean isDone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && !title.trim().equals(""))
            this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.trim().equals("")) text = null;
        this.text = text;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority > 0 && priority < 6)
            this.priority = priority;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDeadline(Date deadline) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.deadline = fmt.format(deadline);
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
