package shared.communication;

import shared.model.Batch;

public class AddBatchParams {
	private String username;
	private String password;
	private Batch batch;
	private String recordData; 
	
	
	
	/**
	 * Base constructor
	 */
	public AddBatchParams(){
		this.username = null;
		this.password = null;
		this.batch = null;
		this.recordData = null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param batch
	 * @param recordData
	 */
	public AddBatchParams(String username, String password, Batch batch, String recordData){
		this.username = username;
		this.password = password;
		this.batch = batch;
		this.recordData = recordData;
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

	public Batch getBatch() {
		return batch;
	}
	
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public String getRecordData() {
		return recordData;
	}

	public void setRecordData(String recordData) {
		this.recordData = recordData;
	}

}
