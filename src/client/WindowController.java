package client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import shared.model.User;
import client.backend.*;
import client.backend.facade.ClientFacade;
import client.window.IndexerWindow;
import client.window.LoginWindow;

public class WindowController implements Runnable, LoginStateListener{

	private LoginWindow loginWin;
	private IndexerWindow indexWin;	
	
	
	public WindowController(String host, String port){
		//Initialize things
		loginWin = new LoginWindow();
		indexWin = new IndexerWindow();
		ClientFacade.initialize(host, port);
		LoginState.initialize();
		LoginState.addListener(this);
	}
	
	private void viewLoginWindow(){
		loginWin.setVisible(true);
		indexWin.setVisible(false);
		loginWin.requestFocus();
	}
	
	private void viewIndexerWindow(){
		loginWin.setVisible(false);
		indexWin.setVisible(true);
		indexWin.requestFocus();
	}
	
	private void login(){
		viewLoginWindow();
	}
	
	@Override
	public void userLoggedIn(User user) {
		
		JOptionPane.showMessageDialog(loginWin,
			    "Welcome, " + user.getFirstName() + " " + user.getLastName() +".\n"
			    + "You have indexed " + user.getRecordsIndexed() + " records.",
			    "Welcome to Indexer",
			    JOptionPane.PLAIN_MESSAGE);
		
		indexWin.loadState();
		
		launchIndexer();
	}

	@Override
	public void userLoggedOut(User user) {
		indexWin.saveState();
		loginWin.clearPassword();
		login();
	}
	
	private void launchIndexer(){
		viewIndexerWindow();
	}
	
	public static void main(String[] args){
		EventQueue.invokeLater(new WindowController(args[0],args[1]));
	}

	@Override
	public void run() {
		this.login();
	}
}
