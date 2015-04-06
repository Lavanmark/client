package client.window;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.communication.GetBatchResult;
import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import client.ClientException;
import client.backend.BatchState;
import client.backend.LoginState;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class DownloadBatchWindow extends JDialog{

	private JPanel masterPane;
	private JComboBox<String> projectList;
	private String[] projectArray;
	private JButton sampleImageBut;
	private JButton cancelBut;
	private JButton downloadBut;
	private List<Project> projects;
	
	private boolean batchDownloaded;
	
	public DownloadBatchWindow(JFrame parent){
		super(parent,"Download Batch", true);
		this.setResizable(false);
		this.setLocationRelativeTo(parent);
		createComponents();
		this.requestFocus();
		this.setContentPane(masterPane);
		this.pack();
		this.setVisible(true);
		batchDownloaded = false;
	}
	
	private void createComponents(){
		masterPane = new JPanel();
		masterPane.setLayout(new BoxLayout(masterPane, BoxLayout.Y_AXIS));
		
		//TODO make separate pane for this crap
		
		JPanel projectSelectPane = new JPanel();
		projectSelectPane.setLayout(new BoxLayout(projectSelectPane, BoxLayout.X_AXIS));
		
		JLabel projectLabel = new JLabel("Project:");
		projectSelectPane.add(Box.createRigidArea(new Dimension(5,5)));
		projectSelectPane.add(projectLabel);
		projectSelectPane.add(Box.createRigidArea(new Dimension(5,5)));
		
		projects = null;
		try {
			projects = ClientFacade.getAllProjects(LoginState.getUserOn().getUsername(), LoginState.getUserOn().getPassword());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(projects != null){
			projectArray = new String[projects.size()];
			for(int i = 0; i < projects.size(); i++){
				projectArray[i] = projects.get(i).getTitle();
			}
		}
		
		projectList = new JComboBox<String>(projectArray);
		projectList.setSelectedIndex(0);
		
		projectSelectPane.add(Box.createRigidArea(new Dimension(5,5)));
		projectSelectPane.add(projectList);
		projectSelectPane.add(Box.createRigidArea(new Dimension(5,5)));
		
		sampleImageBut = new JButton("View Sample");
		sampleImageBut.addActionListener(actionListener);
		
		projectSelectPane.add(sampleImageBut);
		projectSelectPane.add(Box.createRigidArea(new Dimension(5,5)));
		
		//TODO finish adding buttons to a different pane then add the two panes to the main pane
		JPanel downloadPane = new JPanel();
		downloadPane.setLayout(new BoxLayout(downloadPane, BoxLayout.X_AXIS));
		
		cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(actionListener);
		
		downloadPane.add(Box.createHorizontalGlue());
		downloadPane.add(cancelBut);
		downloadPane.add(Box.createRigidArea(new Dimension(5,5)));
		
		downloadBut = new JButton("Download");
		downloadBut.addActionListener(actionListener);
		
		downloadPane.add(downloadBut);
		downloadPane.add(Box.createHorizontalGlue());
		
		masterPane.add(Box.createRigidArea(new Dimension(5,5)));
		masterPane.add(projectSelectPane);
		masterPane.add(Box.createRigidArea(new Dimension(5,5)));
		masterPane.add(downloadPane);
		masterPane.add(Box.createRigidArea(new Dimension(5,5)));
		
	}
	
	private void createErrorPane(String msg, String title){
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
	}
	
	private void downloadBatch(){
		
		GetBatchResult result = null;
		try {
			result = ClientFacade.downloadBatch(LoginState.getUserOn().getUsername(), LoginState.getUserOn().getPassword(), projects.get(projectList.getSelectedIndex()).getKey());
			//update user
			LoginState.updateUser(ClientFacade.validateUser(LoginState.getUserOn().getUsername(), LoginState.getUserOn().getPassword()));
		} catch (ClientException e) {
			e.printStackTrace();
			createErrorPane("Client Exception. " + e.getMessage(), "Error");
			return;
		}
		
		if(!result.isSuccessful()){
			createErrorPane("Result was not successful!!","Error");
			return;
		}
		
		Project prj = result.getProject();
		Batch bch = result.getBatch();
		List<Field> fields = result.getFields();
		
		BatchState.initialize(prj.getRecordsPerImage(), fields.size(), fields, bch, prj);
		batchDownloaded = true;
	}
	
	private void viewSampleImage(){
		String urlPrefix = ClientFacade.getURLPrefix();
		Project prj = projects.get(projectList.getSelectedIndex());
		String url = null;
		try {
			url = ClientFacade.getSampleImage(LoginState.getUserOn().getUsername(), LoginState.getUserOn().getPassword(), prj.getKey());
		} catch (ClientException e1) {
			e1.printStackTrace();
			return;
		}
		String title = prj.getTitle();
		if(url != null){
			
			Image img = null;
			URL imgURL = null;
			try {
				imgURL = new URL(urlPrefix+ "/" + url);
			} catch (MalformedURLException e2) {
				e2.printStackTrace();
			}
			try {
				img = ImageIO.read(imgURL);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			img = img.getScaledInstance(img.getWidth(null) / 2, img.getHeight(null) / 2, Image.SCALE_DEFAULT);
			
			JLabel picLabel = new JLabel(new ImageIcon(img));
			JOptionPane.showMessageDialog(null, picLabel, "Sample Image from " + title, JOptionPane.PLAIN_MESSAGE, null);
			
			//JDialog sampleImageDisplay = new JDialog(DownloadBatchWindow.this,"Sample Image from " + title,true);
			//sampleImageDisplay.setContentPane(imgPane);
		}else{
			System.err.println("url non existent!!");
		}
	}
	
	private void exit(){
		this.setVisible(false);
	}
	
	private ActionListener actionListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == sampleImageBut){
				viewSampleImage();
			}
			if(e.getSource() == downloadBut){
				downloadBatch();
				if(batchDownloaded)
					exit();
			}
			if(e.getSource() == cancelBut){
				exit();
			}
			
		}
		
	};
	
	
}
