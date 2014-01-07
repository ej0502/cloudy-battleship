package cs.bris.cloud.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentController {
	
	private static ContentController instance;
	private ContentController() {};
	
	public static ContentController getInstance() {
		if (instance == null) instance = new ContentController();
		return instance;
	}

	public void setContent(Composite c) {
		RootPanel.get("content").clear();
		RootPanel.get("content").add(c);
	}

}
