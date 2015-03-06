package ca.ualberta.cs.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ManageTagActivity extends Activity {
	private ListView manageTags;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);
		manageTags = (ListView)findViewById(R.id.listViewManageTags);
		
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
		ArrayAdapter<Tag> tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, TagController.getTagList().getTags());
		manageTags.setAdapter(tagListAdapter);
	}
	
	

}
