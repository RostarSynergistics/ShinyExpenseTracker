// Covers Issue #5, 14, 15, 16

package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Collection;

import junit.framework.TestCase;

public class ExpenseItemListTest extends TestCase {

	    public void testEmptyExpenseItemList(){
	    	//test initialize ExpenseItemList
	    	ExpenseItemList expenseItemList = new expenseItemList();
	    	Collection<ExpenseItem> expenseItems = expenseItemLis.getExpenseItems();
	    	assertTrue("Empty Expense Item List", expenseItems.size() == 0);
	    }

	    public void testAddExpenseItem(){
	    	
	    	ExpenseItemList expenseItemList = new ExpenseItemList();
	    	
	    	// add sample expenseItem
	    	ExpenseItem testExpenseItem = new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency");
	    	expenseItemList.addExpenseItem(testExpenseItem);
	    	Collection<ExpenseItem> expenseItems = expenseItemList.getExpenseItems();
	    	assertTrue("ExpenseItemList size is not equal to 1", expenseItems.size() == 1);
	    	assertTrue("ExpenseItem not contained", expenseItems.contains(testExpenseItem));
	    }
	    
	    public void testEditExpenseItem() {
	    	
	    	ExpenseItemList expenseItemList = new ExpenseItemList();
	    	
	    	// add sample expenseItem
	    	ExpenseItem testExpenseItem = new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency");
	    	expenseItemList.addExpenseItem(testExpenseItem);
	    	
	    	// add edited expenseItem
	    	ExpenseItem testExpenseItem2 = new ExpenseItem("Description2", "Date2", "Category2", "Amount Spent2", "Unit of Currency2");
	    	expenseItemList.addExpenseItem(testExpenseItem);
	    	
	    	// edit and replace testExpenseItem with testExpenseItem2
	    	expenseItemList.editExpenseItem(testExpenseItem2, index);
	    	Collection<ExpenseItem> expenseItems = expenseItemList.getExpenseItems();
	    	
	    	assertTrue("ExpenseItemList size is not equal to 1", expenseItems.size() == 1);
	    	assertFalse("ExpenseItem1 is contained", expenseItems.contains(testExpenseItem));
	    	
	    	assertTrue("ExpenseItemList still has one expenseItem", expenseItems.size() == 1);
	    	assertTrue("ExpenseItem is not contained", expenseItems.contains(testExpenseItem2));
	    	assertFalse("Original expenseItem still contained", expenseItems.contains(testExpenseItem));
	    	
	    	
	    }
	        
	    public void testRemoveExpenseItem(){
	    	ExpenseItemList expenseItemList = new ExpenseItemList();
	    	
	    	// add sample expenseItem
	    	ExpenseItem testExpenseItem = new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency");
	    	expenseItemList.addExpenseItem(testExpenseItem);
	    	Collection<ExpenseItem> expenseItems = expenseItemList.getExpenseItems();
	    	assertTrue("ExpenseItemList size is not equal to 1", expenseItems.size() == 1);
	    	assertTrue("ExpenseItem not contained", expenseItems.contains(testExpenseItem));
	    	
	    	// remove sample expenseItem
	    	expenseItemList.removeExpenseItem(testExpenseItem);
	    	expenseItems = expenseItemList.getExpenseItems();
	    	assertTrue("ExpenseItemList still has one expenseItem", expenseItems.size() == 1);
	    	assertFalse("Original expenseItem still contained", expenseItems.contains(testExpenseItem));
	    	
	    }
}
