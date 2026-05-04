package com.example.manajementugas;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title;
    private String course;
    private String priority; // "TINGGI", "SEDANG", "RENDAH"
    private String deadline;
    private boolean isCompleted;

    public Task(int id, String title, String course, String priority, String deadline, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.course = course;
        this.priority = priority;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
