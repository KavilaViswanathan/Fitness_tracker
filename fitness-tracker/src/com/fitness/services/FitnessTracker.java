package com.fitness.services;

import com.fitness.iservices.IFitnessTracker;
import com.fitness.models.Activity;
import com.fitness.models.ActivityType;
import com.fitness.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessTracker implements IFitnessTracker {
    private Connection connection;

    public FitnessTracker() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitness_db", "root", "root");
    }

    @Override
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, age, weight, height) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setInt(2, user.getAge());
            statement.setDouble(3, user.getWeight());
            statement.setDouble(4, user.getHeight());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void addActivity(Activity activity) throws SQLException {
        String query = "INSERT INTO activities (user_id, type, duration, distance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, activity.getUserId());
            statement.setString(2, activity.getType().toString());
            statement.setDouble(3, activity.getDuration());
            statement.setDouble(4, activity.getDistance());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    activity.setId(generatedKeys.getInt(1));
                    System.out.println("Activity added successfully with ID: " + activity.getId());
                }
            }
        }
    }

    @Override
    public List<Activity> getActivitiesByUser(int userId) throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activities WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    activities.add(new Activity(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            ActivityType.valueOf(resultSet.getString("type")),
                            resultSet.getDouble("duration"),
                            resultSet.getDouble("distance")
                    ));
                }
            }
        }
        return activities;
    }

    @Override
    public Activity getActivityById(int activityId) throws SQLException {
        String query = "SELECT * FROM activities WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Activity(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            ActivityType.valueOf(resultSet.getString("type")),
                            resultSet.getDouble("duration"),
                            resultSet.getDouble("distance")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updateActivity(Activity activity) throws SQLException {
        // Check if the activity exists
        Activity existingActivity = getActivityById(activity.getId());
        if (existingActivity != null) {
            String query = "UPDATE activities SET type=?, duration=?, distance=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, activity.getType().name());
                statement.setDouble(2, activity.getDuration());
                statement.setDouble(3, activity.getDistance());
                statement.setInt(4, activity.getId());
                statement.executeUpdate();
                System.out.println("Activity updated successfully.");
            }
        } else {
            System.out.println("Activity with ID " + activity.getId() + " not found.");
        }
    }

    @Override
    public void deleteActivity(int activityId) throws SQLException {
        // Check if the activity exists
        Activity existingActivity = getActivityById(activityId);
        if (existingActivity != null) {
            String query = "DELETE FROM activities WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, activityId);
                statement.executeUpdate();
                System.out.println("Activity with ID " + activityId + " deleted successfully.");
            }
        } else {
            System.out.println("Activity with ID " + activityId + " not found.");
        }
    }

    public List<Activity> getAllActivities() throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activities";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                activities.add(new Activity(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        ActivityType.valueOf(resultSet.getString("type")),
                        resultSet.getDouble("duration"),
                        resultSet.getDouble("distance")
                ));
            }
        }
        return activities;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
