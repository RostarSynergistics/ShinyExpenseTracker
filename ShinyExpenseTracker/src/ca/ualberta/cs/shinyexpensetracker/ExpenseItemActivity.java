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
 * 
 * Displays activity_create_expense_item activity, to give the user an 
 * interface to add the name, date, category, amount spent, currency, 
 * description and a photo of a receipt for expense items. 
 */

package ca.ualberta.cs.shinyexpensetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;

public class ExpenseItemActivity extends Activity implements OnClickListener{

	// DatePickerDialog from: 
	//  	http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
	// On March 2 2015	  
    private EditText date;
    private DatePickerDialog datePickerDialog;    
    private SimpleDateFormat dateFormatter;
    ImageButton button;
    Bitmap ourBMP;
    private Uri imageFileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private AlertDialog.Builder adb;
    public Dialog alertDialog;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_expense_item);
		
		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
		adb = new AlertDialog.Builder(this);
        
        findViewsById();
        
        setDateTimeField();
        
        button = (ImageButton) findViewById(R.id.expenseItemReceiptImageButton);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void findViewsById() {
    	date = (EditText) findViewById(R.id.expenseItemDateEditText);
        date.setInputType(InputType.TYPE_NULL);
        date.requestFocus();
    }
 
	/**
	 * Uses a DatePickerDialog to allow user to choose a date
	 */
    private void setDateTimeField() {
        date.setOnClickListener(this);
        date.setOnClickListener(this);
        
        Calendar newCalendar = Calendar.getInstance();
        
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }
 
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
     
    }
	
    /** 
     * displays a datePickerDialog when expenseItemDateTextView is clicked.
     */
    @Override
    public void onClick(View view) {
        if(view == date) {
            datePickerDialog.show();
        }
    }

    /** 
     * Gets inputed data from text fields and spinners, saving them in an expenseItem object.
     * @param v
     * @throws ParseException
     */
	public boolean createExpenseItem(View v) throws ParseException {
		
		EditText nameText = (EditText) findViewById(R.id.expenseItemNameEditText);
		EditText dateText = (EditText) findViewById(R.id.expenseItemDateEditText);
		Spinner categorySpinner = (Spinner) findViewById(R.id.expenseItemCategorySpinner);
		EditText amountText = (EditText) findViewById(R.id.expenseItemAmountEditText);
		Spinner currencySpinner = (Spinner) findViewById(R.id.expenseItemCurrencySpinner);
		EditText descriptionText = (EditText) findViewById(R.id.expesenItemDescriptionEditText);
		
		String name = "";
		if (nameText.getText().length() == 0){
			adb.setMessage("Expense Item requires a name");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialog = adb.create();
			alertDialog.show();
			return false;
		} else {
			name = nameText.getText().toString();
		}
		
		Date date = new Date();
		if (dateText.getText().length() == 0) {
			adb.setMessage("Expense Item requires a date");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialog = adb.create();
			alertDialog.show();
			return false;
		} else {
			date = dateFormatter.parse(dateText.getText().toString());
		}
		
		Category category = Category.fromString(categorySpinner.getSelectedItem().toString());
		
		// get the amount Spent from the editText, checking if not entered
		BigDecimal amount = new BigDecimal("0.00");
		if (amountText.getText().length() == 0) {
			adb.setMessage("Expense Item requires an amount spent");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialog = adb.create();
			alertDialog.show();
			return false;
		} else {
			amount = new BigDecimal(amountText.getText().toString());
		}
		
		Currency currency = Currency.valueOf(currencySpinner.getSelectedItem().toString());
		
		String description;
		if (descriptionText.getText().length() == 0){
			description = "";
		} else {
			description = descriptionText.getText().toString();
		}
		
		ExpenseItem expense = new ExpenseItem(name, date, category, amount, 
				currency, description, button.getDrawingCache()); 
		
		//Still needs to be implemented
		//Add expenseItem to claim
		
		return true;
	}
	
	/** 
	 * Runs on click of the expenseItemImageButton.  Opens Camera app to allow user to take a 
	 * picture of a receipt.  Saves picture in a temporary file.
	 * @param V
	 */
	public void takePicture(View V) {
		// Create a folder to store pictures
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}
			
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Create an URI for the picture file
		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
				
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	/**
	 * Runs on return from the camera app.  Displays the image taken in the 
	 * expenseItemImageButton
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Result: OK!", Toast.LENGTH_SHORT).show();
				assert imageFileUri != null;
				assert button != null;
				button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Result: Cancelled", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Result: ???", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/** On click of the expenseItemDoneButton, the activity will close to 
	 * return to the previous activity (ExpenseItems Summary page)
	 * @param v
	 * @throws ParseException 
	 */
	public void doneExpenseItem(View v) throws ParseException{
		
		if (createExpenseItem(v)){
			finish();
		}
	}
	
	/** 
	 * Method to allow for testing of the DatePickerDialog
	 * @return
	 */
	public DatePickerDialog getDialog(){
		return datePickerDialog;
	}
	
	public AlertDialog.Builder getAlertDialog() {
		return adb;
	}
}
