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
import java.text.ParseException;
import java.util.Date;

import android.graphics.Bitmap;

/**
 * Houses ExpenseItem Data.  Extends Model
 * 
 * Has name: String,
 * 	   date: Date,
 * 	   category: enum Category
 *     amountSpent: BigDecimal
 *     currency: enum Currency
 *     description: String
 *     recieptPhoto: Bitmap
 * 
 *
 */
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
		
		public String toString(){
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
	public boolean incompletenessMarker;
	
	public static final boolean COMPLETE = false;
	public static final boolean INCOMPLETE = true;
	
	/**
	 * Generalized constructor.
	 * @param name
	 * @param date
	 * @param category
	 * @param amountSpent
	 * @param currency
	 * @param description
	 * @param photo
	 * @param completenessFlag
	 */
	public ExpenseItem (String name, Date date, Category category, 
			BigDecimal amountSpent, Currency currency, String description,
			Bitmap photo, boolean completenessFlag) {
		this.name = name;
		this.date = date;
		this.category = category;
		this.amountSpent = amountSpent;
		this.currency = currency;
		this.description = description;
		this.receiptPhoto = photo;
		this.incompletenessMarker = completenessFlag;
	}
	
	
	public ExpenseItem (String name, Date date, Category category, 
			BigDecimal amountSpent, Currency currency, String description, Bitmap photo) {
		// Call to more specific constructor
		this(name, date, category, amountSpent, currency, description,
				photo, false);
	}

	
	public ExpenseItem (String name, Date date, Category category, 
			BigDecimal amountSpent, Currency currency, String description) {
		// Call to more specific constructor
		this(name, date, category, amountSpent, currency, description, null, false);
	}
	
	public ExpenseItem(String name, Date date, Category category,
			BigDecimal amount, Currency currency) {
		// Call to more specific constructor
		this(name, date, category, amount, currency, "", null, false);
	}

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public void setDate(Date date) throws ParseException{
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

	// XXX: #69 <- This should return the formatted JodaMoney string
	public String getValueString() {
		return new StringBuilder()
					.append(getAmountSpent())
					.append(" ")
					.append(getCurrency().toString())
			.toString();
	}

	/**
	 * returns true is expenseItem has a receipt
	 * @return
	 */
	public boolean doesHavePhoto() {
		return this.receiptPhoto != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		ExpenseItem other = (ExpenseItem) obj;
		if (!this.getName().equals(other.getName()) ){
			return false;
		}
		else if(!this.getDate().equals(other.getDate())){
			return false;
		}
		else if(!this.getDescription().equals(other.getDescription())){
			return false;
		}
		else if(!this.getCategory().equals(other.getCategory())){
			return false;
		}
		else if(!this.getAmountSpent().equals(other.getAmountSpent())){
			return false;
		}
		else if(!this.getCurrency().equals(other.getCurrency())){
			return false;
		}
		else if(!this.getReceiptPhoto().sameAs(other.getReceiptPhoto())){
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the incompleteness markers. You should use ExpenseItem.COMPLETE
	 * or ExpenseItem.INCOMPLETE for clarity.
	 * @param isIncomplete
	 */
	public void setIncompletenessMarker(boolean isIncomplete) {
		incompletenessMarker = isIncomplete;
		notifyViews();
	}
	
	/**
	 * Mark as complete (turn off marker)
	 */
	public void markComplete() {
		setIncompletenessMarker(COMPLETE);
	}
	
	/**
	 * Mark as incomplete (turn on marker)
	 */
	public void markIncomplete() {
		setIncompletenessMarker(INCOMPLETE);
	}
	
	/**
	 * Toggles the incompleteness marker
	 */
	public void toggleIncompletenessMarker() {
		if (isMarkedIncomplete()) {
			markComplete();
		} else {
			markIncomplete();
		}
	}
	
	/**
	 * @return true if the expense is marked incomplete, false otherwise.
	 */
	public boolean isMarkedIncomplete() {
		return incompletenessMarker;
	}
	
}
