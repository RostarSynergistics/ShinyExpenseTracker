package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.R.id;
import ca.ualberta.cs.shinyexpensetracker.R.layout;
import ca.ualberta.cs.shinyexpensetracker.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that reacts to creation of a new tag
 * Covers Issue 25
 * Things to implement: proper navigation back to Manage Tags Activity
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class AddTagActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_tag, menu);
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
	
	public void addTag(View v)
	{
		EditText tagNameTextBox = (EditText) findViewById(R.id.tagName);
		String tagName = tagNameTextBox.getText().toString();
		int prevSize = TagController.getTagCount();
		TagController.addTag(tagName);
		int currSize = TagController.getTagCount();
		if (currSize == prevSize){
			Toast.makeText(this, "Tag name has to be an alphanumeric value.\nPlease enter a new name and press Add Tag again", Toast.LENGTH_LONG).show();
		}
		else{
			// Go back to the Manage Tags screen
			// Not sure how to do that without creating a new activity, though
			Intent intent = new Intent(AddTagActivity.this, ManageTagActivity.class);
	    	startActivity(intent);
		}
	}
}
