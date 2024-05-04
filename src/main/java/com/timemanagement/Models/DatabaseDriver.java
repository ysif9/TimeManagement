package com.timemanagement.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {

    private static final String DB_NAME = "tasks.db";
    private static final String TABLE_NAME = "tasks";

    public static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "task_name TEXT NOT NULL,"
                + "deadline DATE,"
                + "time_spent DOUBLE,"
                + "completed INTEGER DEFAULT 0"
                + ")";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveTask(Task task) throws SQLException, ClassNotFoundException {
        String sql = "INSERT OR REPLACE INTO " + TABLE_NAME + " (id, task_name, deadline, time_spent, completed) VALUES (?, ?, ?, ?, ?)";  // Removed deleted parameter

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, task.idProperty().get());
            stmt.setString(2, task.taskNameProperty().get());
            stmt.setDate(3, task.deadlineProperty().getValue() != null ? Date.valueOf(task.deadlineProperty().getValue()) : null);
            stmt.setDouble(4, task.timeSpentInMinutesProperty().get());
            stmt.setInt(5, task.completedProperty().get() ? 1 : 0);
            stmt.execute();
        }
    }

    public static void deleteTask(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    public static List<Task> loadAllTasks() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM " + TABLE_NAME;

        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int taskId = rs.getInt("id");
                String taskName = rs.getString("task_name");
                LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                double timeSpent = rs.getDouble("time_spent");
                boolean completed = rs.getInt("completed") == 1;
                tasks.add(new Task(taskId, taskName, deadline, timeSpent, completed));
            }
        }
        return tasks;
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
    }
}

