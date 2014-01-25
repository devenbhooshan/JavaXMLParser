import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")

public class XMLTree extends JPanel implements ActionListener,MouseListener{
	int printPoints[];
	int SelectedNode=0;
	int tree_width;
	int node_width=50;
	int node_height=50;
	static boolean isPrinted=false;
	static Tree<String> xml_parsed_tree;
	static Node<String> root ;
	static boolean isTreeParsed=false;
	static boolean isTreePrinted=false;
	JButton jb=new JButton("Generate");
	
	JTextArea text=new  JTextArea(6,8);
	JButton delete=new JButton("Delete Node");
	
	JButton Smart_delete=new JButton("Smart Delete");
	JButton insert_node=new JButton("Insert Node");
	JButton dumb_xml=new JButton("Dump XML");
	JButton move=new JButton("Move");
	JButton edit=new JButton("Edit");
	JPanel bottom_area=new JPanel();
	JPanel print_area=new JPanel();
	JPanel generate_print_button_area=new JPanel();
	JScrollPane js=new JScrollPane(print_area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	Graphics gg;
	int i=0;
	int array_bounds[][];
	public XMLTree(){
		setPreferredSize(new Dimension(1320, 550));
		this.setLayout(null);
		gg=this.getGraphics();
		text.setMargin(new Insets(5,5,5,5));
		text.setEditable(false);
		JScrollPane data_display=new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		generate_print_button_area.add(jb);
		bottom_area.add(delete);
		bottom_area.add(Smart_delete);
		bottom_area.add(insert_node);
		bottom_area.add(move);
		bottom_area.add(edit);
		bottom_area.add(dumb_xml);
		data_display.setBounds(100,460,1000,80);
		generate_print_button_area.setBounds(10, 10, 1300, 40);
		js.setBounds(0, 50, 1200, 400);
		bottom_area.setBounds(1210,50,100,400);
		this.add(data_display);
		this.add(generate_print_button_area);
		this.add(js);
		this.add(bottom_area);
		insert_node.addActionListener(this);
		delete.addActionListener(this);
		jb.addActionListener(this);
		Smart_delete.addActionListener(this);
		move.addActionListener(this);
		dumb_xml.addActionListener(this);
		js.addMouseListener(this);
		edit.addActionListener(this);
	}
	
	
	int counter_for_nodes=0;
	public void print_tree_Wrapper(){
		if(isTreeParsed && root!=null){
			counter_for_nodes=0;
			tree_width=0;
			array_bounds=new int[10000][3];
			get_tree_width(root);
			System.out.println("Width:"+tree_width);
			node_width=((1200-15*tree_width)/tree_width);
			printPoints=new int[tree_width];
			printPoints=getprintPoints(tree_width);
			Graphics gg=js.getGraphics();
			gg.setColor(Color.blue);
			gg.fillRect(printPoints[tree_width/2]-node_width/2, 10, node_width, node_height);
			array_bounds[counter_for_nodes][0]=printPoints[tree_width/2]-node_width/2;
			array_bounds[counter_for_nodes][1]=10;
			array_bounds[counter_for_nodes][2]=root.root_id;
			counter_for_nodes++;
			root.xCord=printPoints[tree_width/2];
			root.yCord=10;
			this.gg=gg;
			gg.setColor(Color.RED);
			gg.drawString(Integer.toString((root.root_id)), printPoints[tree_width/2]-node_width/2, 10);
			gg.setColor(Color.BLUE);
			drawTree(gg, printPoints[0], root,10);
		}
	
		
	}
	
	private void drawTree(Graphics g, int j, Node<String> root2,int y) {
		if(root2==null) return ;
		
		int noOfchilder=0;
		Node <String> papa=root2.getParent();
		ArrayList<Node<String>> all=new ArrayList<Node<String>>();
		if(papa==null){ noOfchilder=root2.getChildren().size(); 
		all.addAll(all.size(), root2.getChildren());
		}
		else {for(Node<String> child: papa.getChildren()){
			noOfchilder+=child.getChildren().size();
			all.addAll(all.size(), child.getChildren());
		}
		}
		int startI=(tree_width-noOfchilder)/2;
		int temp=0;
		Node <String> onechild=null;
		Iterator<Node<String>> it=all.iterator();
		while(it.hasNext()){
			Node<String> child=it.next();
			if(child.getChildren().size()!=0 && temp==0){onechild=child; temp=1;}
			g.fillRect(printPoints[startI], y+node_height+25, node_width, node_height);
			array_bounds[counter_for_nodes][0]=printPoints[startI];
			array_bounds[counter_for_nodes][1]=y+node_height+25;
			array_bounds[counter_for_nodes][2]=child.root_id;
			child.xCord=array_bounds[counter_for_nodes][0]+node_width/2;
			child.yCord=array_bounds[counter_for_nodes][1];
			counter_for_nodes++;
			g.setColor(Color.RED);
			g.drawString(Integer.toString((child.root_id)), printPoints[startI], y+node_height+25);
			g.setColor(Color.WHITE);
			g.drawLine(child.getParent().xCord,child.getParent().yCord ,child.xCord, child.yCord);
			g.setColor(Color.BLUE);
			startI++;
		}
		drawTree(g, j, onechild,y+node_height+25);
		
	}
	private int[] getprintPoints(int tree_width) {
		int i=1;
		int points[]=new int[tree_width];
		points[0]=5;
		while(i<tree_width){
			points[i]=points[i-1]+node_width+15;
			i++;
		}
		return points;
	}
	private void get_tree_width(Node<String> top) {
		if(top.getChildren()==null) { return;}
		boolean hasChild=false;
		for(Node<String> child:top.getChildren()){
			hasChild=true;
			get_tree_width(child);
		}
		if(!hasChild) tree_width++;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource()==jb ){
			if(!isTreePrinted){
				isTreeParsed=true;
				if(gg!=null) super.paintComponent(gg);
				print_tree_Wrapper();
			}else {
				isTreePrinted=false;
			}
		}
		else if(e.getSource()==delete){
			if(SelectedNode!=0){
				System.out.println("Node #"+SelectedNode+" would be deleted");
				DeleteNode(SelectedNode,root);
				super.paintComponent(gg);
				print_tree_Wrapper();

			}
		}
		else if(e.getSource()==Smart_delete){
			if(SelectedNode!=0){
				System.out.println("Smartly Deleting Node:"+SelectedNode);
				smartDelete(SelectedNode,root);
				super.paintComponent(gg);
				print_tree_Wrapper();
		
			}else {
					text.setText("Error: Cannot delete the Root Node");
			}
			
		}
		else if(e.getSource()==edit){
			if(SelectedNode!=0){
			Node<String> node=SearchById(SelectedNode);
			
			//JTextField nodeAt = new JTextField();
			JTextField tag_Name = new JTextField();
			tag_Name.setText(node.getData());
			JTextField data = new JTextField();
			data.setText(node.data_part);
			tag_Name.setEditable(false);
			JTextField id = new JTextField();
			id.setText(node.attrib);
			final JComponent[] inputs = new JComponent[] {
					
					new JLabel("Tag Name"),
					tag_Name,new JLabel("Data"),data,new JLabel("Id"),id
			};
			JOptionPane.showMessageDialog(null, inputs, "Enter Details", JOptionPane.PLAIN_MESSAGE);
				System.out.println("You entered " +
					 tag_Name.getText());
				
				node.attrib=id.getText();
				node.data_part=data.getText();
				node.root_id=SelectedNode;
				
				//insertNode(from_Id,root,added);
				super.paintComponent(gg);
				print_tree_Wrapper();
			}
		}
		else if(e.getSource()==insert_node){
			
			JTextField nodeAt = new JTextField();
			JTextField tag_Name = new JTextField();
			JTextField data = new JTextField();
			JTextField id = new JTextField();
			final JComponent[] inputs = new JComponent[] {
					new JLabel("At Node #"),
					nodeAt,
					new JLabel("Tag Name"),
					tag_Name,new JLabel("Data"),data,new JLabel("Id"),id
			};
			JOptionPane.showMessageDialog(null, inputs, "Enter Details", JOptionPane.PLAIN_MESSAGE);
				System.out.println("You entered " +
					nodeAt.getText() + ", " +tag_Name.getText());
			
				int from_Id=Integer.parseInt(nodeAt.getText());
				String tag_name=(tag_Name.getText());
				Node<String> added=new Node<String>(tag_name);
				added.attrib=id.getText();
				added.data_part=data.getText();
				added.root_id=Tree.counter++;
				insertNode(from_Id,root,added);
				super.paintComponent(gg);
				print_tree_Wrapper();
			
		}else if(e.getSource()==move){
			
			JTextField from = new JTextField();
			JTextField to = new JTextField();
			
			final JComponent[] inputs = new JComponent[] {
					new JLabel("From"),
					from,
					new JLabel("To"),
					to,
					
			};
			JOptionPane.showMessageDialog(null, inputs, "Enter Details", JOptionPane.PLAIN_MESSAGE);
				System.out.println("You entered " +
					from.getText() + ", " +to.getText());
            int from_Id=Integer.parseInt(from.getText());
            int to_Id=Integer.parseInt(to.getText());
            Move(from_Id,to_Id,root);
            //Refresh_tree();
            super.paintComponent(gg);
			print_tree_Wrapper();
		}else if(e.getSource()==dumb_xml){
			dumbXMLFile(root);
			
		}
		
		
		}
	
	
	private void dumbXMLFile(Node<String> root2) {

	
			JFileChooser jf=new JFileChooser();
			
			
			File file = new File("/dumb.xml");
			jf.setCurrentDirectory(file);
			int actionDialog = jf.showSaveDialog(this);

			if ( actionDialog == JFileChooser.APPROVE_OPTION )
			{

				File fileName = new File( jf.getSelectedFile( ) + ".xml" );
				if(fileName == null)
					return;
				if(fileName.exists())
				{
					actionDialog = JOptionPane.showConfirmDialog(this,
							"Replace existing file?");
					if (actionDialog == JOptionPane.NO_OPTION)
						return;
				}
				BufferedWriter write;
				try {
					write = new BufferedWriter(new FileWriter(new File(jf.getSelectedFile( ) + ".xml")));
					createXMLFile( root ,write);
					
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	}
	private void createXMLFile(Node<String> root, BufferedWriter write) {
		Node <String> childputtar;
		try {
			if(root!=null) {
				if(root.getData()=="XML"){ 
					write.append("<?xml version=\"1.0\"?>\n ");
				}else {
				String data="";
				if(root.data_part!=null){
					data=root.data_part;
				}
				if(root.attrib!=null){
					write.append("< "+root.getData()+ " "+root.attrib+" >\n"+data+"\n");
				}else 
					write.append("< "+root.getData()+ " >\n"+data+"\n");
				}
			Stack < Node<String>> st=new Stack<Node<String>>();
			for(Node<String> child: root.getChildren()){
				//write.append("< "+child.getData()+ " >\n");
				st.push(child);
			}
			while(!st.isEmpty()){
				childputtar=st.pop();
				createXMLFile(childputtar, write);
				write.append("\t< "+childputtar+ " \\>\n");
			}
			
			}else {
				
			}
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void Move(int from_Id, int to_Id, Node<String> top) {
		ArrayList<Node<String>> sd=xml_parsed_tree.getPreOrderTraversal();
		
		
		Iterator<Node<String>> it=sd.iterator();
		Node<String> from =null;
		Node<String> to =null;
		while(it.hasNext()){
			Node <String > child=it.next();
			if(child.root_id==from_Id){
				from=child;
			}
			if(child.root_id==to_Id){
				to=child;
			}
		}
		
		int i=0;
		if(from!=null && to != null && from.getParent()!=null && from.getParent().getChildren()!=null ){
		for(Node<String> ch: from.getParent().getChildren()){
			if(ch.root_id==from_Id){
				from.getParent().removeChildAt(i);
				to.addChild(from);
				break;
			}
			i++;
		}
		}else {
			text.setText("Cannot Be moved");
			
		}
		
		
	}
	private void insertNode(int selectedNode2, Node<String> root2, Node<String> added) {
		ArrayList<Node<String>> preorder=xml_parsed_tree.getPreOrderTraversal();
		Iterator<Node<String>> it=preorder.iterator();
		while(it.hasNext()){
			Node<String> noder=it.next();
			if(noder.root_id==selectedNode2){
				noder.addChild(added);
				break;
			}
			
		}
		
		
	}
	private void smartDelete(int Node_Id, Node<String> top) {
		if(top==null) {System.out.println("Null: DeleteNode"); return ; }
		if(top.root_id==Node_Id) {
			System.out.println("Should Not Happen");
			try {
				throw new Exception("Something Went Wrong");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			}
		Node<String> node=top;
		Stack<Node> children_tree=new Stack<Node>();
		int i=0;
		for (Node<String> child : node.getChildren()) {
			if(child.root_id==Node_Id){
				for(Node<String> childchild: child.getChildren()){
					top.addChild(childchild);
				}
				top.removeChildAt(i);
				return;
			}
			children_tree.add(child);
			i++;
		}
		while(!children_tree.isEmpty()){
			smartDelete(Node_Id,children_tree.pop());
		}
	}
	
	
	private void DeleteNode(int Node_Id, Node<String> top) {
		
		if(top==null) {System.out.println("Null: DeleteNode"); return ; }
		if(top.root_id==Node_Id) {  root=null; }
		Node<String> node=top;
		Stack<Node> children_tree=new Stack<Node>();
		int i=0;
		for (Node<String> child : node.getChildren()) {
			if(child.root_id==Node_Id){
				top.removeChildAt(i);
				return;
			}
			children_tree.add(child);
			i++;
		}
		while(!children_tree.isEmpty()){
			DeleteNode(Node_Id,children_tree.pop());
		}

	}
	
private Node<String> SearchById(int Node_Id){
	ArrayList<Node<String>> sd=xml_parsed_tree.getPreOrderTraversal();
	Iterator<Node<String>> it=sd.iterator();
	while(it.hasNext()){
		Node <String > child=it.next();
		if(child.root_id==Node_Id) return child; 
	}
	return null;
}

@Override
public void mouseClicked(MouseEvent e) {
	int x=e.getX();
	int y=e.getY();
	int i=0;
	
	while(i<counter_for_nodes){

		Rectangle r1=new Rectangle(array_bounds[i][0], array_bounds[i][1], node_width, node_height);
		if(r1.contains(x, y)){
			int Node_Id=array_bounds[i][2];
			SelectedNode=Node_Id;
			Node<String> node=SearchById(Node_Id);
			if(node!=null){
				String data="";
				if(node.getData()!=null){
					
				data+="Node Name: "+(node.getData())+"\n";
				}
				if(node.data_part!=null){
					data+="Data Part: "+(node.data_part)+"\n";
				}
				if(node.attrib!=null){
					data+="Attributes: "+node.attrib+"\n";
				}
				text.setText(data);
			}
			break;
		}
		i++;
	}
}

@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
	
	
	

}
