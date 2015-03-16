package ca.ualberta.cs.shinyexpensetracker.test;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.activities.ManageTagActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ManageTagsTest extends ActivityInstrumentationTestCase2<ManageTagActivity> {

	Activity activity;
	Instrumentation instrumentation;
	TagController tagController;
	ListView manageTagsListView;
	
	public ManageTagsTest() {
		super(ManageTagActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tagController = TagController.getInstance();
		tagController.setTagList(new TagList());
		activity = getActivity();
		instrumentation = getInstrumentation();
		manageTagsListView = (ListView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.listViewManageTags);
	}

	public void testViewTag() {
		assertNotNull(tagController.getTagList());
		assertTrue(tagController.getTagList().size() == 0);
		assertTrue(manageTagsListView.getCount() == 0);

		Tag tag = new Tag("Test");
		addTagToTagList(tag);
		
		assertEquals(tag, (Tag)manageTagsListView.getItemAtPosition(0));
		assertTrue(tagController.getTagList().size() == 1);
		assertTrue(manageTagsListView.getCount() == 1);	
	}
	
	

	private Tag addTagToTagList(final Tag tag) {
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				tagController.getTagList().addTag(tag);
			}
		});
		instrumentation.waitForIdleSync();
		return tag;
	}
	
	
	
	

}
