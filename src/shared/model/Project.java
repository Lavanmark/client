package shared.model;

public class Project {

	
	private int key;
	private int recordsPerImage;
	private int firstYCoord;
	private int recordHeight;
	private String title;
	
	/**
	 * Base constructor
	 */
	public Project(){
		this.key = -1;
		this.recordsPerImage = -1;
		this.firstYCoord = -1;
		this.recordHeight = -1;
		this.title = null;
	}
	
	/**
	 * 
	 * @param title
	 */
	public Project(String title){
		this.key = -1;
		this.recordsPerImage = -1;
		this.firstYCoord = -1;
		this.recordHeight = -1;
		this.title = title;
	}
	
	/**
	 * 
	 * @param recordsPerImage
	 * @param firstYCoord
	 * @param recordHeight
	 * @param title
	 */
	public Project(int recordsPerImage, int firstYCoord, int recordHeight, String title){
		this.key = -1;
		this.recordsPerImage = recordsPerImage;
		this.firstYCoord = firstYCoord;
		this.recordHeight = recordHeight;
		this.title = title;
	}
	
	/**
	 * 
	 * @param key
	 * @param recordsPerImage
	 * @param firstYCoord
	 * @param recordHeight
	 * @param title
	 */
	public Project(int key, int recordsPerImage, int firstYCoord, int recordHeight, String title){
		this.key = key;
		this.recordsPerImage = recordsPerImage;
		this.firstYCoord = firstYCoord;
		this.recordHeight = recordHeight;
		this.title = title;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public int getRecordsPerImage(){
		return recordsPerImage;
	}
	
	public void setRecordsPerImage(int recordsPerImage){
		this.recordsPerImage = recordsPerImage;
	}
	
	public int getFirstYCoord(){
		return firstYCoord;
	}
	
	public void setFirstYCoord(int firstYCoord){
		this.firstYCoord = firstYCoord;
	}
	
	public int getRecordHeight(){
		return recordHeight;
	}
	
	public void setRecordHeight(int recordHeight){
		this.recordHeight = recordHeight;
	}
	
	public int getKey(){
		return key;
	}
	
	public void setKey(int key){
		this.key = key;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
		result = prime * result + firstYCoord;
		result = prime * result + key;
		result = prime * result + recordHeight;
		result = prime * result + recordsPerImage;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Project other = (Project) obj;
		if (key != other.key)
			return false;
		if (firstYCoord != other.firstYCoord)
			return false;
		if (recordHeight != other.recordHeight)
			return false;
		if (recordsPerImage != other.recordsPerImage)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
