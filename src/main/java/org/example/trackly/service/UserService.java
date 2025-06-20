package org.example.trackly.service;

import org.example.trackly.config.DBConfig;
import org.example.trackly.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    public boolean updateUser(User user) {
        // contoh update ke database
        try (Connection conn = DBConfig.getConnection()) {
            String sql = "UPDATE users SET username = ?, phone_number = ?, profile_image = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPhoneNumber());
            stmt.setString(3, user.getProfileImage());
            stmt.setInt(4, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
