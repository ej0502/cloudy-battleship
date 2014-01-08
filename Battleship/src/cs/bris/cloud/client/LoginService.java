package cs.bris.cloud.client;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	String loginServer(String username, String password) throws IllegalArgumentException;
	Boolean logoutServer(String username) throws IllegalArgumentException;
	List<String> getLoggedInUsers() throws IllegalArgumentException;
}
