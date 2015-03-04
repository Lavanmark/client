package shared.communication;

public class AddBatchResult {

	private boolean succeded;
	
	
	public AddBatchResult(){
		setSucceded(false);
	}


	public boolean hasSucceded() {
		return succeded;
	}


	public void setSucceded(boolean succeded) {
		this.succeded = succeded;
	}
	
}
