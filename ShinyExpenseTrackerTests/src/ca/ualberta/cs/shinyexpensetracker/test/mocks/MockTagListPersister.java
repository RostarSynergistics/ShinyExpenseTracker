package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.persistence.ITagListPersister;

public class MockTagListPersister 
implements ITagListPersister {
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
