package cs.bris.cloud.server;

import cs.bris.cloud.client.LoginService;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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

	public String loginServer(String username, String password) throws IllegalArgumentException {

		// Escape data from the client to avoid cross-site script vulnerabilities.
		username = escapeHtml(username);
		password = escapeHtml(password);

		// gets access to the datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// creates a key for the current username and then attempts to retrieve it
		Key usernameKey = KeyFactory.createKey("Username", username);
		Query query = new Query("Username", usernameKey);
		Entity user = datastore.prepare(query).asSingleEntity();
		
		// check whether username exists
		if (user == null) {
			user = new Entity("Username", username);
			user.setProperty("password", password);
			datastore.put(user);
			return "registered";
		} else if (user.getProperty("password").equals(password)) {
			return "logged in";
		} else {
			return "false password";
		}
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
