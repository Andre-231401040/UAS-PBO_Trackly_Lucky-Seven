package org.example.trackly.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.trackly.config.DBConfig;
import org.example.trackly.model.Task;
import org.example.trackly.model.User;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TaskService {
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        User user = Session.getInstance().getUser();

        if (user == null) {
            return tasks;
        }

        String query = "SELECT * FROM tasks WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, user.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("with_deadline"),
                            rs.getTimestamp("deadline"),
                            rs.getString("status"),
                            rs.getInt("user_id")
                    );
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<Task> getTasksByStatus(String status) {
        List<Task> tasks = new ArrayList<>();
        User user = Session.getInstance().getUser();
        String query = "SELECT * FROM tasks WHERE status = ? AND user_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2,user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = new Task(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("with_deadline"),
                        rs.getTimestamp("deadline"),
                        rs.getString("status"),
                        rs.getInt("user_id")
                );

                tasks.add(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public boolean addTask(String title, String description, boolean withDeadline, Timestamp deadline, String status) {
        // Ambil user yang sedang login
        User user = Session.getInstance().getUser();
        if (user == null) {
            System.err.println("Tidak ada user yang login.");
            return false;
        }

        String query = "INSERT INTO tasks (title, description, with_deadline, deadline, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setBoolean(3, withDeadline);
            stmt.setTimestamp(4, deadline);
            stmt.setString(5, status);
            stmt.setInt(6, user.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTask(int id, String title, String description, boolean withDeadline, Timestamp deadline) {
        String sql = "UPDATE tasks SET title = ?, description = ?, with_deadline = ?, deadline = ? WHERE id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setBoolean(3, withDeadline);

            if (deadline != null) {
                stmt.setTimestamp(4, deadline);
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setInt(5, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateTaskStatus(int taskId, String newStatus) {
        String query = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, taskId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteTaskNotification(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure to continue delete?");
        alert.setContentText("Title: " + task.getTitle());

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deletionSuccessful = deleteTask(task.getId());
            if (deletionSuccessful) {
                NavigationUtil.handleNavigation("task_list_view.fxml");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean deleteTask(Integer taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
