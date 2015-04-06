package client.searchGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import shared.communication.SearchTuple;

@SuppressWarnings("serial")
public class ResultViewer extends JFrame {

	private JEditorPane imageViewer;
	private DefaultTreeModel fpTreeModel;
	private JTree fpTree;
	
	private String urlPrefix;
	
	
	
	
	public ResultViewer(String urlPrefix){
		
		super();
		
		this.urlPrefix = urlPrefix;
		
		imageViewer = new JEditorPane();
		imageViewer.setOpaque(true);
		imageViewer.setBackground(Color.white);
		imageViewer.setPreferredSize(new Dimension(500, 600));
		imageViewer.setEditable(false);
        
		JScrollPane ivScrollPane = new JScrollPane(imageViewer);
		ivScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ivScrollPane.setPreferredSize(new Dimension(1000, 800));
		
		
		
		fpTreeModel = new DefaultTreeModel(createDefaultTree());

		fpTree = new JTree(fpTreeModel);
		fpTree.setOpaque(true);
		fpTree.setBackground(Color.white);
		fpTree.setPreferredSize(new Dimension(250, 600));
		fpTree.setEditable(false);
		fpTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		fpTree.setShowsRootHandles(false);
		fpTree.addTreeSelectionListener(treeSelListener);
		
		JScrollPane rvScrollPane = new JScrollPane(fpTree);
		rvScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rvScrollPane.setPreferredSize(new Dimension(250, 300));
		

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(rvScrollPane, BorderLayout.WEST);
        rootPanel.add(ivScrollPane, BorderLayout.EAST);

        this.add(rootPanel);
        this.setTitle("Search Result");
        this.pack();
        this.setVisible(false);
        this.setLocationRelativeTo(null);
        this.requestFocus();
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	private DefaultMutableTreeNode createDefaultTree() {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Result Images");
		
		return root;
	}
	
	public void addResult(List<SearchTuple> result){
		if(result != null){
			for(int i = 0; i < result.size(); i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)fpTreeModel.getRoot();
				boolean addme = true;
				for(int r = 0; r < node.getChildCount(); r++){
					DefaultMutableTreeNode check = (DefaultMutableTreeNode)node.getChildAt(r);
					SearchTuple chktup = (SearchTuple)check.getUserObject();
					if(chktup.getImgURL().equals(result.get(i).getImgURL()))
						addme = false;
				}
				if(addme)
					fpTreeModel.insertNodeInto(new DefaultMutableTreeNode(result.get(i)), node, node.getChildCount());
			}
		}
	}
	
	public void clear(){
		fpTreeModel = new DefaultTreeModel(createDefaultTree());
	}
	
	public void updateImage(String url){
		imageViewer.setContentType("text/html");
		imageViewer.setText("<html><img src="+urlPrefix+"/"+url+"></img></html>");
	}
	
	private TreeSelectionListener treeSelListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fpTree.getLastSelectedPathComponent();
			if (node != null) {
				if (node.isLeaf()) {
					if(node != fpTreeModel.getRoot()){
						SearchTuple st = (SearchTuple)node.getUserObject();
						updateImage(st.getImgURL());
					}else{
						imageViewer.setText("No Results!");
					}
				}
			}
		}
	};
}
