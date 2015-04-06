package client.window.table;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import client.backend.BatchStateListener;
import client.backend.Cell;

@SuppressWarnings("serial")
public class TablePanel extends JPanel implements BatchStateListener{

	private BatchTableModel tableModel;
	private JTable mainTable;
	
	
	public TablePanel(){
		super(new BorderLayout());
	}
	
	
	public void setupTable(){
		
		tableModel = new BatchTableModel();
		
		mainTable = new JTable(tableModel);
		mainTable.setRowHeight(20);
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.setCellSelectionEnabled(true);
		mainTable.getTableHeader().setReorderingAllowed(false);
		mainTable.addMouseListener(mouseAdapter);

		TableColumnModel columnModel = mainTable.getColumnModel();		
		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(50);
		}		
		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(new BatchCellRenderer());
		}
		this.add(mainTable.getTableHeader(), BorderLayout.NORTH);
		this.add(mainTable, BorderLayout.CENTER);
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent e) {

			if (e.isPopupTrigger()) {
				
				final int row = mainTable.rowAtPoint(e.getPoint());
				final int column = mainTable.columnAtPoint(e.getPoint());
				
				if (row >= 0 && row < tableModel.getRowCount() &&
						column >= 1 && column < tableModel.getColumnCount()) {
					
				}
			}
		}
		
	};


	@Override
	public void valueChanged(Cell cell, String newValue) {
		mainTable.setValueAt(newValue, cell.row, cell.column+1);
	}


	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		mainTable.changeSelection(newSelectedCell.row, newSelectedCell.column+1, false, false);
	}


	@Override
	public void tableChanged(String[][] table, Cell selectedCell) {
		if(table == null){
			mainTable = null;
			tableModel = null;
			repaint();
			return;
		}
		setupTable();
		for(int i = 0; i < table.length; i++){
			for(int c = 0; c < table[i].length; c++){
				mainTable.setValueAt(table[i][c], i, c+1);
			}
		}
	}
}
