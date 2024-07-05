package com.fitness.iservices;

import com.fitness.models.Activity;
import com.fitness.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IFitnessTracker {
    void addUser(User user) throws SQLException;
    void addActivity(Activity activity) throws SQLException;
    List<Activity> getActivitiesByUser(int userId) throws SQLException;
    Activity getActivityById(int activityId) throws SQLException;
    void updateActivity(Activity activity) throws SQLException;
    void deleteActivity(int activityId) throws SQLException;
    List<Activity> getAllActivities() throws SQLException; // Added this method
}