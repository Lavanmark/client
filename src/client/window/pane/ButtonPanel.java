package client.window.pane;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.ClientException;
import client.backend.BatchState;
import client.backend.LoginState;
import client.backend.facade.ClientFacade;
import client.window.IndexerWindow;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel{

	private JButton zoomInBut;
	private JButton zoomOutBut;
	private JButton invertImageBut;
	private JButton toggleHighlightsBut;
	private JButton saveBut;
	private JButton submitBut;
	
	private ImageViewer imgViewer;
	//ONLY USE IN SUBMIT BATCH
	private IndexerWindow parentWin;
	
	
	/**
	 * 
	 * @param imgViewer
	 */
	public ButtonPanel(ImageViewer imgViewer, IndexerWindow win){
		super();
		
		this.imgViewer = imgViewer;
		this.parentWin = win;
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		createButtons();
		
		layOutButtons();
		
		disableButtons();
	}
	
	private void createButtons(){
		zoomInBut = new JButton("Zoom In");
		zoomInBut.addActionListener(buttonListener);
		
		zoomOutBut = new JButton("Zoom Out");
		zoomOutBut.addActionListener(buttonListener);
		
		invertImageBut = new JButton("Invert Image");
		invertImageBut.addActionListener(buttonListener);
		
		toggleHighlightsBut = new JButton("Toggle Highlights");
		toggleHighlightsBut.addActionListener(buttonListener);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(buttonListener);
		
		submitBut = new JButton("Submit");
		submitBut.addActionListener(buttonListener);
	}
	
	private void layOutButtons(){
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(zoomInBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(zoomOutBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(invertImageBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(toggleHighlightsBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(saveBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(submitBut);
		this.add(Box.createRigidArea(new Dimension(5, 35)));
		this.add(Box.createHorizontalGlue());
	}
	
	/**
	 * Disable all buttons
	 */
	public void disableButtons(){
		zoomInBut.setEnabled(false);
		zoomOutBut.setEnabled(false);
		invertImageBut.setEnabled(false);
		toggleHighlightsBut.setEnabled(false);
		saveBut.setEnabled(false);
		submitBut.setEnabled(false);
	}
	
	/**
	 * Enable all buttons
	 */
	public void enableButtons(){
		zoomInBut.setEnabled(true);
		zoomOutBut.setEnabled(true);
		invertImageBut.setEnabled(true);
		toggleHighlightsBut.setEnabled(true);
		saveBut.setEnabled(true);
		submitBut.setEnabled(true);
	}
	
	private ActionListener buttonListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == zoomInBut){
				imgViewer.zoomIn();
			}
			if(e.getSource() == zoomOutBut){
				imgViewer.zoomOut();
			}
			if(e.getSource() == invertImageBut){
				imgViewer.invertImage();
			}
			if(e.getSource() == toggleHighlightsBut){
				imgViewer.toggleHighlights();
			}
			if(e.getSource() == saveBut){
				parentWin.saveState();
			}
			if(e.getSource() == submitBut){
				//submit the changes and clear everything
				int res = JOptionPane.showConfirmDialog(parentWin, 
						"Are you sure you want to submit?",
						"Submit Batch",
						JOptionPane.YES_NO_OPTION);
				if(res == JOptionPane.YES_OPTION){
					boolean success = false;
					try {
						success = ClientFacade.submitBatch();
						//updates the user
					} catch (ClientException e1) {
						e1.printStackTrace();
						return;
					}
					if(success){
						parentWin.submitBatch();
					}else{
						JOptionPane.showMessageDialog(parentWin, 
								"Could not submit batch!\n"
								+ "Check your connection and try again!", 
								"Submit failed", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}		
	};
	
	
	
}
