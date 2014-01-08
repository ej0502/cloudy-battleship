package cs.bris.cloud.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
	void loginServer(String username, String password, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
