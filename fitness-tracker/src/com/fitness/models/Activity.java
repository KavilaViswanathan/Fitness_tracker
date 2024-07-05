package com.fitness.models;

public class Activity {
	private int id;
	private int userId;
	private ActivityType type;
	private double duration;
	private double distance;
	public Activity(int id, int userId, ActivityType type, double duration, double distance) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.duration = duration;
        this.distance = distance;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
}
