package org.example.trackly.util;

import org.example.trackly.model.User;

public class Session {
    public User currentUser;
    private static Session instance;

    private Session(User user) {
        this.currentUser = user;
    }

    public static void login(User user) {
        if(instance == null) {
            instance = new Session(user);
        }
    }

    public static Session getInstance() {
        return instance;
    }

    public User getUser() {
        return currentUser;
    }
}
