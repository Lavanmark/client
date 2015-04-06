package shared.model;

import java.io.Serializable;

/**
 * 
 * @author Tyler Draughon
 * 
 *
 */

public class Field implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int projectKey;
	private int recordOrder;
	
	private String title;
	private String helpHtml;
	private String knownData;
	
	private int xCoord;
	private int width;
	
	
	
	
	/**
	 * Base constructor
	 */
	public Field(){
		this.id = -1;
		this.projectKey = -1;
		this.recordOrder = -1;
		this.title = null;
		this.helpHtml = null;
		this.knownData = null;
		this.xCoord = -1;
		this.width = -1;
	}
	
	/**
	 * 
	 * @param id
	 */
	public Field(int key, String whatfor){
		if(whatfor.equals("id")){
			this.id = key;
			this.projectKey = -1;
		}else if(whatfor.equals("project")){
			this.id = -1;
			this.projectKey = key;
		}else{
			this.id = -1;
			this.projectKey = -1;
		}
		this.recordOrder = -1;
		this.title = null;
		this.helpHtml = null;
		this.knownData = null;
		this.xCoord = -1;
		this.width = -1;
	}
	
	/**
	 * 
	 * @param projectKey
	 * @param recordOrder
	 * @param title
	 * @param htmlHelp
	 * @param knownData
	 * @param xCoord
	 * @param width
	 */
	public Field(int projectKey, int recordOrder, String title, String htmlHelp, String knownData, int xCoord, int width){
		this.id = -1;
		this.projectKey = projectKey;
		this.recordOrder = recordOrder;
		this.title = title;
		this.helpHtml = htmlHelp;
		this.knownData = knownData;
		this.xCoord = xCoord;
		this.width = width;
	}
	
	/**
	 * 
	 * @param projectKey
	 * @param recordOrder
	 * @param title
	 * @param htmlHelp
	 * @param xCoord
	 * @param width
	 */
	public Field(int projectKey, int recordOrder, String title, String htmlHelp, int xCoord, int width){
		this.id = -1;
		this.projectKey = projectKey;
		this.recordOrder = recordOrder;
		this.title = title;
		this.helpHtml = htmlHelp;
		this.knownData = null;
		this.xCoord = xCoord;
		this.width = width;
	}
	
	/**
	 * 
	 * @param id
	 * @param projectKey
	 * @param recordOrder
	 * @param title
	 * @param htmlHelp
	 * @param xCoord
	 * @param width
	 */
	public Field(int id, int projectKey, int recordOrder, String title, String htmlHelp, int xCoord, int width){
		this.id = id;
		this.projectKey = projectKey;
		this.recordOrder = recordOrder;
		this.title = title;
		this.helpHtml = htmlHelp;
		this.knownData = null;
		this.xCoord = xCoord;
		this.width = width;
	}
	
	/**
	 * 
	 * @param id
	 * @param projectKey
	 * @param recordOrder
	 * @param title
	 * @param htmlHelp
	 * @param knownData
	 * @param xCoord
	 * @param width
	 */
	public Field(int id, int projectKey, int recordOrder, String title, String htmlHelp, String knownData, int xCoord, int width){
		this.id = id;
		this.projectKey = projectKey;
		this.recordOrder = recordOrder;
		this.title = title;
		this.helpHtml = htmlHelp;
		this.knownData = knownData;
		this.xCoord = xCoord;
		this.width = width;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getHelpHtml(){
		return helpHtml;
	}
	
	public void setHelpHtml(String helpHtml){
		this.helpHtml = helpHtml;
	}
	
	public String getKnownData(){
		return knownData;
	}
	
	public void setKnownData(String knownData){
		this.knownData = knownData;
	}
	
	public int getxCoord(){
		return xCoord;
	}
	
	public void setxCoord(int xCoord){
		this.xCoord = xCoord;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width){
		this.width = width;
	}

	public int getProjectKey(){
		return projectKey;
	}

	public void setProjectKey(int projectKey){
		this.projectKey = projectKey;
	}

	public int getRecordOrder(){
		return recordOrder;
	}

	public void setRecordOrder(int recordOrder){
		this.recordOrder = recordOrder;
	}
	
	public int getId(){
		return this.id;
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
				+ ((helpHtml == null) ? 0 : helpHtml.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((knownData == null) ? 0 : knownData.hashCode());
		result = prime * result + projectKey;
		result = prime * result + recordOrder;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		result = prime * result + xCoord;
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
		Field other = (Field) obj;
		if (id != other.id)
			return false;
		if (helpHtml == null) {
			if (other.helpHtml != null)
				return false;
		} else if (!helpHtml.equals(other.helpHtml))
			return false;
		if (knownData == null) {
			if (other.knownData != null)
				return false;
		} else if (!knownData.equals(other.knownData))
			return false;
		if (projectKey != other.projectKey)
			return false;
		if (recordOrder != other.recordOrder)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		if (xCoord != other.xCoord)
			return false;
		return true;
	}

	public String toString(String URLPrefix) {
		//to string for download batch
		if(this.knownData == null)
			return id + "\n"
					+ recordOrder + "\n"
					+ title + "\n"
					+ URLPrefix + "/" + helpHtml + "\n"
					+ xCoord + "\n"
					+ width + "\n";
		else
			return id + "\n"
					+ recordOrder + "\n"
					+ title + "\n"
					+ URLPrefix + "/" + helpHtml + "\n"
					+ xCoord + "\n"
					+ width + "\n"
					+ URLPrefix + "/" + knownData + "\n";
	}

	@Override
	public String toString() {
		return title;
		//to string for get fields
		/*return projectKey + "\n"
				+ id + "\n"
				+ title + "\n";
				*/
	}
}
