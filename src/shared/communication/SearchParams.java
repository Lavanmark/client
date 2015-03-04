package shared.communication;

public class SearchParams {

	private String username;
	private String password;
	private String fields;
	private String searchwords;
	
	public SearchParams(){
		this.username = null;
		this.password = null;
		this.fields = null;
		this.searchwords = null;
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

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getSearchwords() {
		return searchwords;
	}

	public void setSearchwords(String searchwords) {
		this.searchwords = searchwords;
	}
}
