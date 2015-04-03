package ca.ualberta.cs.shinyexpensetracker.models;

public class User {

	public enum Type {
		Approver(),
		Claimant();
	}
	
	private int id;
	private String name;
	private Type type;
	
	

	
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
}
