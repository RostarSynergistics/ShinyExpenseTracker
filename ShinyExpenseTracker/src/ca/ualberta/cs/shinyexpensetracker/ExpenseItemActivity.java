/**
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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_expense_item);
		
		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
        
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
	public void createExpenseItem(View v) throws ParseException {
		EditText nameText = (EditText) findViewById(R.id.expenseItemNameEditText);
		EditText dateText = (EditText) findViewById(R.id.expenseItemDateEditText);
		Spinner categorySpinner = (Spinner) findViewById(R.id.expenseItemCategorySpinner);
		EditText amountText = (EditText) findViewById(R.id.expenseItemAmountEditText);
		Spinner currencySpinner = (Spinner) findViewById(R.id.expenseItemCurrencySpinner);
		EditText descriptionText = (EditText) findViewById(R.id.expesenItemDescriptionEditText);
		
		String name;
		if (nameText.getText().length() == 0){
			name = "name";
		} else {
			name = nameText.getText().toString();
		}
		
		Date date;
		if (dateText.getText().length() == 0) {
			date = new Date();
		} else {
			date = dateFormatter.parse(dateText.getText().toString());
		}
		
		Category category = Category.fromString(categorySpinner.getSelectedItem().toString());
		
		// get the amount Spent from the editText, checking if not entered
		BigDecimal amount;
		if (amountText.getText().length() == 0) {
			amount = new BigDecimal("0.00");
		} else {
			amount = new BigDecimal(amountText.getText().toString());
		}
		
		Currency currency = Currency.valueOf(currencySpinner.getSelectedItem().toString());
		
		String description;
		if (descriptionText.getText().length() == 0){
			description = "description";
		} else {
			description = descriptionText.getText().toString();
		}
		
		ExpenseItem expense = new ExpenseItem(name, date, category, amount, 
				currency, description, button.getDrawingCache()); 
		
		//Still needs to be implemented
		//Add expenseItem to claim
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
				//tv.setText("Result: OK!");
				assert imageFileUri != null;
				assert button != null;
				button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Result: Cancelled", Toast.LENGTH_SHORT).show();
				//tv.setText("Result: Cancelled");
			} else {
				Toast.makeText(this, "Result: ???", Toast.LENGTH_SHORT).show();
				//tv.setText("Result: ????");
			}
		}
	}
	
	/** On click of the expenseItemDoneButton, the activity will close to 
	 * return to the previous activity (ExpenseItems Summary page)
	 * @param v
	 * @throws ParseException 
	 */
	public void doneExpenseItem(View v) throws ParseException{
		createExpenseItem(v);
		finish();
	}
	
	/** 
	 * Method to allow for testing of the DatePickerDialog
	 * @return
	 */
	public DatePickerDialog getDialog(){
		return datePickerDialog;
	}
}
