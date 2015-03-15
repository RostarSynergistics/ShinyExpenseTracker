/** Covers Issue 5
 * Things to implement: saving of an expenseItem to an expenseClaim
 * @author Sarah Morris
 * @version 1.0
 * @since 2015-03-09
 *
 * ExpenseItem hold the name, date, category, amount spent, currency, description
 * and a photo of a receipt for an expense that is added to a claim.
 * 
 * To use:
 * 	ExpenseItem e = new ExpenseItem(String name, Date date, Category category, BigDecimal amountSpent, 
 * 							Currency currency, String description, Bitmap photo);
 */

package ca.ualberta.cs.shinyexpensetracker.models;

import java.math.BigDecimal;
import java.util.Date;
import android.graphics.Bitmap;

public class ExpenseItem extends Model <ExpenseItem> {

	public enum Category { 
		AIR_FARE("air fare"),
		GROUND_TRANSPORT("ground transport"), 
		VEHICLE_RENTAL("vehicle rental"), 
		PRIVATE_AUTOMOBILE("private automobile"), 
		FUEL("fuel"), 
		PARKING("parking"), 
		REGISTRATION("registration"), 
		ACCOMODATION("accomodation"), 
		MEAL("meal"), 
		SUPPLIES("supplies");

		private final String text;
		
		private Category(final String text){
			this.text = text;
		}
		
		public String getText(){
			return this.text;
		}
		
		public static Category fromString(String text) {
			if (text != null) {
				for (Category c : Category.values()) {
					if (text.equalsIgnoreCase(c.text)) {
						return c;
					}
				}
			}
			return null;
		}
	}
		
	public enum Currency {
		CAD, USD, GBP, EUR, CHF, JPY, CNY
		
	}

	public String name;
	public Date date;
	public Category category;
	public BigDecimal amountSpent;
	public Currency currency;
	public String description;
	public Bitmap receiptPhoto;
	private boolean hasPhoto;
	
	public ExpenseItem (String name, Date date, Category category, 
			BigDecimal amountSpent, Currency currency, String description, Bitmap photo){
		this.name = name;
		this.date = date;
		this.category = category;
		this.amountSpent = amountSpent;
		this.currency = currency;
		this.description = description;
		this.receiptPhoto = photo;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
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
	
	public void setReceiptPhoto(Bitmap photo) {
		this.receiptPhoto = photo;
	}
	
	public Bitmap getReceiptPhoto(){
		return this.receiptPhoto;
	}

	public boolean doesHavePhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}
}
