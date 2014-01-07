package cs.bris.cloud.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Finish extends Composite {
	public Finish() {
		VerticalPanel panel = new VerticalPanel();
		final Button finishButton = new Button("Finish");
		panel.add(finishButton);
		
		// Add a handler to close the DialogBox
		finishButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				System.out.println("Finish button clicked, huzzah!");
				ContentController.getInstance().setContent(new Start());
			}
		});
		
		initWidget(panel);
	}
}