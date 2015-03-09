package shared.communication;

import shared.model.User;

public class ValidateUserResult {

	private boolean valid;
	private User usr;
	
	
	
	
	/**
	 * Defaults user to null
	 * @param valid
	 */
	public ValidateUserResult(boolean valid){
		this.valid = valid;
		this.usr = null;
	}
	/**
	 * Create a user validation result 
	 * @param valid
	 * @param usr
	 */
	public ValidateUserResult(boolean valid, User usr){
		this.valid = valid;
		this.usr = usr;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public User getUsr() {
		return usr;
	}
	public void setUsr(User usr) {
		this.usr = usr;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * UTILITY THINGS
	 * 
	 * 
	 */
	
	@Override
	public String toString() {
		if(valid)
			return "TRUE\n" + usr.getFirstName() + "\n" + usr.getLastName() + "\n" + usr.getRecordsIndexed() + "\n";
		else
			return "FALSE\n";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usr == null) ? 0 : usr.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
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
		ValidateUserResult other = (ValidateUserResult) obj;
		if (usr == null) {
			if (other.usr != null)
				return false;
		} else if (!usr.equals(other.usr))
			return false;
		if (valid != other.valid)
			return false;
		return true;
	}
}
