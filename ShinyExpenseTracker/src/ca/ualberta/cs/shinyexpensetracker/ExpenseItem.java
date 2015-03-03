package ca.ualberta.cs.shinyexpensetracker;

import java.util.Date;

import android.graphics.Bitmap;

public class ExpenseItem {

	public enum Category { 
		AIR_FARE, GROUND_TRANSPORT, VEHICLE_RENTAL, PRIVATE_AUTOMOBILE, FUEL, PARKING, 
		REGISTRATION, ACCOMODATION, MEAL, SUPPLIES
	}
	public enum Currency {
		CAD, USD, GBR, EUR, CHF, JPY, CNY
	}
	
	public Date date;
	public String description;
	public Category category;
	public double amountSpent;
	public Currency currency;
	public Bitmap reciptPhoto;

	public ExpenseItem (Date date, String description, Category category, 
			double amountSpent, Currency currency, Bitmap photo){
		this.date = date;
		this.description = description;
		this.category = category;
		this.amountSpent = amountSpent;
		this.currency = currency;
		this.reciptPhoto = photo;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return this.category;
	}
	
	public void setAmountSpent(double amountSpent){
		this.amountSpent = amountSpent;
	}
	
	public double getAmountSpent(){
		return this.amountSpent;
	}
	
	public void setCurrency(Currency currency){
		this.currency = currency;
	}
	
	public Currency getCurrency(){
		return this.currency;
	}
	
	public void setReciptPhoto(Bitmap photo) {
		this.reciptPhoto = photo;
	}
	
	public Bitmap getReciptPhoto(){
		return this.reciptPhoto;
	}
}
