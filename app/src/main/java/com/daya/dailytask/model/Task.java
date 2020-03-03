package com.daya.dailytask.model;

public class Task {
    private int id;
    private String taskName;
    private int taskDuration;
    private int taskPriority;
    private String taskType;
    public Task(){

    }

    public Task(int id, String taskName, int taskDuration, int taskPriority, String taskType) {
        this.id = id;
        this.taskName = taskName;
        this.taskDuration = taskDuration;
        this.taskPriority = taskPriority;
        this.taskType = taskType;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


}
