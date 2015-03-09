package shared.communication;

import java.util.List;

import shared.model.Field;

public class GetFieldsResult {

	private boolean successful;
	private List<Field> fields;
	
	
	/**
	 * Base Constructor
	 */
	public GetFieldsResult(){
		this.setSuccessful(false);
		this.fields = null;
	}
	
	public GetFieldsResult(List<Field> fields){
		this.setSuccessful(true);
		this.fields = fields;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
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
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + (successful ? 1231 : 1237);
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
		GetFieldsResult other = (GetFieldsResult) obj;
		if (successful != other.successful)
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(successful){
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < fields.size(); i++){
				sb.append(fields.get(i).toString());
			}
			return sb.toString();
		}else
			return "FAILED\n";
	}
	
}
