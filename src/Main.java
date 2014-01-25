import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Main extends JPanel implements ActionListener,MouseListener {
    JFileChooser fileChooser;  // For choosing the file
    JButton openB;             // Button for opening the file chooser
    JTextArea logger;		   // TextArea for file 
    JTextArea tree_logger;     // Textarea for tree
    static  JPanel printree;   // Panel for Tree
    
    /**
     * 
     * Constructor for the Main  
     * 
     */
    
    public Main() {
        super(new BorderLayout());
        logger = new JTextArea(3,20);
        tree_logger=new JTextArea(30,100);
        tree_logger.setEditable(false);
        logger.setMargin(new Insets(5,5,5,5));
        logger.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logger);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openB = new JButton("Upload A file");
        openB.addActionListener(this);
        JPanel buttonPanel = new JPanel(); 
        printree=new JPanel();
        JScrollPane tree_logg_scroller= new JScrollPane(tree_logger);
        printree.setPreferredSize(new Dimension(1200, 500));
        printree.add(tree_logg_scroller);
        buttonPanel.add(openB);
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add((new XMLTree()), BorderLayout.SOUTH);
    }
 
    public void actionPerformed(ActionEvent e) {
 
        if (e.getSource() == openB) {
            int returnVal = fileChooser.showOpenDialog(Main.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String arr=file.getName();
                if(arr.substring(arr.indexOf('.')+1).compareTo("xml")==0 || arr.substring(arr.indexOf('.')+1).compareTo("XML")==0){
                	logger.append("Uploading File: " + arr + "." + "\n");
                	try {
						Wrapper_For_Parser(file.getAbsolutePath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                }else{
                	logger.append("Only .xml file supported \n");
                }
            } else {
                logger.append("Cancelled." + "\n");
            }
            logger.setCaretPosition(logger.getDocument().getLength());
        } 
    }
 
    /**
     * 
     * @param absolutePath
     * @throws IOException
     * Calls the  parser and generates tree
     */
    
    private void Wrapper_For_Parser(String absolutePath) throws IOException {
    	Parser parser=new Parser();
    	parser.parser_Wrapper(absolutePath);
    	if(parser.logger.length()!=0){
    		logger.append(parser.logger);
    	}else{
    		logger.append("File Uploaded Succesfully : Generate Tree now\n");
    		Tree<String> xml_parsed_tree = new Tree<String>(parser.root);
    		//XMLTree.isTreeParsed=true;
    		XMLTree.xml_parsed_tree=xml_parsed_tree;
    		XMLTree.root=parser.root;
    		System.out.println(XMLTree.isTreeParsed);
    		xml_parsed_tree.getPreOrderTraversal(); // for width also it has to be run first
    		tree_logger.append(xml_parsed_tree.logger);
    		int heightOfTree=xml_parsed_tree.getLongestPathFromRootToAnyLeaf().size();
    		int widthOfTree=xml_parsed_tree.width;
            


    		/* To be implement next week
    		 * JPanel jp=new JPanel();
    	JTree tree=new JTree(Validator.top);
    	tree.setDragEnabled(true);
    	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
    	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    	final JScrollPane jsp = new JScrollPane(tree,v,h);

    	jp.add(jsp);
    	printree.add(jsp);
    		 */
    	}
    	
	}
    
    
    
/**
 * 
 * This creates the GUI;
 * 
 */
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("GuruDev-XML Parser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(new Main());
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(30, 50);
    }
/**
 * 
 * @param args
 * This is the entry point of the program
 */
  
	public static void main(String[] args) {
       
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               createAndShowGUI();
            }
        });
        
    }
	static char [] special_char_detect(String text1){
		char text[]=text1.toCharArray();
		   char act_text[]=new char[text.length];
		   
		   for(int i=0; i<text.length-4;i++){
		      if(text[i]=='&' && text[i+1]=='#'){
		         int n=i;
		         while(text[i]!=';'){
		         	i++;	
		         }
		         
		         	int a=Integer.parseInt(text1.substring(n+2, i));
		         	
		         	act_text[n]=(char)a;
		      }
		      else{
		         act_text[i]=text[i];
		      }
		   }
		   
		   return act_text;
		}

@Override
public void mouseClicked(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
}
