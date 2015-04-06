package client.window.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import client.backend.BatchState;
import client.backend.Cell;

@SuppressWarnings("serial")
public class BatchCellRenderer extends JLabel implements TableCellRenderer {

	
	private Border selectedBorder = BorderFactory.createLineBorder(Color.BLUE, 2);

	public BatchCellRenderer() {
		
		setOpaque(true);
		setFont(getFont().deriveFont(15.0f));
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		boolean misspelled = false;
		Color myc = new Color(0,128,255,128);
		//TODO make this check spelling
		if(isSelected){
			Cell cell = new Cell(row,column-1);
			//TODO write a equals for cell
			if(!BatchState.getSelectedCell().equals(cell))
			this.setBackground(myc);
			this.setBorder(selectedBorder);
		}else{
			this.setBackground(Color.WHITE);
			this.setBorder(null);
		}
		
		if(misspelled)
			this.setBackground(Color.RED);
		
		this.setText((String)value);
		
		return this;
	}

}
