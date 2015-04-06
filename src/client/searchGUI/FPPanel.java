package client.searchGUI;

import java.util.List;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import client.ClientException;
import client.backend.facade.ClientFacade;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

@SuppressWarnings("serial")
public class FPPanel extends JPanel{

	
	private DefaultTreeModel fpTreeModel;
	private JTree fpTree;
	private boolean removeState;
	private FPPanel theOtherGuy;
	
	public FPPanel(String fieldOrPrj, boolean remove){
		removeState = remove;
		
		fpTreeModel = new DefaultTreeModel(createDefaultTree(fieldOrPrj));

		fpTree = new JTree(fpTreeModel);
		fpTree.setOpaque(true);
		fpTree.setBackground(Color.white);
		fpTree.setPreferredSize(new Dimension(250, 600));
		fpTree.setEditable(false);
		fpTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		fpTree.setShowsRootHandles(false);
		fpTree.addTreeSelectionListener(treeSelListener);
		
		JScrollPane fpScrollPane = new JScrollPane(fpTree);
		fpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		fpScrollPane.setPreferredSize(new Dimension(250, 300));
		this.add(fpScrollPane);
	}
	
	public void addTheOtherGuy(FPPanel otherGuy){
		theOtherGuy = otherGuy;
	}
	
	private DefaultMutableTreeNode createDefaultTree(String fORp) {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(fORp);
		
		return root;
	}
	
	/**
	 * 
	 * @param projects
	 */
	public void addAllProjects(List<Project> projects, User user){
		for(int i = 0; i < projects.size(); i++){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)fpTreeModel.getRoot();
			fpTreeModel.insertNodeInto(new DefaultMutableTreeNode(projects.get(i)), node, node.getChildCount());
			
			List<Field> fields = null;
			try {
				fields = ClientFacade.getFields(user.getUsername(), user.getPassword(), projects.get(i).getKey());
			} catch (ClientException e) {
				e.printStackTrace();
			}
			
			for(int f = 0; f < fields.size(); f++){
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
				fpTreeModel.insertNodeInto(new DefaultMutableTreeNode(fields.get(f)), child, child.getChildCount());
			}
		}
		
	}
	
	public void addField(Field field, Project project){
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)fpTreeModel.getRoot();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(field);
		DefaultMutableTreeNode prjNode = new DefaultMutableTreeNode(project);
		for(int i = 0; i < fpTreeModel.getChildCount(root); i++){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)root.getChildAt(i);
			if(project.equals(parent.getUserObject())){
				for(int p = 0; p < fpTreeModel.getChildCount(parent); p++){
					DefaultMutableTreeNode comp = (DefaultMutableTreeNode)fpTreeModel.getChild(parent, p);
					if(comp.getUserObject().equals(field))
						return;
				}
				fpTreeModel.insertNodeInto(node, parent, parent.getChildCount());
				return;
			}
		}
		fpTreeModel.insertNodeInto(prjNode, root, root.getChildCount());
		fpTreeModel.insertNodeInto(node, prjNode, prjNode.getChildCount());
	}
	
	public String getSearchFields(){
		StringBuilder sb = new StringBuilder();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)fpTreeModel.getRoot();
		for(int p = 0; p < root.getChildCount(); p++){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)root.getChildAt(p);
			for(int i = 0; i < parent.getChildCount(); i++){
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getChildAt(i);
				Field searchMe = (Field)child.getUserObject();
				sb.append(searchMe.getId() + ",");
			}
		}
		if(sb.length() > 1)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	private TreeSelectionListener treeSelListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fpTree.getLastSelectedPathComponent();
			if (node != null) {
				if (node.isLeaf()) {
					if(removeState){
						if(node != fpTreeModel.getRoot()){
							DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
							fpTreeModel.removeNodeFromParent(node);
							if(parent.getChildCount() < 1 && parent != fpTreeModel.getRoot())
								fpTreeModel.removeNodeFromParent(parent);
						}
					}else{
						DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
						theOtherGuy.addField((Field)node.getUserObject(), (Project)parent.getUserObject());
					}
				}
			}
		}
	};
}
