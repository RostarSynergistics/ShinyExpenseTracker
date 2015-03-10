package ca.ualberta.cs.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.IModel;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ManageTagActivity extends Activity implements IView<TagList> {
	private ListView manageTags;
	ArrayAdapter<Tag> tagListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);
		manageTags = (ListView)findViewById(R.id.listViewManageTags);
		TagController.getInstance().getTagList().addView(this);
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
		tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, TagController.getInstance().getTagList().getTags());
		manageTags.setAdapter(tagListAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.actionManageTagsAdd:
			//TODO: But intent to get to the add screen 
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
		
	}

	@Override
	public void update(TagList m) {
		// TODO Auto-generated method stub
		tagListAdapter.notifyDataSetChanged();
	}

	
	
}
