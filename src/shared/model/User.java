package shared.model;

public class User {

	
	private int id;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int recordsIndexed;
	private String currentBatch;
	
	
	
	
	/**
	 * Base constructor
	 */
	public User(){
		this.id = -1;
		this.username = null;
		this.password = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.recordsIndexed = -1;
		this.currentBatch = null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public User(String username, String password){
		this.id = -1;
		this.username = username;
		this.password = password;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.recordsIndexed = -1;
		this.currentBatch = null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param recordsIndexed
	 * @param currentBatch
	 */
	public User(String username, String password, String firstName, String lastName, String email, int recordsIndexed, String currentBatch){
		this.id = -1;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.recordsIndexed = recordsIndexed;
		this.currentBatch = currentBatch;
	}
	
	/**
	 * 
	 * @param id
	 * @param username
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param recordsIndexed
	 * @param currentBatch
	 */
	public User(int id, String username, String password, String firstName, String lastName, String email, int recordsIndexed, String currentBatch){
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.recordsIndexed = recordsIndexed;
		this.currentBatch = currentBatch;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public int getRecordsIndexed(){
		return recordsIndexed;
	}

	public void setRecordsIndexed(int recordsIndexed){
		this.recordsIndexed = recordsIndexed;
	}

	public String getCurrentBatch(){
		return currentBatch;
	}

	public void setCurrentBatch(String currentBatch){
		this.currentBatch = currentBatch;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * UTILITY THINGS
	 * 
	 * 
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentBatch == null) ? 0 : currentBatch.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + recordsIndexed;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (currentBatch == null) {
			if (other.currentBatch != null)
				return false;
		} else if (!currentBatch.equals(other.currentBatch))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (recordsIndexed != other.recordsIndexed)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
