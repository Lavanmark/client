package client.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.*;

public class BatchState implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static String[][] values;
	private static Cell selectedCell;
	private static List<Field> fields;
	private static Batch currentBatch;
	private static Project currentProject;
	
	private transient static List<BatchStateListener> listeners = new ArrayList<BatchStateListener>();
	private void writeObject(ObjectOutputStream oos)
	        throws IOException 
	    {
	        oos.defaultWriteObject();
	        oos.writeObject(values);
	        oos.writeObject(selectedCell);
	        oos.writeObject(fields);
	        oos.writeObject(currentBatch);
	        oos.writeObject(currentProject);
	    }

	    @SuppressWarnings("unchecked")
		private void readObject(ObjectInputStream ois)
	    throws ClassNotFoundException, IOException 
	    {
	        ois.defaultReadObject();
	        values = (String[][])ois.readObject();
	        selectedCell = (Cell)ois.readObject();
	        fields = (List<Field>)ois.readObject();
	        currentBatch = (Batch)ois.readObject();
	        currentProject = (Project)ois.readObject();
	    }
	
	
	public static void initialize(int row, int column, List<Field> fieldList, Batch batch, Project project) {
		values = new String[row][column];
		selectedCell = null;
		fields = fieldList;
		currentBatch = batch;
		currentProject = project;
		
		for(BatchStateListener l : listeners){
			l.tableChanged(values, selectedCell);
		}
	}
	
	public static void clearCells(){
		values = null;
		selectedCell = null;
		fields = null;
		currentBatch = null;
		currentProject = null;
		for(BatchStateListener l : listeners){
			l.tableChanged(values, selectedCell);
		}
	}
	
	public static void addListener(BatchStateListener l) {
		listeners.add(l);
	}
	
	public static void setValue(Cell cell, String value){
		
		values[cell.row][cell.column] = value;
		
		for(BatchStateListener l : listeners){
			l.valueChanged(cell, value);
		}
	}
	
	public static String getValue(Cell cell){
		return values[cell.row][cell.column];
	}
	
	public static void setSelectedCell(Cell selCell){
		
		selectedCell = selCell;
		
		for(BatchStateListener l : listeners){
			l.selectedCellChanged(selCell);
		}
	}
	
	public static Cell getSelectedCell(){
		return selectedCell;
	}
	
	public static Field getField(int index){
		if(fields != null)
			return fields.get(index);
		return null;
	}
	
	public static Batch getBatch(){
		return currentBatch;
	}
	
	public static Project getProject(){
		return currentProject;
	}
	
	public static int getRows(){
		if(values != null)
			return values.length;
		return 0;
	}
	public static int getColumns(){
		if(values != null)
			return values[0].length;
		return 0;
	}
	
	public static void tableUpdate(){
		for(BatchStateListener l : listeners){
			l.tableChanged(values, selectedCell);
		}
	}
	
	public static String submitBatchString(){
		StringBuilder sb = new StringBuilder();
		if(values != null){
			for(int o = 0; o < values.length; o++){
				for(int i = 0; i < values[o].length; i++){
					sb.append(values[o][i]);
					if(i < values[o].length-1)
						sb.append(',');
				}
				sb.append(';');
			}
		}
		return sb.toString();
	}
}




