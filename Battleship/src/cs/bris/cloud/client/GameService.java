package cs.bris.cloud.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("game")
public interface GameService extends RemoteService {
	String setupChannel(String username);
	Boolean sendChallenge(String challenger, String challengedUser);
}
