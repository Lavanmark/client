package shared.communication;

public class AddBatchResult {

	private boolean succeded;
	
	
	
	
	/**
	 * Base constructor. Defaults success to false
	 */
	public AddBatchResult(){
		setSucceded(false);
	}
	
	/**
	 * 
	 * @param succeded
	 */
	public AddBatchResult(boolean succeded){
		setSucceded(succeded);
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public boolean hasSucceded() {
		return succeded;
	}
	
	public void setSucceded(boolean succeded) {
		this.succeded = succeded;
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
		result = prime * result + (succeded ? 1231 : 1237);
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
		AddBatchResult other = (AddBatchResult) obj;
		if (succeded != other.succeded)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		if(succeded)
			return "TRUE\n";
		else
			return "FAILED\n";
	}
}
