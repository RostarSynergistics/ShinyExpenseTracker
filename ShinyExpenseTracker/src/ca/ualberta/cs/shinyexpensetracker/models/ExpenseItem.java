package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.Date;
import org.joda.money.Money;
import android.graphics.Bitmap;

/**
 * ExpenseItem holds the name, date, category, amount spent, description and a
 * photo of a receipt for an expense that is added to a claim.
 * 
 * TODO: saving of an expenseItem to an expenseClaim
 */
public class ExpenseItem extends Model<ExpenseItem> {

	public enum Category {
		AIR_FARE("air fare"), 
		GROUND_TRANSPORT("ground transport"), 
		VEHICLE_RENTAL( "vehicle rental"), 
		PRIVATE_AUTOMOBILE("private automobile"), 
		FUEL( "fuel"), 
		PARKING("parking"), 
		REGISTRATION("registration"), 
		ACCOMODATION( "accomodation"), 
        MEAL("meal"), 
        SUPPLIES("supplies");

		private final String text;

		private Category(final String text) {
			this.text = text;
		}

		public String getText() {
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

	private String name;
	private Date date;
	private Category category;
	private Money amountSpent;
	private String description;
	private Bitmap receiptPhoto;

	/**
	 * Primary constructor for ExpenseItem
	 * 
	 * @param name Its name.
	 * @param date The date the expense was incurred.
	 * @param category Its category
	 * @param amountSpent Amount spent (value and type of currency)
	 * @param description Its description
	 * @param photo A photograph of the receipt.
	 */
	public ExpenseItem(String name, Date date, Category category,
			Money amountSpent, String description, Bitmap photo) {
		this.name = name;
		this.date = date;
		this.category = category;
		this.amountSpent = amountSpent;
		this.description = description;
		this.receiptPhoto = photo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setAmountSpent(Money amountSpent) {
		this.amountSpent = amountSpent;
	}

	public Money getAmountSpent() {
		return this.amountSpent;
	}

	public void setReceiptPhoto(Bitmap photo) {
		this.receiptPhoto = photo;
	}

	public Bitmap getReceiptPhoto() {
		return this.receiptPhoto;
	}
}
