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
 * Tests ExpenseItemActivity 
 */

package ca.ualberta.cs.shinyexpensetracker.test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.shinyexpensetracker.AddTagActivity;
import ca.ualberta.cs.shinyexpensetracker.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.sax.StartElementListener;
import android.support.v4.graphics.BitmapCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ExpenseItemActivityTest extends
		ActivityInstrumentationTestCase2<ExpenseItemActivity> {

    private static final int TARGET_YEAR = 2008;
    private static final int TARGET_MONTH = 11;
    private static final int TARGET_DAY = 7;
    
    int year, month, day;
    
	 Instrumentation instrumentation;
	 ExpenseItemActivity activity;
	 DatePickerDialog datePicker;
	 EditText nameInput, dateInput, amountInput, descriptionInput;
	 Spinner currencyInput, categoryInput;
	 ImageButton photoInput;
	 Button doneButton;
	 Drawable image = new Drawable() {
		@Override
		public void setColorFilter(ColorFilter arg0) {		}
		@Override
		public void setAlpha(int arg0) {		}
		@Override
		public int getOpacity() {
			return 0;
		}
		public void draw(Canvas arg0) {		}
	};
	
	public ExpenseItemActivityTest(){
		super(ExpenseItemActivity.class);
	}
	 
	public ExpenseItemActivityTest(Class<ExpenseItemActivity> activityClass) {
		super(activityClass);
	}
	
    private OnDateSetListener dateListener = new OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	year = year;
            month = monthOfYear;
            day = dayOfMonth;
        }
    };

    protected void setUp() throws Exception {
    	super.setUp();
        instrumentation = getInstrumentation();
        activity = getActivity();
        
        datePicker = new DatePickerDialog(instrumentation.getContext(), dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);
        
        nameInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemNameEditText));
        dateInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDateEditText));
        categoryInput = ((Spinner) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCategorySpinner));
        amountInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemAmountEditText));
        currencyInput = ((Spinner) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCurrencySpinner));
        descriptionInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expesenItemDescriptionEditText));
        photoInput = ((ImageButton) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemReceiptImageButton));
        doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDoneButton);
    }
    
    /** Tests that when the ExpenseItemDateTextView is clicked a DatePickerDialog is shown */
	public void testSetDateTimeField() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				dateInput.performClick();
			}
		});
		assertTrue("datepicker dialog is showing", ((ExpenseItemActivity) activity).getDialog().isShowing());
	}
	
	/** test if an expense Item is successfully created */
	public void testCreateExpenseItem() {
		instrumentation.runOnMainSync(new Runnable(){
			public void run() {
				assertNotNull(doneButton);

				 SimpleDateFormat sdf = new SimpleDateFormat();
				 Date date = new Date();
				 sdf.format(date);
				 BigDecimal amount = new BigDecimal(10.00);
				 Bitmap bitmap = null;
				 
				 ExpenseItem expense = new ExpenseItem("name", date, 
						 Category.ACCOMODATION,  amount, Currency.CAD, 
						 "description", bitmap);
				 
				 assertEquals("name != name", "name", expense.getName());
				 assertNotSame("false positive, name", "Wrong Name", expense.getName());
				 assertEquals("date != date", date, expense.getDate());
				 assertNotSame("false positive date", "wrong date", expense.getDate());
				 assertEquals("category != accomodation", Category.ACCOMODATION, expense.getCategory());
				 assertNotSame("false positive, category", "wrong category", expense.getCategory());
				 assertEquals("amount != 10.00", amount, expense.getAmountSpent());
				 assertNotSame("false positive, amount", new BigDecimal(5.00), expense.getAmountSpent()); 
				 assertEquals("currnency != CAD", Currency.CAD, expense.getCurrency());
				 assertNotSame("false positive, currency", "wrong currency", expense.getCurrency());
				 assertEquals("description != description", "description", expense.getDescription());
				 assertNotSame("false positibe description", "wrong description", expense.getDescription());
				 assertEquals("bitmap != bitmap", bitmap, expense.getReceiptPhoto());
				 assertNotSame("false posibive, photo", "not bitmap", expense.getReceiptPhoto());
			}
		});
	}
	
	/** tests if the data entered has been correctly saved to an expenseItem when the Done button is clicked */
	public void testDone() {
		
		//Test not done being implemented, needs to check to see if loaded expenseItem is what was entered
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {				
				nameInput.setText("name");
				doneButton.performClick();
				activity = getActivity();
				nameInput = (EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemNameEditText);
				
			}
		});
		instrumentation.waitForIdleSync();

		assertTrue(activity != null);
		assertEquals("length != 0", 0, nameInput.getText().length());
		assertEquals("nameInput == expenseItem.name", "name", nameInput.getText().toString());
		fail();
		
		
	}

	/** tests that a dialog telling the user that they require a name before completing the expense item appears */
	public void testNameDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no name dialog", activity.alertDialog);
		assertTrue("Name dialog is not showing", activity.alertDialog.isShowing());
	}

	/** tests that a dialog telling the user that they require a date before completing the expense item appears */
	public void testDateDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no date dialog", activity.alertDialog);
		assertTrue("Date dialog is not showing", activity.alertDialog.isShowing());
	}
	
	/** 
	 * tests that a dialog telling the user that they require an amount spent before completing 
	 * the expense item appears 
	*/
	public void testAmountDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no amount spent dialog", activity.alertDialog);
		assertTrue("Dialog amount spent is not showing", activity.alertDialog.isShowing());
	}


	
	/** test is the camera app is opened when ExpenseItemImageButton is clicked */
	/*public void testOpenCamera() {
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				photoInput.performClick();
				
				/*
				* Camera testing taken from: 
				*	http://developer.android.com/guide/topics/media/camera.html
				* on March 7 2015
				 
				//try to create an instance of a camera
			    Camera camera = null;
			    try {
			    	//if this succeeds, camera was not open
			        camera = Camera.open();
			        fail("camera is not open");
			    } catch (RuntimeException e) {
			    	// runtimeException thrown means that camera was already open
			    } finally {
			        if (camera != null) camera.release();
			    }
			    
			}
		});
	}
	*/

}
