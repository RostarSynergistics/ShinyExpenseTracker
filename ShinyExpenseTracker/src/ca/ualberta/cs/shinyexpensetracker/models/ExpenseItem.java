package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.Date;
import android.graphics.Bitmap;

public class ExpenseItem extends Model<ExpenseItem> {
	
	public Date date;
	public String description;
	public String category;
	public double amountSpent;	//FIXME doubles are bad for currencies
	public String currency;
	public Bitmap reciptPhoto;
	
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
	
	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return this.category;
	}
	
	public void setAmountSpent(double amountSpent){
		this.amountSpent = amountSpent;
	}
	
	public double getAmountSpent(){
		return this.amountSpent;
	}
	
	public void setCurrency(String currency){
		this.currency = currency;
	}
	
	public String getCurrency(){
		return this.currency;
	}
	
	public void setReciptPhoto(Bitmap photo) {
		this.reciptPhoto = photo;
	}
	
	public Bitmap getReciptPhoto(){
		return this.reciptPhoto;
	}
}
