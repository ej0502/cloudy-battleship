package cs.bris.cloud.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Start extends Composite {
	public Start() {
		VerticalPanel panel = new VerticalPanel();
		final Button startButton = new Button("Start");
		panel.add(startButton);
		
		// Add a handler to close the DialogBox
		startButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				System.out.println("Start button clicked, huzzah!");
				ContentController.getInstance().setContent(new Finish());
			}
		});
		
		initWidget(panel);
	}
}
