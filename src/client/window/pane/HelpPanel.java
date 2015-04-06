package client.window.pane;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JEditorPane;

import shared.model.Field;
import client.backend.BatchState;
import client.backend.BatchStateListener;
import client.backend.Cell;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class HelpPanel extends JEditorPane implements BatchStateListener{
	
	public HelpPanel(){
		super();
		
		setupPane();
		
		setMessage("No help data");
	}
	
	private void setupPane(){
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setEditable(false);
		this.setContentType("text/html");
	}
	
	public void setMessage(String msg){
		this.setText("<html><body>" + msg + "</body></html>");
	}
	
	public void loadHelp(String url){
		try {
			this.setPage(ClientFacade.getURLPrefix() + "/" + url);
		} catch (IOException e) {
			e.printStackTrace();
			setMessage("Help data could not be displayed!");
		}
	}

	@Override
	public void valueChanged(Cell cell, String newValue) {
		//do nothing
	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		if(newSelectedCell == null){
			setMessage("No help data");
			return;
		}
		int fieldLocal = newSelectedCell.column;
		if(fieldLocal > -1){
			Field currentField = BatchState.getField(fieldLocal);
			String helpURL = currentField.getHelpHtml();
			if(helpURL != null)
				loadHelp(helpURL);
			else
				setMessage("No help data");
			return;
		}
		setMessage("No help data");
	}

	@Override
	public void tableChanged(String[][] table, Cell selectedCell) {
		selectedCellChanged(selectedCell);
	}
	
}
