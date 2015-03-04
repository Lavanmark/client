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