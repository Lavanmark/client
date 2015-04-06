package client.backend;

import java.io.Serializable;

public class Cell implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int row;
	public int column;
	
	public Cell(int row, int column){
		this.row = row;
		this.column = column;
	}
}
