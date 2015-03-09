package shared.communication;

public class SearchParams {

	private String username;
	private String password;
	private String fields;
	private String searchWords;
	
	
	
	
	/**
	 * Base constructor
	 */
	public SearchParams(){
		this.username = null;
		this.password = null;
		this.fields = null;
		this.searchWords = null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param fieldString
	 * @param searchWords
	 */
	public SearchParams(String username, String password, String fieldString, String searchWords){
		this.username = username;
		this.password = password;
		this.fields = fieldString;
		this.searchWords = searchWords;
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

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getSearchwords() {
		return searchWords;
	}

	public void setSearchwords(String searchWords) {
		this.searchWords = searchWords;
	}
}
