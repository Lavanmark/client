package shared.model;

import java.io.Serializable;

//rename this class.
public class Batch implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int projectKey;
	private String imgFile;
	/*
	 * -1 = Uninitialized 
	 * 0 = not in use
	 * 1 = in use
	 * 2 = complete
	 */
	private int status;
	
	
	
	
	/**
	 * Base constructor
	 */
	public Batch(){
		this.id = -1;
		this.projectKey = -1;
		this.imgFile = null;
		this.status = -1;
	}
	
	/**
	 * 
	 * @param key
	 * @param whatfor "id" for id "project" for project key
	 */
	public Batch(int key, String whatfor){
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
		this.imgFile = null;
		this.status = -1;
	}
	
	/**
	 * 
	 * @param projectKey
	 * @param fileString
	 * @param status
	 */
	public Batch(int projectKey, String fileString, int status){
		this.id = -1;
		this.projectKey = projectKey;
		this.imgFile = fileString;
		this.status = status;
	}
	
	/**
	 * 
	 * @param id
	 * @param projectKey
	 * @param fileString
	 * @param status
	 */
	public Batch(int id, int projectKey, String fileString, int status){
		this.id = id;
		this.projectKey = projectKey;
		this.imgFile = fileString;
		this.status = status;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public String getImgFile(){
		return imgFile;
	}
	
	public void setImgFile(String imgFile){
		this.imgFile = imgFile;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getProjectKey(){
		return projectKey;
	}
	
	public void setProjectKey(int projectKey){
		this.projectKey = projectKey;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int status){
		this.status = status;
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
		result = prime * result + id;
		result = prime * result + ((imgFile == null) ? 0 : imgFile.hashCode());
		result = prime * result + projectKey;
		result = prime * result + status;
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
		Batch other = (Batch) obj;
		if (id != other.id)
			return false;
		if (imgFile == null) {
			if (other.imgFile != null)
				return false;
		} else if (!imgFile.equals(other.imgFile))
			return false;
		if (projectKey != other.projectKey)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	public String toString(String URLPrefix) {
		
		return this.id + "\n"
				+ this.projectKey + "\n"
				+ URLPrefix + "/" + this.imgFile + "\n";
	}
	
}
