package org.example.trackly.model;

import java.sql.Timestamp;

public class Task {
    private Integer id;
    private String title;
    private String description;
    private boolean with_deadline;
    private Timestamp deadline;
    private String status;
    private Integer user_id;

    public Task(Integer id, String title, String description, boolean with_deadline, Timestamp deadline, String status, Integer user_id) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setWithDeadline(with_deadline);
        setDeadline(deadline);
        setStatus(status);
        setUserId(user_id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public boolean getWithDeadline() {
        return with_deadline;
    }

    public void setWithDeadline(boolean with_deadline) {
        this.with_deadline = with_deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
}
