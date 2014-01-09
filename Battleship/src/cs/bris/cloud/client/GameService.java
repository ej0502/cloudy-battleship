package cs.bris.cloud.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("game")
public interface GameService extends RemoteService {
	String setupLobbyChannel(String username);
	Boolean sendChallenge(String challenger, String challengedUser);
	Boolean challengeReply(String challenger, Boolean reply);
	String setupGameChannel(String username);
	Boolean sendPositions(String username, String opponent);
	Boolean checkHit(String username, String opponent);
}
