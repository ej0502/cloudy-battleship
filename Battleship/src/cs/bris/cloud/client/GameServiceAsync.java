package cs.bris.cloud.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GameService</code>.
 */
public interface GameServiceAsync {
	void setupLobbyChannel(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sendChallenge(String challenger, String challengedUser, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void challengeReply(String challenger, Boolean reply, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void setupGameChannel(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sendPositions(String username, String opponent, int[][] ships, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void checkHit(String username, String opponent, int[] target, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
}
