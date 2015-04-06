package client.window;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import shared.model.User;
import client.ClientException;
import client.backend.LoginState;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class LoginWindow extends JFrame{

	private JPanel loginPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton exitButton;
	
	
	
	
	public LoginWindow(){
		super("Login to Indexer");
		
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		createComponents();

		this.add(loginPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
	
	private void createComponents(){
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		
		JLabel usernameLabel = new JLabel("Username:");
		Font defaultFont = usernameLabel.getFont();
		Font font = new Font(defaultFont.getName(), defaultFont.getStyle(), 14);
		usernameLabel.setFont(font);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(font);
		//TODO this is not right appearance
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(275, 15));

		JPanel passPanel = new JPanel();
		passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(275, 15));
		
		
		userPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		userPanel.add(usernameLabel);
		userPanel.add(Box.createRigidArea(new Dimension(8, 0)));
		userPanel.add(usernameField);
		userPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		
		passPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		passPanel.add(passwordLabel);
		passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		passPanel.add(passwordField);
		passPanel.add(Box.createRigidArea(new Dimension(5, 5)));

		
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(actionListener);
		
		exitButton = new JButton("Exit");
		exitButton.addActionListener(actionListener);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(loginButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(userPanel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		loginPanel.add(passPanel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		loginPanel.add(buttonPanel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	}
	
	private String getUsername(){
		return usernameField.getText();
	}
	
	private String getPassword(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < passwordField.getPassword().length; i++){
			sb.append(passwordField.getPassword()[i]);
		}
		return sb.toString();
	}
	
	private void createErrorPane(String msg, String title){
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public void clearPassword(){
		passwordField.setText("");
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton) {
				User userOn = null;
				try {
					userOn = ClientFacade.validateUser(getUsername(), getPassword());
				} catch (ClientException er) {
					usernameField.setText("");
					passwordField.setText("");
					createErrorPane("Could not connect to server!", "Login Failed");
					return;
				}
				
				if(userOn == null){
					usernameField.setText("");
					passwordField.setText("");
					createErrorPane("Invalid username and/or password", "Login Failed");
					return;
				}
				LoginState.logUserIn(userOn);
				LoginWindow.this.setVisible(false);
			}
			if(e.getSource() == exitButton){
				System.exit(EXIT_ON_CLOSE);
			}
		}
	};
	
	
	
	
}
