package com.example.mytodolist.models;

public class SubtaskModel {
    private int idSubtask, taskId;
    private String title, text;
    private boolean isDone;

    public int getIdSubtask() {
        return idSubtask;
    }

    public void setIdSubtask(int idSubtask) {
        this.idSubtask = idSubtask;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}
