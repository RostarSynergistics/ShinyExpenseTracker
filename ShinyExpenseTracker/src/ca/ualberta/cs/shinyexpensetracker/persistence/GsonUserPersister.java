package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.User;

import com.google.gson.Gson;

/**
 * Handles the persistence of {@link User} to a file for offline usage, using
 * Gson to serialize it.
 * 
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class GsonUserPersister implements IUserPersister {

	private final IPersistenceStrategy persistenceStrategy;
	private final Gson gson;

	/**
	 * Constructor.
	 * 
	 * @param persistenceStrategy
	 *            The desired persistence strategy.
	 */
	public GsonUserPersister(IPersistenceStrategy persistenceStrategy) {
		this.persistenceStrategy = persistenceStrategy;
		this.gson = new Gson();
	}

	@Override
	public User loadUser() throws IOException {
		String userData = persistenceStrategy.load();
		if (userData.equals("")) {
			return new User();
		} else {
			return gson.fromJson(userData, User.class);
		}
	}

	@Override
	public void saveUser(User user) throws IOException {
		String userString = gson.toJson(user);
		persistenceStrategy.save(userString);
	}
}
