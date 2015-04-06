package client.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import shared.model.User;
import client.ClientException;
import client.backend.BatchState;
import client.backend.LoginState;
import client.backend.facade.ClientFacade;
import client.window.pane.ButtonPanel;
import client.window.pane.FormPanel;
import client.window.pane.HelpPanel;
import client.window.pane.ImageViewer;
import client.window.table.TablePanel;

@SuppressWarnings("serial")
public class IndexerWindow extends JFrame{

	private JMenuBar menuBar;
	private JPanel mainPanel;
	private ImageViewer imgViewer;
	private ButtonPanel buttonPanel;
	private TablePanel tablePanel;
	private FormPanel formPanel;
	private HelpPanel helpPanel;
	
	private JSplitPane tableAndHelpPane;
	private JSplitPane ivAndFormPane;
	
	
	
	
	public IndexerWindow(){
		super("Indexer");
		
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		createComponents();
		
		addListeners();
		
		this.setJMenuBar(menuBar);
		this.add(mainPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
	
	private void addListeners(){
		BatchState.addListener(helpPanel);
		BatchState.addListener(imgViewer);
		BatchState.addListener(tablePanel);
	}
	
	private void createComponents(){
		//create main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//create menu bar
		createMenu();
		
		//create imageviewer
		imgViewer = new ImageViewer();
		
		//create button bar
		buttonPanel = new ButtonPanel(imgViewer, this);
		
		//create table/form entry
		//TODO left off here. continue with building these classes
		tablePanel = new TablePanel();
		JScrollPane tableScroll = new JScrollPane(tablePanel);
		
		formPanel = new FormPanel();
		
		JTabbedPane tableFormPane = new JTabbedPane();
		tableFormPane.addTab("Table Entry", tableScroll);
		tableFormPane.addTab("Form Entry", formPanel);
		
		//create help text
		helpPanel = new HelpPanel();
		JScrollPane helpScroll = new JScrollPane(helpPanel);
		helpScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		helpScroll.setPreferredSize(new Dimension(500, 500));
		
		//combine table and help
		tableAndHelpPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableFormPane, helpScroll);
		tableAndHelpPane.setResizeWeight(.5);
		
		//combine imageviewer and form entry
		ivAndFormPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imgViewer, tableAndHelpPane);
		ivAndFormPane.setResizeWeight(.5);
		
		//put it all together
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		mainPanel.add(ivAndFormPane, BorderLayout.CENTER);
	}
	
	private void createMenu(){
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		
		JMenuItem downloadBatchMI = new JMenuItem("Download Batch");
		downloadBatchMI.addActionListener(menuActionListener);
		JMenuItem logoutMI = new JMenuItem("Logout");
		logoutMI.addActionListener(menuActionListener);
		JMenuItem exitMI = new JMenuItem("Exit");
		exitMI.addActionListener(menuActionListener);
		
		menu.add(downloadBatchMI);
		menu.add(logoutMI);
		menu.add(exitMI);
		
		menuBar.add(menu);
	}
	
	/**
	 * Disable "Download batch menu item"
	 */
	public void disableDBMI(){
		menuBar.getMenu(0).getMenuComponent(0).setEnabled(false);
	}
	
	/**
	 * Enable "Download batch menu item"
	 */
	public void enableDBMI(){
		menuBar.getMenu(0).getMenuComponent(0).setEnabled(true);
	}
	
	private void downloadBatch(){
		DownloadBatchWindow dbWin = new DownloadBatchWindow(this);
		if(BatchState.getProject() != null){
			disableDBMI();
			buttonPanel.enableButtons();
			imgViewer.loadBatch();
			tablePanel.setupTable();
			
		}
		dbWin.dispose();
	}
	
	public void submitBatch(){
		BatchState.clearCells();
		buttonPanel.disableButtons();
		enableDBMI();
		imgViewer.reset();
		imgViewer.repaint();
		tablePanel.removeAll();
		tablePanel.repaint();
		try {
			LoginState.updateUser(ClientFacade.validateUser(LoginState.getUserOn().getUsername(), LoginState.getUserOn().getPassword()));
		} catch (ClientException e1) {
			e1.printStackTrace();
			return;
		}

	}
	
	public boolean saveState(){
		User user = LoginState.getUserOn();
		String userFileLoc = "./Data/UserInfo/" + user.getUsername() + ".ud";
		File userData = new File(userFileLoc);
		
		if(!userData.exists()){
			try {
				userData.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		
		ObjectOutputStream output = null;
		BufferedOutputStream buffer = null;
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(userData);
			buffer = new BufferedOutputStream(fop);
			output = new ObjectOutputStream (buffer);
			
			BatchState bs = new BatchState();
			output.writeObject(bs);
			output.writeDouble(imgViewer.getZoomLevel());
			//output.writeInt(imgX);
			//output.writeInt(imgY);
			output.writeBoolean(imgViewer.isHighlights());
			output.writeBoolean(imgViewer.isInverted());
			//TODO make this save info about the position of the window
			output.writeInt(this.getWidth());
			output.writeInt(this.getHeight());
			output.writeObject(this.getLocationOnScreen());
			output.writeObject(tableAndHelpPane.getSize());
			output.writeObject(ivAndFormPane.getSize());
			
			if(output != null)
				output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean loadState(){
		User user = LoginState.getUserOn();
		String userFileLoc = "./Data/UserInfo/" + user.getUsername() + ".ud";
		File userData = new File(userFileLoc);
		if(!userData.exists()){
			BatchState.clearCells();
			return true;
		}
		ObjectInputStream input = null;
		BufferedInputStream buffer = null;
		FileInputStream fip = null;
		try {
			fip = new FileInputStream(userData);
			buffer = new BufferedInputStream(fip);
			input = new ObjectInputStream (buffer);
			
			@SuppressWarnings("unused")
			BatchState bs = (BatchState)input.readObject();
			imgViewer.setZoomLevel(input.readDouble());
			//imgX = input.readInt();
			//imgY = input.readInt();
			imgViewer.setHighlights(input.readBoolean());
			imgViewer.setInverted(input.readBoolean());
			//TODO test the pane sizing better
			this.setSize(input.readInt(), input.readInt());
			this.setLocation((Point)input.readObject());
			tableAndHelpPane.setSize((Dimension)input.readObject());
			ivAndFormPane.setSize((Dimension)input.readObject());
			
			
			if(BatchState.getBatch() != null){
				imgViewer.loadBatch();
				disableDBMI();
				buttonPanel.enableButtons();
				tablePanel.setupTable();
			}
			
			if(imgViewer.isInverted())
				imgViewer.invertImage();
			
			if(input != null)
				input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	
	private ActionListener menuActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == menuBar.getMenu(0).getItem(0)){
				//Download batch stuff here
				downloadBatch();
			}
			if(e.getSource() == menuBar.getMenu(0).getItem(1)){
				//logout
				LoginState.logUserOut();
			}
			if(e.getSource() == menuBar.getMenu(0).getItem(2)){

				//TODO add saving the state here
				System.exit(EXIT_ON_CLOSE);
			}
		}
	};
}
