package client.searchGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import shared.communication.SearchTuple;
import shared.model.User;
import client.ClientException;
import client.backend.facade.ClientFacade;

@SuppressWarnings("serial")
public class SearchPanel extends JPanel{

	private JTextField searchField;
	private JButton searchButton;
	private User userOn;
	private FPPanel fp;
	private ResultViewer rv;
	
	
	
	
	public SearchPanel(FPPanel fieldPanel){
		super();
		
		this.fp = fieldPanel;
		
		JLabel searchLabel = new JLabel("Search Key:");
		Font defaultFont = searchLabel.getFont();
		Font font = new Font(defaultFont.getName(), defaultFont.getStyle(), 14);
		searchLabel.setFont(font);
		
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(300, 30));
		searchField.setToolTipText("Enter keywords to search for seperated by commas");
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(actionListener);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(searchLabel);
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(searchField);		
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(searchButton);
		this.add(Box.createRigidArea(new Dimension(5, 0)));
	}
	
	public void giveUser(User user){
		this.userOn = user;
	}
	
	public void addResultViewer(ResultViewer rv){
		this.rv = rv;
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == searchButton) {
				List<SearchTuple> result = null;
				try {
					result = ClientFacade.search(userOn.getUsername(), userOn.getPassword(), fp.getSearchFields(), searchField.getText());
				} catch (ClientException er) {
					er.printStackTrace();
					return;
				}
				rv = new ResultViewer(ClientFacade.getURLPrefix());
				rv.addResult(result);
				rv.setVisible(true);
			}
		}
	};
}
