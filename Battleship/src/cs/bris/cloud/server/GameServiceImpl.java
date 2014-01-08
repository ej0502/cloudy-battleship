package cs.bris.cloud.server;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import cs.bris.cloud.client.GameService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GameServiceImpl extends RemoteServiceServlet implements GameService {

	public String setupChannel(String username) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return channelService.createChannel(username);
	}

	public Boolean sendChallenge(String challenger, String challengedUser) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		channelService.sendMessage(new ChannelMessage(challengedUser, "challenge-" + challenger));
		return true;
	}
}
