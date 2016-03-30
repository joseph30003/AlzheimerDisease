package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import gene.Gene;

public class NetTable {
	
	
	
	protected Connection conn;
	public  String NetName;
	public  String nodeTable;
	public  String edgeTable;
	public  int NodesNum;
	public  int EdgesNum;
	public  String[] types;
	public  int[] TypeNum;
	
	public NetTable (String net,Connection con){
		NetName=net;
		nodeTable = net+"_nodes";
		edgeTable = net+"_edges";
		conn=con;
		
		
	}
	public void Report(){
		NodesNum=countNodes();
		EdgesNum=countEdges();
		types=Types();
		TypeNum=CountofTypes();
	}
	
	
	public void build(){
		dropNet();
		createNet();
	}
	
	public void dropNet(){
		dropTable(edgeTable);
		dropTable(nodeTable);
		
	}
	
	public void createNet(){
		createTable(nodeTable);
		createTable(edgeTable);
	}
		
	public void dropTable(String table){
		String dropTable="DROP TABLE IF EXISTS "+table;
        try {
            Statement st = conn.createStatement();
        	st.executeUpdate(dropTable);
                                   
            System.out.println("Table  "+table+" droped");
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
		
	}
	
	public void createTable(String table) {
        
		String Table="";
		
		if(table.equals(nodeTable)) {
    		
    		Table = "CREATE TABLE "+table+" (" 
                + "id INT NOT NULL AUTO_INCREMENT,"  
                + "name VARCHAR(600)," 
                + "reference_name VARCHAR(100),"
                + "type VARCHAR(100),PRIMARY KEY (id))";
            }else{
            Table = "CREATE TABLE "+table+" (" 
                    + "node1 int,"  
                    + "node2 int," 
                    + "FOREIGN KEY (node1) REFERENCES "+nodeTable+" (id),"
                    + "FOREIGN KEY (node2) REFERENCES "+nodeTable+" (id))";
            }
              
        try {
            
        	Statement st = conn.createStatement();
            //The next line has the issue
            
            if(!Table.isEmpty()){
            
			st.executeUpdate(Table);
            }else{
            	System.out.println("no sql statements!");
            }
            System.out.println("network  "+table+" Created");
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public int findNode(String name,String type){
		int id=-1;
    	try{
    		Statement st = conn.createStatement();    
     		String query_node= "select id from "+nodeTable+" where name=\""+name+"\"";
     		ResultSet rs_node = st.executeQuery(query_node);
     		
     		if(rs_node.next()){
     			id=rs_node.getInt(1);     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return id;
	 }
	
	public boolean containEdge(int node1,int node2){
		
		    		
     		if(containSingleEdge(node1,node2)){
     			return true;     			
     		}else if(containSingleEdge(node2,node1)){
     			return true;
     		}else return false;
     		
    	
	}
	
	public boolean containSingleEdge(int node1,int node2){
		boolean result=false;
		String query="select * from "+edgeTable+" where node1="+node1+" and node2="+node2;
		
		try{
 		    
			Statement st = conn.createStatement();
     		ResultSet rs1 = st.executeQuery(query);
     		
     		
     		if(rs1.next()){
     			result=true;    			
     		}
     	    st.close(); 	
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
		return result;	
		
	}
	
	public String findEdge(int id){
		String result=findEdge(id,true,edgeTable);
		result=result+findEdge(id,false,edgeTable);
		return result;
	}
	
	public String findEdge(int id,String Table){
		String result=findEdge(id,true,Table);
		result=result+findEdge(id,false,Table);
		return result;
	}
		
	public String findEdge(int id, boolean column,String Table){
		String result="";
		String query="select node2 from "+Table+" where node1="+id;
		if(!column){
			query="select node1 from "+Table+" where node2="+id;
		}
		try{
 		    
			Statement st = conn.createStatement();
     		ResultSet rs_node = st.executeQuery(query);
     		
     		while(rs_node.next()){
     			result=result+rs_node.getInt(1)+",";     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return result;
	}
	
    public void edgeInsert(int node1,int node2) {
		
	    edgeInsert(node1,node2,edgeTable);
    	
	}
    
    public void edgeInsert(int node1,int node2,String Table) {
		
	    
    	try{
    			  	if(!containEdge(node1,node2)){
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+Table+"(node1,node2) VALUES(?,?)");
    	            pst_user.setInt(1, node1);
    	            pst_user.setInt(2, node2);
    	            pst_user.execute();
    	            pst_user.close(); }
    		    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
	}	
    

    public int nodeInsert(String name,String type) {
    	
    	return nodeInsert(name,type,null,nodeTable);
    	    
	}
    
    public int nodeInsert(String name,String type,String reference) {
		
	    return nodeInsert(name,type,reference,nodeTable);

}    
    public int nodeInsert(String name,String type,String reference,String Table) {
		
	    try{
   		if(findNode(name,type)==-1){
    			
    		      			
    			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+Table+"(name,type,reference_name) VALUES(?,?,?)");
	            pst_user.setString(1, name);
	            pst_user.setString(2, type);
	            if(reference==null){
	            pst_user.setNull(3,java.sql.Types.VARCHAR);  	
	            }else pst_user.setString(3, reference);
	            pst_user.execute();
	            pst_user.close();          		
	            
    		}
    	    		
    				
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
    	return findNode(name,type);

}    
    
    
    public String[] Types(){
    	String rs="";
    	try{
    		Statement st = conn.createStatement();    
     		String query= "select distinct(type) from "+nodeTable;
     		ResultSet rs_node = st.executeQuery(query);
     		
     		while(rs_node.next()){
     			rs=rs+rs_node.getString(1)+",";  			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
    	if(rs.isEmpty())	return null;
    	else return rs.split(",");
    	}
    public int[] CountofTypes(){
    	if(types==null)return null;
    	else{
    	int[] rs=new int[types.length];
    	for(int i=0;i<types.length;i++){
    		rs[i]=countofTable(nodeTable,types[i]);
    	}
    	return rs;
    	}
    }
    
    public int countNodes(){
        return countofTable(nodeTable);
    	}
	
    public int countEdges(){
        return countofTable(edgeTable);
    	}
     
    public int countofTable(String table){
        
    	int count=-1;
    	try{
    		Statement st = conn.createStatement();    
     		String query_node= "select count(*) from "+table;
     		ResultSet rs_node = st.executeQuery(query_node);
     		
     		if(rs_node.next()){
     			count=rs_node.getInt(1);     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return count;
    	}
    public int countofTable(String table,String type){
        
    	int count=-1;
    	try{
    		Statement st = conn.createStatement();    
     		String query_node= "select count(*) from "+table+" where type=\""+type+"\"";
     		ResultSet rs_node = st.executeQuery(query_node);
     		
     		if(rs_node.next()){
     			count=rs_node.getInt(1);     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return count;
    	}
    
    public String[] String_spliter(String in,String stop){
		String[] array=in.split(stop);
		List<String> list = new ArrayList<String>();

	    for(String s : array) {
	       if(s != null && s.length() > 0) {
	          list.add(s);
	       }
	    }

	    array = list.toArray(new String[list.size()]);
		return array;
			
	}
	    
    public int getNumofType(String type){
    	this.Report();	
    	int rs=-1;
    	for(int i=0;i<types.length;i++){
    		if(types[i].equals(type)) rs =TypeNum[i];
    	}
    	return rs;
    }
     
    
    public Node[] getNodes(String... type){
      int length=this.countNodes();
      
      String cond=" order by id";
  	  if(type.length>0){
  		  cond=" where type=\""+type[0]+"\""+cond;
  		  length=getNumofType(type[0]);
  	  }
  	     	
    
     if(length>0){
     Node[] nodes=new Node[length];
     
    	 try{
     		Statement st = conn.createStatement();    
      		String query_node= "select id,name,reference_name,type from "+nodeTable+cond;
      		ResultSet rs = st.executeQuery(query_node);
      		int i=0;
      		while(rs.next() && i<length){
      		  
      			nodes[i]=new Node(rs.getString(2),rs.getString(4),rs.getString(3),rs.getInt(1),NetName);
      			i++;
      		}
      		st.close();
      	}
      	catch (Exception e)
          {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            e.printStackTrace();
          } 
    
     return nodes;	
     }else return null;	
    }
    
    public void UpdateRef(String text,int id) {
		try
	    {
	    
				PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+nodeTable+" set reference_name = ? where id = ?");
			
			    node_update.setString(1, text);
			    node_update.setInt(2, id);
				node_update.executeUpdate();
				
	   }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
    public void UpdateGene(Gene gene,int id){
    	
    	UpdateRef(gene.name,id);
    	
    }
	
	

}
