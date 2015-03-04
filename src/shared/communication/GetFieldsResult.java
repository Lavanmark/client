package shared.communication;

import shared.model.Field;

public class GetFieldsResult {

	private int projectID;
	private Field[] fields;
	
	public GetFieldsResult(int numberOfFields){
		this.fields = new Field[numberOfFields];
		this.projectID = -1;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}
	
}
