package client.searchGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import shared.model.User;
import client.ClientException;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class SearchGUI extends JFrame{
		
	private FPPanel pp;
	private FPPanel fp;
	private SearchPanel sp;
	private User userOn;
	
	public SearchGUI(String title) {
        super(title);
        
        createComponents();
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		this.setResizable(false);
    }
	
	private void createComponents(){
		
		pp = new FPPanel("Projects", false);
		fp = new FPPanel("Fields To Search", true);
		pp.addTheOtherGuy(fp);
		fp.addTheOtherGuy(pp);
		sp = new SearchPanel(fp);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pp, fp);
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(sp, BorderLayout.NORTH);
        rootPanel.add(splitPane, BorderLayout.CENTER);

        this.add(rootPanel);
	}
	
	
	private void login(){
		LoginWindow loginwindow = new LoginWindow(this, true);
		loginwindow.setLocationRelativeTo(this);
		loginwindow.setVisible(true);
		
		if (loginwindow.getStatus()) {
			userOn = loginwindow.getUser();
			sp.giveUser(userOn);
			try {
				pp.addAllProjects(ClientFacade.getAllProjects(userOn.getUsername(), userOn.getPassword()), userOn);
			} catch (ClientException e) {
				e.printStackTrace();
			}
			this.setVisible(true);
		}
	}
	public static void main(String[] args){
		
		
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				SearchGUI sgui = new SearchGUI("Search GUI");
				sgui.login();
			}
			
		});
	}
}
