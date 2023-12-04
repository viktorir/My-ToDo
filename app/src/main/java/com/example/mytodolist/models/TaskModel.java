package com.example.mytodolist.models;

public class TaskModel {
    private int idTask, categoryId, priority;
    private String title, text, deadline, categoryName;
    private boolean isDone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getText() {
        return text;
    }

    public void setText(String text) { this.text = text; }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) { this.priority = priority; }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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
