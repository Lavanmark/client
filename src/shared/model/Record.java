package shared.model;

public class Record {

	private int recordNum;
	private int projectKey;
	private int imageKey;
	private int fieldID;
	private String data;

	
	
	
	/**
	 * Base constructor
	 */
	public Record(){
		this.recordNum = -1;
		this.projectKey = -1;
		this.imageKey = -1;
		this.fieldID = -1;
		this.data = null;
	}
	
	/**
	 * 
	 * @param projectKey
	 * @param imageKey
	 * @param fieldID
	 * @param data
	 */
	public Record(int projectKey, int imageKey, int fieldID, String data){
		this.recordNum = -1;
		this.projectKey = projectKey;
		this.imageKey = imageKey;
		this.fieldID = fieldID;
		this.data = data;
	}
	
	/**
	 * 
	 * @param recordNum
	 * @param projectKey
	 * @param imageKey
	 * @param fieldID
	 * @param data
	 */
	public Record(int recordNum, int projectKey, int imageKey, int fieldID, String data){
		this.recordNum = recordNum;
		this.projectKey = projectKey;
		this.imageKey = imageKey;
		this.fieldID = fieldID;
		this.data = data;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public int getRecordNum(){
		return recordNum;
	}

	public void setRecordNum(int recordNum){
		this.recordNum = recordNum;
	}

	public int getProjectKey(){
		return projectKey;
	}

	public void setProjectKey(int projectKey){
		this.projectKey = projectKey;
	}

	public int getImageKey(){
		return imageKey;
	}

	public void setImageKey(int imageKey){
		this.imageKey = imageKey;
	}

	public int getFieldID(){
		return fieldID;
	}

	public void setFieldID(int fieldID){
		this.fieldID = fieldID;
	}

	public String getData(){
		return data;
	}

	public void setData(String data){
		this.data = data;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * UTITLITY THINGS
	 * 
	 * 
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + fieldID;
		result = prime * result + imageKey;
		result = prime * result + projectKey;
		result = prime * result + recordNum;
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
		Record other = (Record) obj;
		if (recordNum != other.recordNum)
			return false;
		if (fieldID != other.fieldID)
			return false;
		if (imageKey != other.imageKey)
			return false;
		if (projectKey != other.projectKey)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return data;
	}
}
