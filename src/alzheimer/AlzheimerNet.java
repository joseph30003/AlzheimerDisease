package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class AlzheimerNet {

	public static void createNet(String network,Connection conn) {
        String dropEdges="DROP TABLE IF EXISTS "+network+"_edges";
        String dropNodes="DROP TABLE IF EXISTS "+network+"_nodes";
        
        
		String nodesTable = "CREATE TABLE "+network+"_nodes (" 
            + "id INT NOT NULL,"
            + "level INT NOT NULL,"
            + "name VARCHAR(100)," 
            + "type VARCHAR(100),PRIMARY KEY (id))";  
        
        String edgesTable = "CREATE TABLE "+network+"_edges (" 
                + "node1 int,"  
                + "node2 int)";
        
        
              
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
           
            st.executeUpdate(dropEdges);
            st.executeUpdate(dropNodes);
            st.executeUpdate(nodesTable);
            st.executeUpdate(edgesTable);
           
            System.out.println("network  "+network+" Created");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }	
	
public static boolean Edges_contains(int node1,int node2,String network,Connection conn){
	boolean result = false;
	try{
		String query = "select * from "+network+"_edges where node1="+node1+" and node2="+node2;
		ResultSet rs = conn.createStatement().executeQuery(query);
	      
	     
		if(rs.next()){
			//System.out.print(rs.getInt(1)+"\n");
			result = true;
		
		}
		
    }
 catch (Exception e)
  {
  System.err.println("Got an exception! ");
  System.err.println(e.getMessage());
  } 
	return result;
}
	
	
	
public static void Edges_update(String network,int node1,List<Integer> node2,Connection conn) {
		
	    
    	try{
    		     PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_edges(node1,node2) VALUES(?,?)");
    			for (int i=0; i<node2.size();i++){
    				
    				
    				if( !Edges_contains(node1,node2.get(i),network,conn) && !Edges_contains(node2.get(i),node1,network,conn) ){
    	            pst_user.setInt(1, node1);
    	            pst_user.setInt(2, node2.get(i));
    	            
    	            pst_user.execute();
    				}
    			}
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
	}	
    	
	public static void Nodes_update(String network,int node,int level,Connection conn) {
    		
    	    
        	try{
        		
        			
        				
        				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_nodes(id,level) VALUES(?,?)");
        	            pst_user.setInt(1, node);
        	            pst_user.setInt(2, level);
        	             pst_user.execute();
        				
        			
        			
        	}
        	catch (Exception e)
            {
              System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
            }

	}
	
	public static boolean Nodes_containts(String network,int node,Connection conn) {
		boolean result=false; 
		try{
		String query = "select id from "+network+"_nodes where id=\""+node+"\"";
		ResultSet rs = conn.createStatement().executeQuery(query);
	      
	     
		if(rs.next()){
			//System.out.print(rs.getInt(1)+"\n");
			result = true;
		}
			
		}
		catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
        return result;
	}
	
	
	
	public static List<Integer> Realtions_searcher (int keyword, String column_query, String column_related, String table, Connection conn )  {
		List<Integer> relations = new ArrayList<Integer>();
		try{   
		String query = "SELECT "+column_related+","+column_query+" FROM "+table+" where "+column_query+" = \""+keyword+"\"";
		 
		
	      // create the java statement
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next()){
	    	
	    	 if(!relations.contains(rs.getInt(column_related))) {
	    	  
	    	    relations.add(rs.getInt(column_related)) ;
	    	 }
	      }
		}
		catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
		return relations;
	}
	
	
	
	public static List<Integer> Realtions_collector (int keyword, String column1, String column2, String table, Connection conn ) {
		
		List<Integer> relations1 = new ArrayList<Integer>();
		List<Integer> relations2 = new ArrayList<Integer>();
		
		relations1=Realtions_searcher(keyword,column1,column2,table,conn);
		relations2=Realtions_searcher(keyword,column2,column1,table,conn);
		
		for(int i=0; i<relations2.size(); i++){
			if(!relations1.contains(relations2.get(i))){
				relations1.add(relations2.get(i));
				}
		}
		return relations1;
	}
	
	public static void run(int key,Connection conn)
	  {
		List<Integer> relations_1 = new ArrayList<Integer>();
		List<Integer> relations_0 = null;
		List<Integer> relations_list = new ArrayList<Integer>();
		String network="Alzheimer";
		
	    try
	    {
	    
	      
	      relations_list.add(key);
	      createNet(network,conn);
	      
	      for(int i=0; i<4; i++){
	    	
	    	  relations_1.clear();
	    	  relations_0 = new ArrayList<Integer>(relations_list);
	    	  relations_list.clear();
	               
	    	 
	    	  
	    	  for(int j=0; j<relations_0.size(); j++){
	    	  
	    	  if(!Nodes_containts(network,relations_0.get(j),conn)){
	    		  
	    		  Nodes_update(network,relations_0.get(j),i,conn); 
	    	      
	    		  relations_1=Realtions_collector(relations_0.get(j),"node1","node2","GPKGDF_edges",conn);
	      
	    	      for(int k=0; k<relations_1.size(); k++){
	  			   if(!relations_list.contains(relations_1.get(k))){
	  				relations_list.add(relations_1.get(k));
	  				  }
	    	        }// insert values to the list;
	    	  
	              Edges_update(network,relations_0.get(j),relations_1,conn);
	      
	    	  }
	                  
	       } 
	      
	      
	     }
	      
	     
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }
	
	
	
	
	
	public static void main(String[] args)
	  {
		List<Integer> relations_1 = new ArrayList<Integer>();
		List<Integer> relations_0 = null;
		List<Integer> relations_list = new ArrayList<Integer>();
		String network="Alzheimer_3layer";
		
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      relations_list.add(3218);
	      createNet(network,conn);
	     
	    for(int i=0;i<4;i++){
	    	
	    	  
	    	  relations_1.clear();
	    	  relations_0 = new ArrayList<Integer>(relations_list);
	    	  relations_list.clear();
	               
	    	 
	    	  
	    	  for(int j=0; j<relations_0.size(); j++){
	    	  
	    	  if(!Nodes_containts(network,relations_0.get(j),conn)){
	    		  
	    		  Nodes_update(network,relations_0.get(j),i,conn); 
	    	      
	    		  relations_1=Realtions_collector(relations_0.get(j),"node1","node2","GPKGDF_edges",conn);
	      
	    	      for(int k=0; k<relations_1.size(); k++){
	  			   if(!relations_list.contains(relations_1.get(k))){
	  				relations_list.add(relations_1.get(k));
	  				  }
	    	        }// insert values to the list;
	    	  
	              Edges_update(network,relations_0.get(j),relations_1,conn);
	      
	    	  }
	                  
	       } 
	      
	      
	     }
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	    
	 
	    
	  }
	
	
}
