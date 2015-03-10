import android.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.app.Activity;
import android.app.Instrumentation;
import ca.ualberta.cs.activities.ManageTagActivity;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;


public class ManageTagsTest extends ActivityInstrumentationTestCase2 {

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
		activity = getActivity();
		instrumentation = getInstrumentation();
		manageTagsListView = (ListView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.listViewManageTags);
	}

	public void testViewTag() {
		assertNotNull(tagController.getTagList());
		assertTrue(tagController.getTagList().size() == 0);
		assertTrue(manageTagsListView.getCount() == 0);

		Tag tag = new Tag("Test");
		addTag(tag);
		
		assertEquals(tag, (Tag)manageTagsListView.getItemAtPosition(0));
		assertTrue(tagController.getTagList().size() == 1);
		assertTrue(manageTagsListView.getCount() == 1);	
	}

	private Tag addTag(final Tag tag) {
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
