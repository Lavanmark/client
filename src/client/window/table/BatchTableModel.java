package client.window.table;

import javax.swing.table.AbstractTableModel;

import client.backend.BatchState;
import client.backend.Cell;

@SuppressWarnings("serial")
public class BatchTableModel extends  AbstractTableModel {

	private int columns;
	private int rows;
	
	
	public BatchTableModel() {
		columns = BatchState.getColumns()+1;
		rows = BatchState.getRows();
		
		
	}

	@Override
	public int getColumnCount() {
		return columns;
	}

	@Override
	public String getColumnName(int column) {
		if (column >= 0 && column < getColumnCount()) {
			if(column == 0)
				return "Record Number";
			else
				return BatchState.getField(column-1).getTitle();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int getRowCount() {
		return rows;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		BatchState.setSelectedCell(new Cell(row,column-1));
		if(column > 0)
			return true;
		else
			return false;
	}

	@Override
	public Object getValueAt(int row, int column) {

		Object result = null;

		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {
			if(column > 0)
				result = BatchState.getValue(new Cell(row,column-1));
			else if(column == 0)
				result = "" +(row+1);
		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		
		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {
			if(BatchState.getValue(new Cell(row,column-1)) != null){
				if(!BatchState.getValue(new Cell(row,column-1)).equals(value))
					BatchState.setValue(new Cell(row,column-1),(String)value);
			}else if(value != null){
				BatchState.setValue(new Cell(row,column-1),(String)value);
			}
			this.fireTableCellUpdated(row, column);
			
		} else {
			throw new IndexOutOfBoundsException();
		}		
	}

}
