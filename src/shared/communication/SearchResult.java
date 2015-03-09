package shared.communication;

import java.util.List;

public class SearchResult {

	private boolean successful;
	private List<SearchTuple> searchTuples;
	
	
	
	
	/**
	 * Base Constructor
	 */
	public SearchResult(){
		this.successful = false;
		this.searchTuples = null;
	}
	
	/**
	 * 
	 * @param searchTuples
	 */
	public SearchResult(List<SearchTuple> searchTuples){
		this.successful = true;
		this.searchTuples = searchTuples;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public List<SearchTuple> getSearchTuples() {
		return searchTuples;
	}

	public void setSearchTuples(List<SearchTuple> searchTuples) {
		this.searchTuples = searchTuples;
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
				+ ((searchTuples == null) ? 0 : searchTuples.hashCode());
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
		SearchResult other = (SearchResult) obj;
		if (searchTuples == null) {
			if (other.searchTuples != null)
				return false;
		} else if (!searchTuples.equals(other.searchTuples))
			return false;
		if (successful != other.successful)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		if(successful){
			if(searchTuples.size() > 0){
				return "FAILED\n";
			}
			StringBuilder sb = new StringBuilder();
			for(SearchTuple st : searchTuples){
				sb.append(st.toString());
			}
			return sb.toString();
		}else
			return "FAILED\n";
	}
	
	







}