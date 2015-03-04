package shared.communication;

public class ValidateUserParams {

	private String username;
	private String password;
	
	
	
	/**
	 * Base Constructor
	 */
	public ValidateUserParams(){
		this.setUsername(null);
		this.setPassword(null);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public ValidateUserParams(String username,String password){
		this.setUsername(username);
		this.setPassword(password);
	}
	
	
	
	
	/*
	 * 
	 * GETTERS AND SETTERS
	 * 
	 */
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
