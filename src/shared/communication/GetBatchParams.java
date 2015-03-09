package shared.communication;

public class GetBatchParams {

	private String username;
	private String password;
	private int projectID;
	
	/**
	 * Initializes variables
	 */
	public GetBatchParams(){
		this.username = null;
		this.password = null;
		this.projectID = -1;
	}
	
	public GetBatchParams(String username, String password, int projectKey){
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
