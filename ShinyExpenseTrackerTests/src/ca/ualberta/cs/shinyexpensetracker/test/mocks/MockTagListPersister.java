package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.persistence.ITagListPersister;

/**
 * Implementation of ITagListPersister that just saves and loads it from
 * an instance variable.
 */
public class MockTagListPersister implements ITagListPersister {
	private TagList list;
	
	public MockTagListPersister() {
		this(new TagList());
	}
	
	public MockTagListPersister(TagList list) {
		this.list = list;
	}

	@Override
	public TagList loadTags() throws IOException {
		return list;
	}

	@Override
	public void saveTags(TagList list) throws IOException {
		this.list = list;
	}
}
