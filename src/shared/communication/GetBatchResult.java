package shared.communication;

import java.util.List;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;

public class GetBatchResult {

	private boolean successful;
	private Batch batch;
	private List<Field> fields;
	private Project project;
	
	
	/**
	 * Base Constructor
	 */
	public GetBatchResult(){
		this.successful = false;
		this.setBatch(null);
		this.setFields(null);
		this.project = null;
	}

	/**
	 * 
	 * @param batch
	 */
	public GetBatchResult(Batch batch, Project project, List<Field> fields){
		this.successful = true;
		this.batch = batch;
		this.fields  = fields;
		this.project = project;
	}
	
	
	
	
	/*
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 * 
	 */
	
	public Batch getBatch(){
		return batch;
	}

	public void setBatch(Batch batch){
		this.batch = batch;
	}

	public boolean isSuccessful(){
		return successful;
	}

	public void setSuccessful(boolean successful){
		this.successful = successful;
	}
	
	public List<Field> getFields(){
		return fields;
	}

	public void setFields(List<Field> fields){
		this.fields = fields;
	}
	
	public Project getProject(){
		return project;
	}

	public void setProject(Project project){
		this.project = project;
	}	
	
	
	
	
	/*
	 * 
	 * 
	 * UTILITY THINGS
	 * 
	 * 
	 */
	
	public String toString(String URLPrefix) {	
		if(successful){
			StringBuilder sb = new StringBuilder();
			int added = 0;
			while(added != fields.size()){
				for(int i = 0; i < fields.size(); i++){
					if(fields.get(i).getRecordOrder() == added+1){
						sb.append(fields.get(i).toString(URLPrefix));
						added++;
					}
				}
			}
			return this.batch.toString(URLPrefix) 
					+ this.project.toString()
					+ this.fields.size() + "\n"
					+ sb.toString();
		}else
			return "FAILED\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		GetBatchResult other = (GetBatchResult) obj;
		if (successful != other.successful)
			return false;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else{
			if(fields.size() == other.fields.size()){
				for(int i = 0; i < fields.size(); i++){
					if(!other.fields.contains(fields.get(i)))
						return false;
				}
			}else
				return false;
		}
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}
}
