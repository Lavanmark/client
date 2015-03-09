package shared.communication;


import java.util.List;

import shared.model.Project;

public class GetAllProjectResult {

	private boolean allIsWell;
	private List<Project> projects;
	
	
	
	
	/**
	 * Initialize projects list
	 */
	public GetAllProjectResult(){
		this.setAllIsWell(false);
		this.projects = null;
	}
	
	/**
	 * Initialize projects list
	 */
	public GetAllProjectResult(List<Project> projects){
		this.setAllIsWell(true);
		this.projects = projects;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public boolean isAllIsWell() {
		return allIsWell;
	}

	public void setAllIsWell(boolean allIsWell) {
		this.allIsWell = allIsWell;
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
		if(allIsWell){
			StringBuilder sb = new StringBuilder("");
			for(int i = 0; i < projects.size(); i++){
				sb.append(projects.get(i).getKey()+ "\n");
				sb.append(projects.get(i).getTitle()+"\n");
			}
			return sb.toString();
		}else{
			return "FAILED\n";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allIsWell ? 1231 : 1237);
		result = prime * result
				+ ((projects == null) ? 0 : projects.hashCode());
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
		GetAllProjectResult other = (GetAllProjectResult) obj;
		if (allIsWell != other.allIsWell)
			return false;
		if (projects == null) {
			if (other.projects != null)
				return false;
		} else {
			if(projects.size() != other.projects.size())
				return false;
			for(int i = 0; i < projects.size(); i++){
				if(!other.projects.contains(projects.get(i)))
					return false;
			}
		}
		return true;
	}
}
