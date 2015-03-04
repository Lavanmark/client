package shared.communication;

import shared.model.Batch;

public class GetBatchResult {

	
	private Batch batch;
	
	public GetBatchResult(){
		this.setBatch(null);
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}
