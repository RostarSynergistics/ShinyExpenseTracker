package ca.ualberta.cs.shinyexpensetracker.framework;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.persistence.FilePersistenceStrategy;
import ca.ualberta.cs.shinyexpensetracker.persistence.GsonExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;

/**
 * Acts as an interface between an ExpenseClaim view and the ExpenseClaimList
 * model.
 * 
 * Use ExpenseClaimController.getInstance() to get the singleton object.
 */
public class ExpenseClaimController {
	private final IExpenseClaimListPersister persister;
	private ExpenseClaimList claimList;

	/**
	 * Default constructor.
	 * 
	 * Only use if you have a very good reason. Otherwise, just use
	 * Application.getExpenseClaimController().
	 * 
	 * @param context
	 *            The application's current context.
	 * @throws IOException
	 */
	public ExpenseClaimController(Context context) throws IOException {
		this(new GsonExpenseClaimListPersister(new FilePersistenceStrategy(context, "expenseClaims")));
	}

	/**
	 * Constructor. Use for testing only.
	 * 
	 * @param context
	 *            The application's current context.
	 * @throws IOException
	 */
	public ExpenseClaimController(IExpenseClaimListPersister persister) throws IOException {
		this.persister = persister;
		claimList = persister.loadExpenseClaims();
	}

	/**
	 * Adds a claim to the ClaimList model
	 * 
	 * @param claim
	 * @throws IOException
	 */
	public ExpenseClaim addExpenseClaim(String name, Date startDate, Date endDate) throws IOException {
		ExpenseClaim newClaim = new ExpenseClaim(name, startDate, endDate);

		claimList.addClaim(newClaim);
		persister.saveExpenseClaims(claimList);
		return newClaim;
	}

	/**
	 * Fetches a claim from the claim list model.
	 * 
	 * @param index
	 * @return the claim at the given index
	 */
	public ExpenseClaim getExpenseClaimAtPosition(int index) {
		return claimList.getClaimAtPosition(index);
	}

	/**
	 * Fetches a claim from the claim list model based on ID.
	 * 
	 * @param id
	 *            The ID of the claim to find.
	 * @return The claim if found, and null otherwise.
	 */
	public ExpenseClaim getExpenseClaimByID(UUID id) {
		return claimList.getClaimByID(id);
	}

	/**
	 * Deletes the claim with the specified ID from the model.
	 * 
	 * @param id
	 *            The ID of the claim to delete.
	 * @throws IOException
	 */
	public void deleteExpenseClaim(UUID id) throws IOException {
		claimList.deleteClaim(id);
		persister.saveExpenseClaims(claimList);
	}

	public ExpenseClaim updateExpenseClaim(UUID id, String name, Date startDate, Date endDate) throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(id);

		claim.setName(name);
		claim.setStartDate(startDate);
		claim.setEndDate(endDate);

		persister.saveExpenseClaims(claimList);
		return claim;
	}

	public ExpenseClaim updateExpenseClaimStatus(UUID id, ExpenseClaim.Status status) throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(id);
		claim.setStatus(status);
		persister.saveExpenseClaims(claimList);
		return claim;
	}

	public Destination addDestinationToClaim(UUID claimID, String name, String reasonForTravel) throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);
		Destination destination = new Destination(name, reasonForTravel);
		claim.addDestination(destination);
		persister.saveExpenseClaims(claimList);
		return destination;
	}

	public Destination updateDestinationOnClaim(UUID claimID, UUID destinationID, String name, String reasonForTravel)
			throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);
		Destination destination = claim.getDestinationByID(destinationID);

		destination.setName(name);
		destination.setReasonForTravel(reasonForTravel);

		persister.saveExpenseClaims(claimList);
		return destination;
	}

	public ExpenseItem addExpenseItemToClaim(UUID claimID, String name, Date date, Category category, BigDecimal amountSpent, Currency currency, String description, Bitmap photo)
			throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);
		ExpenseItem item = new ExpenseItem(name, date, category, amountSpent, currency, description, photo);

		claim.addExpense(item);

		persister.saveExpenseClaims(claimList);
		return item;
	}

	public ExpenseItem updateExpenseItemOnClaim(UUID claimID, UUID expenseItemID, String name, Date date, Category category, BigDecimal amountSpent, Currency currency, String description, Bitmap photo)
			throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);
		ExpenseItem item = claim.getExpenseItemByID(expenseItemID);

		item.setName(name);
		item.setDate(date);
		item.setCategory(category);
		item.setAmountSpent(amountSpent);
		item.setCurrency(currency);
		item.setDescription(description);
		item.setReceiptPhoto(photo);

		persister.saveExpenseClaims(claimList);
		return item;
	}

	public ExpenseClaim addTagsToClaim(UUID claimID, List<Tag> tags) throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);

		for (Tag tag : tags) {
			claim.addTag(tag);
		}

		persister.saveExpenseClaims(claimList);
		return claim;
	}

	public ExpenseClaim removeTagsFromClaim(UUID claimID, List<Tag> tags) throws IOException {
		ExpenseClaim claim = claimList.getClaimByID(claimID);

		for (Tag tag : tags) {
			claim.removeTag(tag);
		}

		persister.saveExpenseClaims(claimList);
		return claim;
	}

	/**
	 * @return the number of claims in the claim list
	 */
	public int getCount() {
		return claimList.getCount();
	}

	/**
	 * Sort the data on the model. Warning: This changes the indexes in the
	 * model.
	 */
	// XXX: May need to be changed. See #64 for details.
	public void sort() {
		claimList.sort();
	}

	/**
	 * Returns the model
	 * 
	 * @return
	 */
	public ExpenseClaimList getExpenseClaimList() {
		return claimList;
	}

	/**
	 * Returns the index of an expenseClaim
	 * 
	 * @param claim
	 * @return
	 */
	public int getIndexOf(ExpenseClaim claim) {
		return claimList.getClaims().indexOf(claim);
	}

	/**
	 * Returns and ArrayList of the expenseItems associated to a given claim
	 * 
	 * @param claim
	 * @return
	 */
	public ArrayList<ExpenseItem> getExpenseItems(ExpenseClaim claim) {
		return claim.getExpenses();
	}

	/**
	 * Removes the tag given to all claim's taglists. Used when a tag is deleted
	 * from the tag controller
	 * 
	 * @param tagController
	 */
	public void removeTag(Tag tag) {

		for (ExpenseClaim claim : claimList.getClaims()) {
			claim.getTagList().deleteTag(tag);
		}
	}
}
