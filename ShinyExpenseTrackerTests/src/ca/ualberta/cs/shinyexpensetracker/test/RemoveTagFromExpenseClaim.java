package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.activities.RemoveTagFromClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockTagListPersister;

public class RemoveTagFromExpenseClaim extends ActivityInstrumentationTestCase2<RemoveTagFromClaimActivity> {

	private RemoveTagFromClaimActivity activity;
	private Instrumentation instrumentation;
	private TagController tagController;
	private ExpenseClaimController expenseClaimController;
	private ListView manageTagsListView;
	private Button done;

	public RemoveTagFromExpenseClaim() {
		super(RemoveTagFromClaimActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		ExpenseClaim claim = new ExpenseClaim("TEST");
		claimList.addClaim(claim);

		// Setting up the controllers
		expenseClaimController = new ExpenseClaimController(new MockExpenseClaimListPersister(claimList));
		tagController = new TagController(new MockTagListPersister());
		Application.setExpenseClaimController(expenseClaimController);
		Application.setTagController(tagController);

		// Filling the controller
		claim.addTag(new Tag("TEST"));
		claim.addTag(new Tag("TEST1"));

		// Setting the initial intent input
		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);

		// Getting the activity
		activity = (RemoveTagFromClaimActivity) getActivity();
		instrumentation = getInstrumentation();
		manageTagsListView = (ListView) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.listViewManageTags);
		done = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.doneButtonManageTags);

	}

	/**
	 * Tests if the add dialogue is shown when the add button is clicked
	 */
	public void testAddDialogShows() {
		// Press the add button in the menu bar. Should create a dialog
		clickRemoveTagsButton();
		AlertDialog dialog = RemoveTagFromClaimActivity.getDialog();
		assertTrue(dialog.isShowing());
	}

	/**
	 * Testing removing one tag at once
	 */
	public void testRemoveTagToClaim() {
		assertEquals(expenseClaimController.getExpenseClaim(0).getTagList().size(), 2);
		clickTagList(0);
		removeSelectedTagToClaim();
		assertEquals(expenseClaimController.getExpenseClaim(0).getTagList().size(), 1);
	}

	/**
	 * Testing removing multiple tags to a single claim at once
	 */
	public void testRemoveMultipleTagsToClaim() {
		assertEquals(expenseClaimController.getExpenseClaim(0).getTagList().size(), 2);
		clickTagList(0);
		clickTagList(1);
		removeSelectedTagToClaim();
		assertEquals(expenseClaimController.getExpenseClaim(0).getTagList().size(), 0);
	}

	// This method clicks on a tag in the list view to check or uncheck it
	private void clickTagList(final int pos) {
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				manageTagsListView.performItemClick(manageTagsListView.getAdapter().getView(0, null, null),
						pos,
						manageTagsListView.getAdapter().getItemId(pos));
			}
		});

		instrumentation.waitForIdleSync();

	}

	// This simulates pressing the done button to remove all checked tags
	private void removeSelectedTagToClaim() {
		clickRemoveTagsButton();
		AlertDialog dialog = RemoveTagFromClaimActivity.getDialog();
		final Button dialogPostiveButton = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);

		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				dialogPostiveButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();
	}

	// Clicks the Removetags button
	private void clickRemoveTagsButton() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				done.performClick();
			}
		});
	}

}
