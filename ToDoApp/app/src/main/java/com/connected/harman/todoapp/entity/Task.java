package com.connected.harman.todoapp.entity;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Entity class for Task
 */
public class Task {

    String taskName;
    String taskDescription;
    long taskDate;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    int taskId;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public long getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(long taskDate) {
        this.taskDate = taskDate;
    }
}
