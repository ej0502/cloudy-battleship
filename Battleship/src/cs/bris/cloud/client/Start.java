package cs.bris.cloud.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Start extends Composite {
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
		
		// Add a handler to close the DialogBox
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				System.out.println("Login button clicked, huzzah!");
				//System.out.println("Username: " + usernameTextBox.getText() + " Password: " + passwordTextBox.getText());
				ContentController.getInstance().setContent(new Finish());
			}
		});
		
		initWidget(content);
	}
}
