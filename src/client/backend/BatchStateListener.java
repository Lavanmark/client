package client.backend;

public interface BatchStateListener {
	public void valueChanged(Cell cell, String newValue);
	public void selectedCellChanged(Cell newSelectedCell);
	public void tableChanged(String[][] table, Cell selectedCell);
}
