/**
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Covers Issue 5
 * Things to implement: saving of an expenseItem to an expenseClaim
 * @author Sarah Morris
 * @version 1.0
 * @since 2015-03-09
 *
 * ExpenseItem hold the name, date, category, amount spent, currency, description
 * and a photo of a receipt for an expense that is added to a claim.
 */

package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Date;

import ca.ualberta.cs.shinyexpensetracker.IView;

import android.graphics.Bitmap;

public class ExpenseItem implements IModel<IView<ExpenseItem>> {

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
	
	private ArrayList<IView<ExpenseItem>> views;
	
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
