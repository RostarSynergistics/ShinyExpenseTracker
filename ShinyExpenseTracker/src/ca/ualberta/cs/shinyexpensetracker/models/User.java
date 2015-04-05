package ca.ualberta.cs.shinyexpensetracker.models;

import java.io.IOException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.persistence.FilePersistenceStrategy;
import ca.ualberta.cs.shinyexpensetracker.persistence.GsonUserPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IUserPersister;

/**
 * Stores the users id, name and the type (Approver or Claimant) they are logged in with.  
 *
 */
public class User {

	public enum Type {
		Approver(),
		Claimant();
	}
	
	private int id;
	private String name;
	private Type type;
	private Coordinate homeGeolocation;
	
	public User() {
		
	}
	
	public User(Context context) throws IOException {
		
		this (new GsonUserPersister( new FilePersistenceStrategy(context, "user")));
	}

	
	public User(IUserPersister gsonUserPersister) throws IOException {
		gsonUserPersister.loadUser();
	}


	public void setUserId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return id;
	}
	
	public void setUserName(String name) {
		this.name = name;
	}
	
	public String getUserName() {
		return name;
	}
	
	public void setUserType(Type type) {
		this.type = type;
	}
	
	public Type getUserType() {
		return type;
	}

	public Coordinate getHomeGeolocation() {
		return homeGeolocation;
	}

	public void setHomeGeolocation(Coordinate homeGeolocation) {
		this.homeGeolocation = homeGeolocation;
	}
	
}
