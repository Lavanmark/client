package shared.communication;


/**
 * Class for the purpose of showing results for searches. Will be very useful in future
 * @author Tyler
 *
 */
public class SearchTuple{
	
	private int batchID;
	private String imgURL;
	private int recordNum;
	private int fieldID;
	
	
	
	
	/**
	 * Base Constructor
	 */
	public SearchTuple(){
		this.batchID = -1;
		this.imgURL = null;
		this.recordNum = -1;
		this.fieldID = -1;
	}
	
	/**
	 * 
	 * @param batchID
	 * @param imgURL
	 * @param recordNum
	 * @param fieldID
	 */
	public SearchTuple(int batchID, String imgURL, int recordNum, int fieldID){
		this.batchID = batchID;
		this.imgURL = imgURL;
		this.recordNum = recordNum;
		this.fieldID = fieldID;
	}
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public int getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
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
		result = prime * result + batchID;
		result = prime * result + fieldID;
		result = prime * result
				+ ((imgURL == null) ? 0 : imgURL.hashCode());
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
		SearchTuple other = (SearchTuple) obj;
		if (batchID != other.batchID)
			return false;
		if (fieldID != other.fieldID)
			return false;
		if (imgURL == null) {
			if (other.imgURL != null)
				return false;
		} else if (!imgURL.equals(other.imgURL))
			return false;
		if (recordNum != other.recordNum)
			return false;
		return true;
	}

	public String toString(String URLPrefix) {
		return this.batchID + "\n"
				+ URLPrefix + "/" +this.imgURL + "\n"
				+ this.recordNum + "\n"
				+ this.fieldID + "\n";
	}
}
