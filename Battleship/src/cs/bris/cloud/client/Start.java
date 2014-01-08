package cs.bris.cloud.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Start extends Composite {

	/**
	 * Create a remote service proxy to talk to the server-side Login service.
	 */
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public Start() {
		VerticalPanel content = new VerticalPanel();

		// create user name input UI
		HorizontalPanel usernamePanel = new HorizontalPanel();
		final Label usernameLabel = new Label("Username:");
		final TextBox usernameTextBox = new TextBox();
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextBox);

		// create password input UI
		HorizontalPanel passwordPanel = new HorizontalPanel();
		final Label passwordLabel = new Label("Password:");
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextBox);

		final Button loginButton = new Button("Login");

		content.add(usernamePanel);
		content.add(passwordPanel);
		content.add(loginButton);
		
		// setup dialog box
		final DialogBox dialogBox = new DialogBox();
		final Button closeButton = new Button("Close");
		dialogBox.add(closeButton);
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		// Add a handler to close the DialogBox
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginService.loginServer(usernameTextBox.getText(), passwordTextBox.getText(), new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						System.out.println("RPC failed.");
					}
					public void onSuccess(String result) {
						if (result.equals("logged in")) {
							ContentController.getInstance().setContent(new Finish());
						} else if (result.equals("registered")) {
							dialogBox.setText("New account created!");
							dialogBox.center();
							ContentController.getInstance().setContent(new Finish());
						} else if (result.equals("false password")) {
							dialogBox.setText("Incorrect account details entered!");
							dialogBox.center();
						}
					}
				});
			}
		});

		initWidget(content);
	}
}
