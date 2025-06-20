package org.example.trackly.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConfig {
    private static final String JDBC_URL = "jdbc:postgresql://ep-solitary-cloud-a1nkymho-pooler.ap-southeast-1.aws.neon.tech/trackly?sslmode=require";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, "trackly_owner", "npg_m3AsDrjdIT7t");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed.");
            return null;
        }
    }
}
