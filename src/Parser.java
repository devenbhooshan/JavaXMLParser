import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;

public class Parser {

	 String logger;
	 String xml_file_data;
	 Node<String> root ;
	 Tree<String> tree ;
	 int root_count_id=1;
	public Parser(){
		logger="";
		xml_file_data="";
		root = new Node<String>("XML");
		tree = new Tree<String>(root);
	}
	public  void parser_Wrapper(String filepath){
		try {
			FileReader fileReader=new FileReader(filepath);
			BufferedReader in=new BufferedReader(fileReader);
			String st;

			while((st=in.readLine())!=null){
				xml_file_data+=st+"\n";

			}
			root.root_id=Tree.counter;
			Tree.counter++;
			xml_file_data=remove_unnecessary_whitespaces(xml_file_data);
			//System.out.println(xml_file_data);
			int indexstart=xml_file_data.indexOf("<?");
			int indexend=xml_file_data.indexOf(">");
			if(indexstart==-1)
			{ 
				this.logger="Error : Opening < Not found \n Line: 1\n";
				return ;
			}
			if(indexend==-1)
			{ 
				logger="Error : Opening ?> Not found \n Line: 1\n";
				return ;
			}
			if(xml_file_data.substring(indexstart, indexend).contains("?xml")){
				parser(xml_file_data.substring(indexend+1),root,1+xml_file_data.substring(0,indexend+1).split("\\n").length);
			}
			else {
				logger="xml tag not found";
				return ;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	static  int index=1;
	private void parser(String xml,Node<String> root,int linenumer) {
		
		if(xml.trim().length()==0 ) return ;
		int indexOpen=xml.indexOf('<');
		int indexClose=xml.indexOf('>')+1;
		if(indexOpen>=indexClose) { logger="Error : Stray > found \n Line : "+linenumer+"\n"; return ;}
		if(indexOpen==-1){ 
			logger="Error : Opening < Not found \n Line : "+linenumer+"\n";
			System.out.println(xml+"Error : Opening < Not found ");
			return ;
		}
		
		if(indexClose==-1){ 
			logger="Error : Closing > Not found \n Line :"+linenumer+"\n";
			return ;
		}
		String middle="";
		String s[];
		Node<String> child;
		DefaultMutableTreeNode childNode;
		if(xml.substring(indexOpen, indexClose).contains("/")){
			logger="Error : Opening Tag Not found \n"+xml.substring(indexOpen, indexClose)+"\n Line:"+linenumer+"\n";
			return ;
		}
		if(xml.substring(indexOpen, indexClose).contains(" ")){
			if(xml.substring(indexOpen+1, indexClose-1).contains("<")){
				logger="Error : Stray  < found \n"+countNoOfLines(xml.substring(indexOpen+1, indexClose-1))+linenumer+"\n";
				return ;
			}
			if(xml.substring(indexOpen+1, indexClose-1).contains(">")){
				logger="Error : Stray  > found \n"+countNoOfLines(xml.substring(indexOpen+1, indexClose-1))+linenumer+"\n";
				return ;
			}
			s=xml.substring(indexOpen+1, indexClose-1).split(" ");
			if(HasErrors(s)==1) {logger="Error : Invalid Tag Names "+s[0]+" \n Line:"+linenumer+"\n";return ; }
			if(HasErrors(s)==2) {logger="Error : Tag name is XML \n Invalid Tag Names "+s[0]+" \n Line:"+linenumer+"\n";return ; }
			middle=s[0];
			int i=1;
			int len=s.length;
			child = new Node<String>(middle);

			child.root_id=Tree.counter;
			Tree.counter++;
			
			child.attrib="";
			
			while(i<len){
				child.attrib+=s[i];
				i++;
			}
			root.addChild(child);
			
		}else {
			if(xml.substring(indexOpen+1, indexClose-1).contains("<")){
				logger="Error : Stray < found \n Line: "+linenumer+"\n";
				return ;
			}
			if(xml.substring(indexOpen+1, indexClose-1).contains(">")){
				logger="Error : Stray > found \n"+ countNoOfLines(xml.substring(indexOpen+1, indexClose-1))+linenumer+"\n";
				return ;
			}
			String s2[]=xml.substring(indexOpen+1, indexClose-1).split(" ");
			if(HasErrors(s2)==1) {logger="Error : Tag is alphanumeric \n Invalid Tag Names: "+s2[0]+" \n Line:"+linenumer+"\n";return ; }
			if(HasErrors(s2)==2) {logger="Error : Tag name is XML \n Invalid Tag Names: "+s2[0]+" \n Line:"+linenumer+"\n";return ; }
			child = new Node<String>(xml.substring(indexOpen+1, indexClose-1));
			child.root_id=Tree.counter;
			Tree.counter++;
			root.addChild(child);
			childNode=new DefaultMutableTreeNode(xml.substring(indexOpen+1, indexClose-1));
			middle=xml.substring(indexOpen+1, indexClose-1);
		}
		int index_end_tag=xml.indexOf("</"+middle+">");
		if(index_end_tag==-1)
		{ 
			logger="Error : Closing Tag Not found \n "+"</"+middle+"> \nLine: "+linenumer+"\n";
			return ;
		}
		if(xml.substring(indexClose, index_end_tag).contains("<")){
			parser( xml.substring(indexClose, index_end_tag),child,linenumer+countNoOfLines(xml.substring(0,indexClose)));
		}	
		else
		{
			child.data_part=xml.substring(indexClose, index_end_tag);
		}
		int offset=("</"+middle+">").length();
		parser( xml.substring(index_end_tag+(offset)),root,linenumer+countNoOfLines(xml.substring(0,index_end_tag)));
	}

	private static String remove_unnecessary_whitespaces(String xml) {
		char s1[]=xml.toCharArray();
		int len=xml.length();
		int i=0;
		String s="";
		while(i<xml.length()){
			if(s1[i]==' '){
				if(i==0 || s1[i-1]=='<' || s1[i-1]=='/'){
					while(s1[i]==' ')
						i++;
				}

				else if(i+1<len && s1[i+1]!='>'){
					if(i+1<len && (s1[i+1]==' ' || s1[i+1]=='<')){
						i++;
					}
					else{
						s+=s1[i];
						i++;
					}
				}
				else
					i++;
			}
			else{
				s+=s1[i];
				i++;
			}

		}
		return s;
	}

	private static  int HasErrors(String tag[]){
		int i=0;
		int  check=0;
		for(;i<tag[0].length();i++){
			if(!((tag[0].charAt(i) >= 'A' && tag[0].charAt(i) <= 'Z') || (tag[0].charAt(i) >= 'a' && tag[0].charAt(i) <= 'z') || tag[0].charAt(i)=='_')){
				check=1;
			}      
		}
		if(tag[0].toLowerCase().compareTo("xml")==0  ){
			check=2;
		}
		return check;
	}
	private static  int countNoOfLines(String st){
		int i=0;
		int count=0;
		while(i<st.length()){
			if(st.charAt(i)=='\n'){
				count++;
			}
			i++;
		}
		return count;
	}
}
