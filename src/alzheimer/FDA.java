package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class FDA {
	
	public static void createNet(String network,Connection conn) {
        String dropEdges="DROP TABLE IF EXISTS "+network+"_edges";
        String dropNodes="DROP TABLE IF EXISTS "+network+"_nodes";
       
        
		String nodesTable = "CREATE TABLE "+network+"_nodes (" 
            + "id INT NOT NULL AUTO_INCREMENT,"  
            + "name VARCHAR(100)," 
            + "reference_name VARCHAR(100),"
            + "type VARCHAR(100),PRIMARY KEY (id))";  
        
        String edgesTable = "CREATE TABLE "+network+"_edges (" 
                + "node1 int,"  
                + "node2 int," 
                + "FOREIGN KEY (node1) REFERENCES "+network+"_nodes (id),"
                + "FOREIGN KEY (node2) REFERENCES "+network+"_nodes (id))";
        
       
              
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
	
	
	
 public static void Edges_update(String network,int node1,int node2,Connection conn) {
		
	    
    	try{
    		
    			
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_edges(node1,node2) VALUES(?,?)");
    	            pst_user.setInt(1, node1);
    	            pst_user.setInt(2, node2);
    	            
    	            pst_user.execute();
    				
    			
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
	}	
    	
	public static int Nodes_update(String network,String name,String type,Connection conn) {
    		
    	    int id=1;
    	 
    		
        	try{
        		
        	    
        		
        		String query_node= "select id from "+network+"_nodes where name=\""+name+"\"";
        		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
        		
        		if(rs_node.next()){
        			id=rs_node.getInt(1);
        			
        		}else{
        			
        			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_nodes(name,type) VALUES(?,?)");
    	            pst_user.setString(1, name);
    	            pst_user.setString(2, type);
    	           
    	            pst_user.execute();
    	            
    	            ResultSet rs_node_new = conn.createStatement().executeQuery(query_node);
    	            if(rs_node_new.next()){
            			id=rs_node_new.getInt(1);
            			
            		}
    	            
        		}
        		
        		
        				
        	}
        	catch (Exception e)
            {
              System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
              e.printStackTrace();
            }
        	return id;

	}
	
	public static void relations_seperate(String network,List<String> relations,List<String> types,Connection conn) {
		
	    if(relations.size() >=2){
	    try{
	    	 
	    	        for (int i=0; i<relations.size();i++){
	    			for (int j=i+1; j<relations.size();j++){
	    				String Node1=relations.get(i);
	    				String type1=types.get(i);
	    				String Node2=relations.get(j);
	    				String type2=types.get(j);
	    				relations_update(network,Node1,type1,Node2,type2,conn);
	    			}
	    			}
	    	}
	    	catch (Exception e)
	        {
	          System.err.println("Got an exception! ");
	          System.err.println(e.getMessage());
	          e.printStackTrace();
	        }
			
		}	
	
	}
	
	public static void relations_update(String network,String node1,String type1,String node2,String type2,Connection conn) {
		
	   	
	   	int id_1;
	   	int id_2;
	   	
	   		
	   		id_1=Nodes_update(network,node1,type1,conn);
	   			
			
			id_2=Nodes_update(network,node2,type2,conn);	
			
			Edges_update(network,id_1,id_2,conn);	
				
				
	   		
	
	}
	
	
	
	public static void main(String[] args)
	  {
		try{
		// create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      createNet("FDA",conn);
	      
	      String query="select FDA.`Therapeutic_Area`,FDA.`Drug`,FDA.`Biomarkeri` from FDA;";
	      
	     
	      List<String> relations = new ArrayList<String>();
	      List<String> types = new ArrayList<String>();
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java result set
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	 
	    	  
	    	  if(!rs.getString(1).equals("")){
	    		
	    		 relations.add(rs.getString(1));
	    		 types.add("disease");
	        	 
	        	 }
	    	  if(!rs.getString(2).equals("")){
		        	 relations.add(rs.getString(2));
		        	 types.add("drug");
		        	 
		        	 }
	    	  
	         if(!rs.getString(3).equals("")){
	        	 if(rs.getString(3).contains(",")){
	        	 String[] gene=rs.getString(3).split(",");
	        	 for(int i=0;i<gene.length;i++){
	        		 relations.add(gene[i]);
		        	 types.add("gene");
	        	 }
	        		 
	        		 
	        	 }else{
	        	 relations.add(rs.getString(3));
	        	 types.add("gene");
	        	 }
	        	 }
	         
	         
	         relations_seperate("FDA",relations,types,conn);
	         
	         relations.clear();
	         types.clear();
	         }
	      //return null;
	      
	      st.close();
	       
	      
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	      e.printStackTrace();
	    }
		
		
		
	  }
}
