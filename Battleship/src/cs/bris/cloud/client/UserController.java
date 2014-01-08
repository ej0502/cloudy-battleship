package cs.bris.cloud.client;

public class UserController {
	
	private String username;
	private static UserController instance;
	private UserController() {};
	
	public static UserController getInstance() {
		if (instance == null) instance = new UserController();
		return instance;
	}
	
	public void setUser(String username) {
		this.username = username;
	}
	public String getUser() {
		return this.username;
	}

}
