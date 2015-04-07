package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.UUID;

/**
 * Stores the users id, name and the type (Approver or Claimant) they are logged
 * in with.
 * 
 */
public class User {
	private UUID id;
	private String name;
	private Coordinate homeGeolocation;

	public User(UUID userID, String name) {
		this.id = userID;
		this.name = name;
	}

	public User(String name) {
		this(UUID.randomUUID(), name);
	}

	public void setUserID(UUID id) {
		this.id = id;
	}

	public UUID getUserID() {
		return id;
	}

	public void setUserName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return name;
	}

	public Coordinate getHomeGeolocation() {
		return homeGeolocation;
	}

	public void setHomeGeolocation(Coordinate homeGeolocation) {
		this.homeGeolocation = homeGeolocation;
	}
}
