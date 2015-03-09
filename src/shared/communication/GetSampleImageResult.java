package shared.communication;

public class GetSampleImageResult {

	private String imgURL;
	private boolean successful;
	
	/**
	 * Base Constructor
	 */
	public GetSampleImageResult(){
		this.imgURL = null;
		this.setSuccessful(false);
	}
	
	/**
	 * 
	 * @param imgURL
	 */
	public GetSampleImageResult(String imgURL){
		this.imgURL = imgURL;
		this.setSuccessful(true);
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public String getImageurl() {
		return imgURL;
	}

	public void setImageurl(String imageurl) {
		this.imgURL = imageurl;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean succesful) {
		this.successful = succesful;
	}

	
	/*
	 * 
	 * 
	 * UTILITY THINGS
	 * 
	 * 
	 */
	
	@Override
	public String toString() {
		if(successful)
			return imgURL + "\n";
		else
			return "FAILED\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imgURL == null) ? 0 : imgURL.hashCode());
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
		GetSampleImageResult other = (GetSampleImageResult) obj;
		if (successful != other.successful)
			return false;
		if (imgURL == null) {
			if (other.imgURL != null)
				return false;
		} else if (!imgURL.equals(other.imgURL))
			return false;
		return true;
	}
}
