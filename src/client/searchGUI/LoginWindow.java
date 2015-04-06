package client.searchGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientException;
import client.backend.facade.ClientFacade;
import shared.model.User;

@SuppressWarnings("serial")
public class LoginWindow extends JDialog{
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField hostField;
	private JTextField portField;
	private JButton loginButton;
	private JButton quitButton;
	
	private boolean status;
	private User userOn;
	
	public LoginWindow(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.setTitle("Login");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		
		JLabel usernameLabel = new JLabel("Username:");
		Font defaultFont = usernameLabel.getFont();
		Font font = new Font(defaultFont.getName(), defaultFont.getStyle(), 14);
		usernameLabel.setFont(font);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(font);
		
		JLabel hostLabel = new JLabel("Host:");
		hostLabel.setFont(font);
		
		JLabel portLabel = new JLabel("Port:");
		portLabel.setFont(font);
		
		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(200, 30));
		
		passwordField = new JTextField();
		passwordField.setPreferredSize(new Dimension(200, 30));
		
		hostField = new JTextField();
		hostField.setPreferredSize(new Dimension(200, 30));
		
		portField = new JTextField();
		portField.setPreferredSize(new Dimension(200, 30));
		
		
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(usernameLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 2)));
		loginPanel.add(usernameField);
		
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(passwordLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 2)));
		loginPanel.add(passwordField);
		
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(hostLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 2)));
		loginPanel.add(hostField);
		
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(portLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 2)));
		loginPanel.add(portField);
		
		
		// Button panel
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(actionListener);
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(actionListener);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(loginButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		buttonPanel.add(quitButton);
		
		// Root panel
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		
		rootPanel.add(loginPanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(buttonPanel);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(rootPanel);
		
		this.pack();
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public User getUser() {
		return userOn;
	}
	
	public String getHost() {
		return hostField.getText();
	}
	
	public String getPort() {
		return portField.getText();
	}
	
	private String getUsername(){
		return usernameField.getText();
	}
	
	private String getPassword(){
		return passwordField.getText();
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton) {
				ClientFacade.initialize(getHost(), getPort());
				
				try {
					userOn = ClientFacade.validateUser(getUsername(), getPassword());
				} catch (ClientException er) {
					usernameField.setText("");
					passwordField.setText("");
					hostField.setText("");
					portField.setText("");
					System.err.println("Bad Host or Port!");
					return;
				}
				
				if(userOn == null){
					usernameField.setText("");
					passwordField.setText("");
					System.err.println("Bad Username or Password!");
					return;
				}
				status = true;
				LoginWindow.this.setVisible(false);
			}
			if(e.getSource() == quitButton){
				System.exit(EXIT_ON_CLOSE);
			}
		}
	};

}