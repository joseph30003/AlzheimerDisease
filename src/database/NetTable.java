package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class NetTable {
	
	
	
	protected Connection conn;
	public  String nodeTable;
	public  String edgeTable;
	public  int NodesNum;
	public  int EegesNum;
	
	public NetTable (String net,Connection con){
		
		nodeTable = net+"_nodes";
		edgeTable = net+"_edges";
		conn=con;
		NodesNum=countNodes();
		NodesNum=countEdges();
		
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
                + "name VARCHAR(200)," 
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
		String result=findEdge(id,true);
		result=result+findEdge(id,false);
		return result;
	}
		
	public String findEdge(int id, boolean column){
		String result="";
		String query="select node2 from "+edgeTable+" where node1="+id;
		if(!column){
			query="select node1 from "+edgeTable+" where node2="+id;
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
		
	    
    	try{
    			  	if(!containEdge(node1,node2)){
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+edgeTable+"(node1,node2) VALUES(?,?)");
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
    		
    	    try{
       		if(findNode(name,type)==-1){
        			
        		      			
        			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+nodeTable+"(name,type) VALUES(?,?)");
    	            pst_user.setString(1, name);
    	            pst_user.setString(2, type);
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
    
    
    
	
	

}