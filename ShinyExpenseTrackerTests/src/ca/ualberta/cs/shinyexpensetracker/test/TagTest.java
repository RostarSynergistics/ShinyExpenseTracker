/* 
 * Test suite for adding and removing a tag
 * 
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
 */

package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockTagListPersister;

/**
 * 
 * 
 * Covers Issue 25 Things to implement: proper navigation back to Manage Tags
 * Activity
 * 
 * @version 1.0
 * @since 2015-03-08
 */

public class TagTest extends InstrumentationTestCase {
	TagController controller;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		controller = new TagController(new MockTagListPersister());
		Application.setTagController(controller);
	}

	public void testAddAndRemoveTag() {
		String[] tags = { "q1wert", "1qwert", "qwert", "12345" };
		Instrumentation instrumentation = getInstrumentation();

		assertEquals("Tag controller not empty: ", 0,
				controller.getTagCount());

		for (final String t : tags) {
			Tag tag = new Tag(t);

			instrumentation.runOnMainSync(new Runnable() {
				@Override
				public void run() {
					try {
						controller.addTag(new Tag(t));
					} catch (IOException e) {
						fail();
					}
				}
			});
			instrumentation.waitForIdleSync();
			assertEquals("failed to add a tag: " + t, 1,
					controller.getTagCount());
			assertEquals("added a tag incorrectly", tag, controller
					.getTagList().getTagById(0));

			instrumentation.runOnMainSync(new Runnable() {
				@Override
				public void run() {
					try {
						controller.removeTag(t);
					} catch (IOException e) {
						fail();
					}
				}
			});
			instrumentation.waitForIdleSync();

			assertEquals("failed to remove a tag: " + t,
					controller.getTagCount(), 0);
		}
	}

	public void testAddInvalidTags() {
		String[] invalidTags = { ("!@#$%"), ("qwe rty"), ("qwe!@#$%rty"),
				("!@qwerty#$%"), (" qwerty "), (null), (""), (" "), ("\n") };
		Instrumentation instrumentation = getInstrumentation();
		for (final String s : invalidTags) {
			instrumentation.runOnMainSync(new Runnable() {

				@Override
				public void run() {
					try {
						controller.addTag(new Tag(s));
					} catch (IOException e) {
						fail();
					}
				}
			});
			instrumentation.waitForIdleSync();

			// The tag list will still be empty because nothing should be added
			assertEquals("should have discarded non-alphanumeric tag: " + s,
					controller.getTagCount(), 0);
		}
	}
}
