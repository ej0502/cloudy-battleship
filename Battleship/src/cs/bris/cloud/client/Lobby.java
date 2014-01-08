package cs.bris.cloud.client;

import java.util.List;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Lobby extends Composite {
	
	/**
	 * Create a remote service proxy to talk to the server-side Login service.
	 */
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	private final GameServiceAsync gameService = GWT.create(GameService.class);
	
	public Lobby() {
		final VerticalPanel panel = new VerticalPanel();
		
		final Timer timer = new Timer() {
            @Override
            public void run() {
        		setupLobbyContent(panel);
            }
        };
        timer.scheduleRepeating(10000); // ms
        
        setupChannel();
        
        setupLobbyContent(panel);
		initWidget(panel);
	}
	
	private void setupChannel() {
		gameService.setupChannel(UserController.getInstance().getUser(), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				System.out.println("Lobby.java: RPC failed.");
			}
			public void onSuccess(String token) {
		        ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
		        	  @Override
		        	  public void onChannelCreated(Channel channel) {
		        		  channel.open(new SocketListener() {
				        	  @Override
				        	  public void onOpen() {
				        	  }
				        	  @Override
				        	  public void onMessage(String message) {
				        	    System.out.println("Received: " + message);
				        	  }
				        	  @Override
				        	  public void onError(SocketError error) {
				        	    System.out.println("Error: " + error.getDescription());
				        	  }
				        	  @Override
				        	  public void onClose() {
				        	  }
		        		  });
		        	  }
		        });
			}
		});
	}
	
	private void setupLobbyContent(final VerticalPanel panel) {
		panel.clear();
		loginService.getLoggedInUsers(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				System.out.println("Lobby.java: RPC failed.");
			}
			public void onSuccess(List<String> users) {
				for (String user : users) {
					if (!user.equals(UserController.getInstance().getUser())) {
						panel.add(createUserRow(user));
					}
				}
			}
		});
		
		final Label usernameLabel = new Label("Hey " + UserController.getInstance().getUser() + "!");
		panel.add(usernameLabel);
		
		final Button finishButton = new Button("Logout");
		panel.add(finishButton);
		
		finishButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginService.logoutServer(UserController.getInstance().getUser(), new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						System.out.println("Lobby.java: RPC failed.");
					}
					public void onSuccess(Boolean result) {
						UserController.getInstance().setUser(null);
						ContentController.getInstance().setContent(new Start());
					}
				});
			}
		});
	}
	
	private Widget createUserRow(String username) {
		HorizontalPanel row = new HorizontalPanel();
		
		Label userLabel = new Label(username);
		Button challengeButton = new Button("Challenge");
		
		challengeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// send notification to server
			}
		});
		
		row.add(userLabel);
		row.add(challengeButton);
		
		return row;
	}
}