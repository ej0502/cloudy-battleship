package cs.bris.cloud.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
	void loginServer(String username, String password, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void logoutServer(String username, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getLoggedInUsers(AsyncCallback<List<String>> callback) throws IllegalArgumentException;
}
