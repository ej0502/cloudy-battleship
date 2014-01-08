package cs.bris.cloud.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GameService</code>.
 */
public interface GameServiceAsync {
	void setupChannel(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
}
