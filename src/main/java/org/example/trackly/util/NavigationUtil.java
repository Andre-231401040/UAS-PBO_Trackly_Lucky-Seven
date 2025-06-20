package org.example.trackly.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import org.example.trackly.Main;

public class NavigationUtil {
    public static void handleNavigation(String path) {
        try {
            Main.changeScene(path);
        } catch(Exception e) {
            System.err.println("Failed to load application.");
            e.printStackTrace();
        }
    }

    public static void handleNavigation(ActionEvent event,
                                        Hyperlink registerLink,
                                        Hyperlink loginLink,
                                        Hyperlink taskList,
                                        Hyperlink todo,
                                        Hyperlink completed,
                                        Hyperlink setting,
                                        Button addTask) {
        try {
            Node source = (Node) event.getSource();

            String path;
            if(source == registerLink) {
                path = "register_view.fxml";
            } else if(source == loginLink) {
                path = "login_view.fxml";
            } else if(source == taskList) {
                path = "task_list_view.fxml";
            } else if(source == todo) {
                path = "todo_view.fxml";
            } else if(source == completed) {
                path = "completed_view.fxml";
            } else if(source == addTask) {
                path = "add_task_view.fxml";
            } else {
                path = "setting_view.fxml";
            }

            Main.changeScene(path);
        } catch(Exception e) {
            System.err.println("Failed to load application.");
        }
    }
}
