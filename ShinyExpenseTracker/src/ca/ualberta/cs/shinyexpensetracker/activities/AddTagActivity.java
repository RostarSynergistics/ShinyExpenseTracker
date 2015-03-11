/* 
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
 */
package ca.ualberta.cs.shinyexpensetracker.activities;

import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that reacts to creation of a new tag
 * Covers Issue #25
 * @author Oleg Oleynikov
 * @author Rajan Jassal
 * @version 1.0
 * @since 2015-03-10
 */
public class AddTagActivity extends Activity {
	
	TagController tagController;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);
		tagController = TagController.getInstance();
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
		int prevSize = tagController.getTagCount();
		tagController.addTag(tagName);
		int currSize = tagController.getTagCount();
		if (currSize == prevSize){
			Toast.makeText(this, "Tag name has to be an alphanumeric value.\nPlease enter a new name and press Add Tag again", Toast.LENGTH_LONG).show();
		}
		else{
			// Go back to the Manage Tags screen
			// Not sure how to do that without creating a new activity, though
			//Intent intent = new Intent(AddTagActivity.this, ManageTagActivity.class);
	    	//startActivity(intent);
		}
	}
}
