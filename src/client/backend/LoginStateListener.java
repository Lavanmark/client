package client.backend;

import shared.model.User;

public interface LoginStateListener {
	public void userLoggedIn(User user);
	public void userLoggedOut(User user);
}
