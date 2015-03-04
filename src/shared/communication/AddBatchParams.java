package shared.communication;

import shared.model.Batch;

public class AddBatchParams {
	 private String username;
	 private String password;
	 private Batch batch;
	 
	 
	 public AddBatchParams(){
		 this.username = null;
		 this.password = null;
		 this.batch = null;
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


	public Batch getBatch() {
		return batch;
	}


	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}
