package cs.bris.cloud.client;

import java.util.List;
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
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);
	
	public Lobby() {
		final VerticalPanel panel = new VerticalPanel();
		
		final Timer timer = new Timer() {
            @Override
            public void run() {
        		setupLobbyContent(panel);
            }
        };
        timer.scheduleRepeating(10000); // ms
        
        setupLobbyContent(panel);
		initWidget(panel);
	}
	
	private void setupLobbyContent(final VerticalPanel panel) {
		panel.clear();
		loginService.getLoggedInUsers(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				System.out.println("Finish.java: RPC failed.");
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
						System.out.println("Finish.java: RPC failed.");
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
				// deal with sending a challenge via dialog box to another player and then the response
			}
		});
		
		row.add(userLabel);
		row.add(challengeButton);
		
		return row;
	}
}