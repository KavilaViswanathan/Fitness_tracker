package com.fitness;

import com.fitness.models.Activity;
import com.fitness.models.ActivityType;
import com.fitness.models.User;
import com.fitness.services.FitnessTracker;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    private static final String[] MENU_OPTIONS = {
            "Register User",
            "Add Activity",
            "Update Activity",
            "Delete Activity",
            "List Activities",
            "Exit"
    };

    public static void main(String[] args) {
        try {
            FitnessTracker tracker = new FitnessTracker();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                displayMenu();

                int choice = getChoice(scanner);

                switch (choice) {
                    case 1:
                        registerUser(scanner, tracker);
                        break;
                    case 2:
                        addActivity(scanner, tracker);
                        break;
                    case 3:
                        updateActivity(scanner, tracker);
                        break;
                    case 4:
                        deleteActivity(scanner, tracker);
                        break;
                    case 5:
                        listActivities(tracker);
                        break;
                    case 6:
                        exit(scanner, tracker);
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("\nSelect an option:");
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            System.out.println((i + 1) + ". " + MENU_OPTIONS[i]);
        }
    }

    private static int getChoice(Scanner scanner) {
        int choice = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= MENU_OPTIONS.length) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + MENU_OPTIONS.length + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // consume non-integer input
            }
            scanner.nextLine(); // consume newline left-over
        }
        return choice;
    }

    private static void registerUser(Scanner scanner, FitnessTracker tracker) throws SQLException {
        System.out.println("Enter user details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        System.out.print("Weight: ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // consume newline left-over

        System.out.print("Height: ");
        double height = scanner.nextDouble();
        scanner.nextLine(); // consume newline left-over

        User user = new User(0, name, age, weight, height);
        tracker.addUser(user);
        System.out.println("User added successfully with ID: " + user.getId());
    }

    private static void addActivity(Scanner scanner, FitnessTracker tracker) throws SQLException {
        System.out.print("Enter user ID to add activity: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values());
        String activityList = activityTypes.stream().map(Enum::name).collect(Collectors.joining(", "));

        System.out.println("Enter activity details:");
        System.out.println("Activity Type (choose from " + activityList + "): ");
        String activityTypeStr = scanner.nextLine().toUpperCase();
        ActivityType activityType = getActivityType(activityTypes, activityTypeStr);

        if (activityType != null) {
            System.out.print("Duration (minutes): ");
            double duration = scanner.nextDouble();
            scanner.nextLine(); // consume newline left-over

            System.out.print("Distance (kilometers, optional): ");
            double distance = scanner.nextDouble();
            scanner.nextLine(); // consume newline left-over

            Activity activity = new Activity(0, userId, activityType, duration, distance);
            tracker.addActivity(activity);
            System.out.println("Activity added successfully with ID: " + activity.getId());
        } else {
            System.out.println("Invalid activity type. Please choose from: " + activityList);
        }
    }

    private static ActivityType getActivityType(List<ActivityType> activityTypes, String activityTypeStr) {
        try {
            return ActivityType.valueOf(activityTypeStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static void updateActivity(Scanner scanner, FitnessTracker tracker) throws SQLException {
        System.out.print("Enter activity ID to update: ");
        int activityIdToUpdate = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        Activity existingActivity = tracker.getActivityById(activityIdToUpdate);
        if (existingActivity != null) {
            List<ActivityType> activityTypes = Arrays.asList(ActivityType.values());
            String activityList = activityTypes.stream().map(Enum::name).collect(Collectors.joining(", "));

            System.out.println("Enter updated details for activity ID " + activityIdToUpdate + ":");
            System.out.println("Activity Type (choose from " + activityList + "): ");
            String updatedActivityTypeStr = scanner.nextLine().toUpperCase();
            ActivityType updatedActivityType = getActivityType(activityTypes, updatedActivityTypeStr);

            if (updatedActivityType != null) {
                System.out.print("Updated Duration (minutes): ");
                double updatedDuration = scanner.nextDouble();
                scanner.nextLine(); // consume newline left-over

                System.out.print("Updated Distance (kilometers, optional): ");
                double updatedDistance = scanner.nextDouble();
                scanner.nextLine(); // consume newline left-over

                existingActivity.setType(updatedActivityType);
                existingActivity.setDuration(updatedDuration);
                existingActivity.setDistance(updatedDistance);

                tracker.updateActivity(existingActivity);
                System.out.println("Activity updated successfully.");
            } else {
                System.out.println("Invalid activity type. Please choose from: " + activityList);
            }
        } else {
            System.out.println("Activity with ID " + activityIdToUpdate + " not found.");
        }
    }

    private static void deleteActivity(Scanner scanner, FitnessTracker tracker) throws SQLException {
        System.out.print("Enter activity ID to delete: ");
        int activityIdToDelete = scanner.nextInt();
        scanner.nextLine(); // consume newline left-over

        Activity activityToDelete = tracker.getActivityById(activityIdToDelete);
        if (activityToDelete != null) {
            tracker.deleteActivity(activityIdToDelete);
            System.out.println("Activity with ID " + activityIdToDelete + " deleted successfully.");
        } else {
            System.out.println("Activity with ID " + activityIdToDelete + " not found.");
        }
    }

    private static void listActivities(FitnessTracker tracker) throws SQLException {
        List<Activity> activities = tracker.getAllActivities();
        if (!activities.isEmpty()) {
            System.out.println("All activities:");
            for (Activity activity : activities) {
                System.out.println("Activity ID: " + activity.getId() + ", User ID: " + activity.getUserId() +
                        ", Type: " + activity.getType() + ", Duration: " + activity.getDuration() +
                        ", Distance: " + activity.getDistance());
            }
        } else {
            System.out.println("No activities found.");
        }
    }

    private static void exit(Scanner scanner, FitnessTracker tracker) throws SQLException {
        System.out.println("Exiting...");
        scanner.close();
        tracker.close();
    }
}
