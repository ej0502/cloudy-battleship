package cs.bris.cloud.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Battleship implements EntryPoint {
	
	private ContentController contentController = new ContentController();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button startButton = new Button("Start");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("content").add(startButton);

		// Add a handler to close the DialogBox
		startButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				System.out.println("Start button clicked, huzzah!");
				contentController.setContent();
			}
		});
	}
}