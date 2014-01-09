package cs.bris.cloud.client;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public class Game extends Composite {

	private final GameServiceAsync gameService = GWT.create(GameService.class);
	private ClientGame game;
	public Game(String opponent) {
		// construct UI here
		game = new ClientGame(opponent);
		initWidget(game);
		
		// call setupChannel
		setupChannel();
	}
	
	private void setupChannel() {
		gameService.setupGameChannel(UserController.getInstance().getUser(), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				System.out.println("Game.java: RPC failed.");
			}
			public void onSuccess(String token) {
		        ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
		        	  @Override
		        	  public void onChannelCreated(Channel channel) {
		        		  channel.open(new SocketListener() {
		        			  @Override
		        			  public void onOpen() {}
		        			  @Override
		        			  public void onMessage(String message) {
		        				  String opponent;
		        				  if (message.contains("newturn")) {
		        					  opponent = message.substring(0, message.indexOf("newturn"));
		        					  int indexOfTarget = message.indexOf('-') + 1;
		        					  int x = Integer.parseInt(message.substring(indexOfTarget, indexOfTarget + 1));
		        					  int y = Integer.parseInt(message.substring(indexOfTarget + 1, indexOfTarget + 2));
		        					  int[] target = {x, y};
		        					  if (message.contains("hit")) game.updateUI(true, false, target);
		        					  else game.updateUI(false, false, target);
		        					  game.getTarget();
		        				  } else if (message.contains("turn")) {
		        					  opponent = message.substring(0, message.indexOf("turn"));
		        					  game.getTarget();
		        				  } else if (message.contains("finish")) {
		        					  opponent = message.substring(0, message.indexOf("finish"));
		        					  if (message.contains("win")) {
		        						  Window.alert("You won against " + opponent + "!");
		        					  } else {
		        						  Window.alert("You lost against " + opponent + "!");
		        					  }
	        						  ContentController.getInstance().setContent(new Lobby());
		        				  }
		        			  }
		        			  @Override
		        			  public void onError(SocketError error) {
		        				  System.out.println("Error: " + error.getDescription());
		        			  }
		        			  @Override
		        			  public void onClose() {
		        				  System.out.println(UserController.getInstance().getUser() + ": channel closed.");
		        			  }
		        		  });
		        	  }
		        });
			}
		});
	}
}
