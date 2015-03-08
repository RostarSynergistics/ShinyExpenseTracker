package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import ca.ualberta.cs.shinyexpensetracker.ExpenseItemActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.sax.StartElementListener;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.DateFormat;
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
	 Activity activity;
	 DatePickerDialog datePicker;
	 EditText nameInput, dateInput, amountInput, descriptionInput;
	 Spinner currencyInput, categoryInput;
	 ImageButton photoInput;
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
        activity = (ExpenseItemActivity) getActivity();
        
        datePicker = new DatePickerDialog(instrumentation.getContext(), dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);
        
        nameInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemNameEditText));
        dateInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDateEditText));
        categoryInput = ((Spinner) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCategorySpinner));
        amountInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemAmountEditText));
        currencyInput = ((Spinner) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCurrencySpinner));
        descriptionInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expesenItemDescriptionEditText));
        photoInput = ((ImageButton) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemReciptImageButton));
    }
    
    /**
     * tests that text is set correctly in the view
     */
	public void testSetText() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				 nameInput.setText("name");
				 dateInput.setText("date");
				 categoryInput.setSelection(0);
				 amountInput.setText("10.11");
				 currencyInput.setSelection(0);
				 descriptionInput.setText("description");
				 photoInput.setImageDrawable(image);
				 
				 assertNotNull(activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDoneButton));
				 
				 assertEquals("name", nameInput.getText().toString());
				 assertEquals("date", dateInput.getText().toString());
				 assertEquals("air fare", categoryInput.getSelectedItem().toString());
				 assertEquals("10.11", amountInput.getText().toString());
				 assertEquals("CAD", currencyInput.getSelectedItem().toString());
				 assertEquals("description", descriptionInput.getText().toString());
				 assertEquals(image, photoInput.getDrawable());
				 
				 ((Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDoneButton)).performClick();
			}
		});

	}
	
	/** Tests that when the ExpenseItemDateTextView is clicked a DatePickerDialog is shown */
	public void testSetDateTimeField() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				dateInput.performClick();
				
				assertTrue("datepicker dialog is showing", ((ExpenseItemActivity) activity).getDialog().isShowing());
			}
		});
	}
	
	/** test if an expense Item is successfully created */
	public void testCreateExpenseItem() {
		instrumentation.runOnMainSync(new Runnable(){
			public void run() {
				assertNotNull(activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDoneButton));
				 
				 assertNotNull("name=null", nameInput);
				 assertNotNull("date=null", dateInput);
				 assertNotNull("category=null", categoryInput);
				 assertNotNull("amount=null", amountInput);
				 assertNotNull("currency=null", currencyInput);
				 assertNotNull("description=null", descriptionInput);
				 assertNotNull("photo=null", photoInput);
			}
		});
		 
	}
	
	/** test is the camera app is opened when ExpenseItemImageButton is clicked */
	/*public void testOpenCamera() {
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				photoInput.performClick();
				
				/*Camera testing taken from: 
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
