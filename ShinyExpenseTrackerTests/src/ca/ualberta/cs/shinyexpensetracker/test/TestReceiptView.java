package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ReceiptViewActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Covers Issue 30
 * 
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-16 Test viewing receipt of a referred Expense Item, if there
 *        is any
 */

public class TestReceiptView extends ActivityInstrumentationTestCase2<ReceiptViewActivity> {

	public TestReceiptView() {
		super(ReceiptViewActivity.class);
	}

	ReceiptViewActivity activity;
	ExpenseClaimController controller;
	ExpenseItem item;
	Bitmap imageSmall;
	Resources res;

	protected void setUp() throws Exception {
		/* Set a test item in a claim list and ask the activity to fetch it */
		super.setUp();
		ExpenseClaimList claimList = new ExpenseClaimList();
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim(UUID.randomUUID(), "test claim");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);

		res = getInstrumentation().getTargetContext().getResources();
		imageSmall = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

		item = new ExpenseItem("test item",
				newDate.getTime(),
				Category.fromString("air fare"),
				new BigDecimal("0.125"),
				Currency.CAD,
				"Test Item",
				imageSmall,
				null);

		claim.addExpenseItem(item);
		claimList.addClaim(claim);

		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		intent.putExtra(IntentExtraIDs.EXPENSE_ITEM_ID, item.getID());
		setActivityIntent(intent);
		activity = getActivity();
	}

	// copied from
	// https://stackoverflow.com/questions/26842530/roundedimageview-add-border-and-shadow
	// on March 15, 2015
	/**
	 * Convert a Drawable object to Bitmap
	 */
	private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
		Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutableBitmap);
		drawable.setBounds(0, 0, widthPixels, heightPixels);
		drawable.draw(canvas);
		return mutableBitmap;
	}

	public void testReceiptImage() {
		// this is getting fixed in a pull request that's pending review right
		// now
		// fail();
		ImageView iv = (ImageView) activity.findViewById(R.id.receiptImageView);
		Drawable dr = iv.getDrawable();
		Bitmap receiptFromItem = item.getReceiptPhoto();
		Bitmap receiptFromImage = convertToBitmap(dr, receiptFromItem.getWidth(), receiptFromItem.getHeight());
		assertTrue("different image", receiptFromImage.sameAs(receiptFromItem));
	}
}
