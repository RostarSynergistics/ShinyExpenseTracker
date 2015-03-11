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
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * Activity that handles viewing all the tags and also allows movement to add, edit and delete tags 
 * Covers Issue #24
 * @author Rajan Jassal
 * @version 1.0
 * @since 2015-03-10
 */
public class ManageTagActivity extends Activity implements IView<TagList> {
	
	private ListView manageTags;
	private ArrayAdapter<Tag> tagListAdapter;
	private TagController tagController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);
		manageTags = (ListView)findViewById(R.id.listViewManageTags);
		tagController = TagController.getInstance();
		//Setting a listener for tag controller
		tagController.getTagList().addView(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_tag, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, tagController.getTagList().getTags());
		manageTags.setAdapter(tagListAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionManageTagsAdd:
			//Go to add screen from here
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
		
	}

	@Override
	public void update(TagList m) {
		tagListAdapter.notifyDataSetChanged();
	}

}
