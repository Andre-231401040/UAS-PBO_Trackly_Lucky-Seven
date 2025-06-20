package org.example.trackly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.trackly.model.OnTrackTodo;
import org.example.trackly.model.OverdueTodo;
import org.example.trackly.model.Task;
import org.example.trackly.model.User;
import org.example.trackly.service.TaskService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TodoController {
    @FXML
    private Hyperlink taskList;
    @FXML
    private Hyperlink todo;
    @FXML
    private Hyperlink completed;
    @FXML
    private Hyperlink setting;
    @FXML
    private ImageView sidebarProfileImageView;
    @FXML
    private Label sidebarNameLabel;
    @FXML
    private Button onTrackButton;
    @FXML
    private Button overdueButton;
    @FXML
    private VBox toDoListContainer;

    private boolean isOnTrackActive = true;
    private List<OnTrackTodo> onTrackTodos;
    private List<OverdueTodo> overdueTodos;
    private final TaskService taskService = new TaskService();

    @FXML
    public void initialize() {
        User currentUser = Session.getInstance().getUser();
        if(currentUser != null) {
            sidebarNameLabel.setText(currentUser.getUsername());

            if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
                File imageFile = new File("user_data/profile/" + currentUser.getProfileImage());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    sidebarProfileImageView.setImage(image);
                } else {
                    // fallback ke default
                    Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                    sidebarProfileImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                sidebarProfileImageView.setImage(defaultImage);
            }
        }

        List<Task> tasks = taskService.getTasksByStatus("To Do");

        LocalDateTime now = LocalDateTime.now();

        onTrackTodos = tasks.stream()
                .filter(task -> task.getDeadline() == null || !task.getDeadline().toLocalDateTime().isBefore(now))
                .map(task -> new OnTrackTodo(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getWithDeadline(),
                        task.getDeadline(),
                        task.getStatus(),
                        task.getUserId()
                ))
                .collect(Collectors.toList());

        overdueTodos = tasks.stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().toLocalDateTime().isBefore(now))
                .map(task -> new OverdueTodo(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getWithDeadline(),
                        task.getDeadline(),
                        task.getStatus(),
                        task.getUserId()
                ))
                .collect(Collectors.toList());

        loadOnTrackTodos();
    }

    public void handleOnTrackClick() {
        isOnTrackActive = true;
        loadOnTrackTodos();
        setActiveButton(onTrackButton, overdueButton);
    }

    public void handleOverdueClick() {
        isOnTrackActive = false;
        loadOverdueTodos();
        setActiveButton(overdueButton, onTrackButton);
    }

    private void loadOnTrackTodos() {
        toDoListContainer.getChildren().clear(); // bersihkan isi lama

        for (OnTrackTodo onTrackTodo : onTrackTodos) {
            toDoListContainer.getChildren().add(buildTaskCard(onTrackTodo));
        }
    }

    private void loadOverdueTodos() {
        toDoListContainer.getChildren().clear(); // bersihkan isi lama

        for (OverdueTodo overdueTodo : overdueTodos) {
            toDoListContainer.getChildren().add(buildTaskCard(overdueTodo));
        }
    }

    private VBox buildTaskCard(Task todo) {
        VBox taskBox = new VBox();
        taskBox.setStyle("-fx-background-color: #DBEAFE; -fx-background-radius: 10;");
        taskBox.setPadding(new Insets(8));
        VBox.setMargin(taskBox, new Insets(0, 0, 20, 0));
        taskBox.setSpacing(20);
        taskBox.setPrefHeight(152);

        // Title
        Label title = new Label(todo.getTitle());
        title.setStyle("-fx-text-fill: #4682B4;");
        title.setPrefHeight(60);
        title.setFont(Font.font("Segoe UI Bold", 18));

        // Description
        Label desc = new Label(todo.getDescription());
        desc.setWrapText(true);
        desc.setFont(Font.font("Segoe UI", 14));

        // Deadline
        HBox deadlineBox = new HBox(5);
        deadlineBox.setAlignment(Pos.CENTER_LEFT);
        deadlineBox.setPrefWidth(750);

        ImageView calendarIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/calendar.png")));
        calendarIcon.setFitWidth(30);
        calendarIcon.setFitHeight(30);

        if(todo.getDeadline() != null) {
            String formattedDeadline = todo.getDeadline().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"));
            Label deadlineLabel = new Label(formattedDeadline);
            deadlineLabel.setTextFill(Color.web("#9ca3af"));
            deadlineLabel.setFont(Font.font("Segoe UI Bold", 14));

            deadlineBox.getChildren().addAll(calendarIcon, deadlineLabel);
        }

        // Status Icon
        Hyperlink completeLink = new Hyperlink();
        completeLink.setStyle("-fx-border-color: transparent;");
        ImageView checkIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/check.png")));
        checkIcon.setFitWidth(40);
        checkIcon.setFitHeight(40);
        completeLink.setGraphic(checkIcon);
        completeLink.setPrefWidth(200);
        completeLink.setAlignment(Pos.TOP_RIGHT);
        completeLink.setOnAction(event -> {
            taskService.updateTaskStatus(todo.getId(), "Completed");
            initialize();
            if (isOnTrackActive) {
                loadOnTrackTodos();
            } else {
                loadOverdueTodos();
            }
        });

        HBox actionRow = new HBox(deadlineBox, completeLink);
        HBox.setHgrow(deadlineBox, Priority.ALWAYS);

        // Add to task box
        taskBox.getChildren().addAll(title, desc, actionRow);

        return taskBox;
    }

    private void setActiveButton(Button active, Button inactive) {
        active.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 8;");
        inactive.setStyle("-fx-background-color: white; -fx-text-fill: #4682B4; -fx-border-color: #4682B4; -fx-border-width: 2; -fx-border-radius: 8;");
    }

    public void navigationHandler(ActionEvent event) {
        NavigationUtil.handleNavigation(event, null, null, taskList, todo, completed, setting, null);
    }
}
