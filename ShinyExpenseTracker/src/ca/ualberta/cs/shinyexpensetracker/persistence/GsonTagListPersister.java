package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

import com.google.gson.Gson;

/**
 * Handles the persistence of {@link TagList} to a file for offline usage,
 * using Gson to serialize it.
 * 
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class GsonTagListPersister implements ITagListPersister {
	private final IPersistenceStrategy persistenceStrategy;
	private final Gson gson;

	/**
	 * Constructor.
	 * 
	 * @param persistenceStrategy The desired persistence strategy.
	 */
	public GsonTagListPersister(IPersistenceStrategy persistenceStrategy) {
		this.persistenceStrategy = persistenceStrategy;
		this.gson = new Gson();
	}

	@Override
	public TagList loadTags() throws IOException {
		String listData = persistenceStrategy.load();
		if (listData.equals("")) {
			return new TagList();
		} else {
			return gson.fromJson(listData, TagList.class);
		}
	}

	@Override
	public void saveTags(TagList list) throws IOException {
		String travelClaimsString = gson.toJson(list);
		persistenceStrategy.save(travelClaimsString);
	}
}
