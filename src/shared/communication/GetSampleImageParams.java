package shared.communication;

public class GetSampleImageParams {

	
	private String username;
	private String password;
	private int projectID;
	
	
	
	
	/**
	 * Initializes variables
	 */
	public GetSampleImageParams(){
		this.username = null;
		this.password = null;
		this.projectID = -1;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param projectKey
	 */
	public GetSampleImageParams(String username, String password, int projectKey){
		this.username = username;
		this.password = password;
		this.projectID = projectKey;
	}

	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
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

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	
}
