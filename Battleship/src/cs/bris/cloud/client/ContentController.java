package cs.bris.cloud.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentController {

	public void setContent() {
		RootPanel.get("content").clear();
		RootPanel.get("content").add(new Button("Finish"));
	}

}
