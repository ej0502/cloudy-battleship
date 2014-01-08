package cs.bris.cloud.server;

import java.util.ArrayList;
import java.util.List;

import cs.bris.cloud.client.LoginService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {
	
	// gets access to the datastore
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public String loginServer(String username, String password) throws IllegalArgumentException {

		// Escape data from the client to avoid cross-site script vulnerabilities.
		username = escapeHtml(username);
		password = escapeHtml(password);
		
		// creates a key for the current username and then attempts to retrieve it
		Key accountKey = KeyFactory.createKey("Accounts", username);
		Query query = new Query("User", accountKey);
		Entity user = datastore.prepare(query).asSingleEntity();
		
		// check whether username exists
		if (user == null) {
			user = new Entity("User", username, accountKey);
			user.setProperty("password", password);
			datastore.put(user);
			addToLobby(username);
			return "registered";
		} else if (user.getProperty("password").equals(password)) {
			addToLobby(username);
			return "logged in";
		} else {
			return "false password";
		}
	}

	public Boolean logoutServer(String username) throws IllegalArgumentException {
		removeFromLobby(username);
		return true;
	}

	public List<String> getLoggedInUsers() throws IllegalArgumentException {
		Key lobbyKey = KeyFactory.createKey("UserList", "Lobby");
		Query query = new Query("User", lobbyKey);
		List<Entity> userEntities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(20));
		List<String> users = new ArrayList<String>();
		for (Entity userEntity : userEntities) {
			users.add(userEntity.getKey().toString());
		}
		return users;
	}
	
	
	private void addToLobby(String username) {
		Key lobbyKey = KeyFactory.createKey("UserList", "Lobby");
		Entity user = new Entity("User", username, lobbyKey);
		datastore.put(user);
	}
	private void removeFromLobby(String username) {
		Key lobbyKey = KeyFactory.createKey("UserList", "Lobby");
		Key userKey = KeyFactory.createKey(lobbyKey, "User", username);
		datastore.delete(userKey);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
