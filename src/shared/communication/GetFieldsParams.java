package shared.communication;

public class GetFieldsParams {

	private String username;
	private String password;
	private boolean allFields;
	private int projectID;
	
	
	
	
	/**
	 * Base Constructor
	 */
	public GetFieldsParams(){
		this.username = null;
		this.password = null;
		this.projectID = -1;
		this.allFields = false;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public GetFieldsParams(String username, String password){
		this.username = username;
		this.password = password;
		this.allFields = true;
		this.projectID = -1;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param projectID
	 */
	public GetFieldsParams(String username, String password, int projectID){
		this.username = username;
		this.password = password;
		this.allFields = false;
		this.projectID = projectID;
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

	public boolean isAllFields() {
		return allFields;
	}

	public void setAllFields(boolean allFields) {
		this.allFields = allFields;
	}
}
