package cs.bris.cloud.server;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import cs.bris.cloud.client.GameService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GameServiceImpl extends RemoteServiceServlet implements GameService {
	
	private ChannelService channelService;

	public String setupLobbyChannel(String username) {
		channelService = ChannelServiceFactory.getChannelService();
		return channelService.createChannel(username + "lobby");
	}

	public Boolean sendChallenge(String challenger, String challengedUser) {
		channelService.sendMessage(new ChannelMessage(challengedUser + "lobby", challenger + "challenge"));
		return true;
	}
	
	public Boolean challengeReply(String challenger, Boolean reply) {
		if (reply) {
			channelService.sendMessage(new ChannelMessage(challenger + "lobby", challenger + "accepted"));
		}
		else {
			channelService.sendMessage(new ChannelMessage(challenger + "lobby", "rejected"));
		}
		return true;
	}
	
	public String setupGameChannel(String username) {
		return channelService.createChannel(username + "game");
	}

	public Boolean sendPositions(String username, String opponent) {
		// add positions to datastore
		
		// check if other client has replied yet
		
		// send communication to other client if it has else return
		// channelService.sendMessage(new ChannelMessage(opponent + "game", opponent + "turn"));
		
		return true;
	}
	
	public Boolean checkHit(String username, String opponent) {
		// check if hit or miss
		Boolean hit = true;
		
		// if hit, check if victory
		// if (hit) // victory check
		// if (victory) {
		// channelService.sendMessage(new ChannelMessage(username + "game", opponent + "finishwin"));
		// channelService.sendMessage(new ChannelMessage(opponent + "game", username + "finishloss"));
		// return hit;
		// }
		
		// send communication to opponent saying its turn and hit/miss
		// if (hit) channelService.sendMessage(new ChannelMessage(opponent + "game", opponent + "newturnhit"));
		// else channelService.sendMessage(new ChannelMessage(opponent + "game", opponent + "newturnmiss"));
		
		// return true if hit, false if miss
		return hit;
	}
}
