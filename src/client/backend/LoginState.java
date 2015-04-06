package client.backend;

import java.util.ArrayList;
import java.util.List;

import shared.model.User;


public class LoginState {

	private static boolean loggedIn;
	private static User userOn;
	private static List<LoginStateListener> listeners;
	
	
	public static void initialize(){
		loggedIn = false;
		userOn = null;
		listeners = new ArrayList<LoginStateListener>();
	}
	
	public static boolean loggedIn(){
		return loggedIn;
	}
	
	public static User getUserOn(){
		return userOn;
	}
	
	public static void logUserIn(User user){
		userOn = user;
		loggedIn = true;
		for(LoginStateListener l : listeners){
			l.userLoggedIn(userOn);
		}
	}
	
	public static void logUserOut(){
		for(LoginStateListener l : listeners){
			l.userLoggedOut(userOn);
		}
		loggedIn = false;
		userOn = null;
	}
	
	public static void updateUser(User user){
		userOn = user;
	}
	
	public static void addListener(LoginStateListener l) {
		listeners.add(l);
	}
}
