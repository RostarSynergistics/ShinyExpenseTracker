package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Date;

import ca.ualberta.cs.shinyexpensetracker.IView;

import android.graphics.Bitmap;

public class ExpenseItem implements IModel<IView<ExpenseItem>> {
	
	public enum Category { 
		AIR_FARE, GROUND_TRANSPORT, VEHICLE_RENTAL, PRIVATE_AUTOMOBILE, FUEL, PARKING, 
		REGISTRATION, ACCOMODATION, MEAL, SUPPLIES

	}
	public enum Currency {
		CAD, USD, GBR, EUR, CHF, JPY, CNY
		
	}

	public String name;
	public Date date;
	public Category category;
	public BigDecimal amountSpent;
	public Currency currency;
	public String description;
	public Bitmap reciptPhoto;
	
	private ArrayList<IView<ExpenseItem>> views;
	
	public ExpenseItem (String name, Date date, Category category, 
			BigDecimal amountSpent, Currency currency, String description, Bitmap photo){
		this.name = name;
		this.date = date;
		this.category = category;
		this.amountSpent = amountSpent;
		this.currency = currency;
		this.description = description;
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
	
	public void setAmountSpent(BigDecimal amountSpent){
		this.amountSpent = amountSpent;
	}
	
	public BigDecimal getAmountSpent(){
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

	@Override
	public void addView(IView<ExpenseItem> v) {
		views.add(v);
	}

	@Override
	public void removeView(IView<ExpenseItem> v) {
		views.remove(v);
	}

	@Override
	public void notifyViews() {
		for (IView<ExpenseItem> v : views) {
			v.update(this);
		}
	}
	
}
