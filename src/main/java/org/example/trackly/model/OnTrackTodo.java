package org.example.trackly.model;

import java.sql.Timestamp;

public class OnTrackTodo extends Task {
    public OnTrackTodo(Integer id, String title, String description, boolean with_deadline, Timestamp deadline, String status, Integer user_id) {
        super(id, title, description, with_deadline, deadline, status, user_id);
    }
}
