package alzheimer;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;


public class Network_generator {

	public static void Edges_update(String node1,List<String> node2,Connection conn) {
		
	    
    	try{
    		
    			for (int i=0; i<node2.size();i++){
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO edges(node1,node2) VALUES(?,?)");
    	            pst_user.setString(1, node1);
    	            pst_user.setString(2, node2.get(i));
    	            
    	            pst_user.execute();
    				
    			}
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
	}	
    	
	public static void Nodes_update(String node,Connection conn) {
    		
    	    
        	try{
        		
        			
        				
        				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO nodes(node) VALUES(?)");
        	            pst_user.setString(1, node);
        	            
        	             pst_user.execute();
        				
        			
        			
        	}
        	catch (Exception e)
            {
              System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
            }

	}
	
	public static boolean Nodes_containts(String node,Connection conn) {
		boolean result=false; 
		try{
		String query = "select * from nodes where node=\""+node+"\"";
		ResultSet rs = conn.createStatement().executeQuery(query);
	      
	     
		if(rs.next()){
			System.out.print(rs.getString(node)+"\n");
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
	
	
	
	public static List<String> Realtions_searcher (String keyword, String column_query, String column_related, String table, Connection conn )  {
		List<String> relations = new ArrayList<String>();
		try{   
		String query = "SELECT "+column_related+","+column_query+" FROM "+table+" where "+column_query+" = \""+keyword+"\"";
		 
		
	      // create the java statement
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next()){
	    	
	    	 if(!relations.contains(rs.getString(column_related))) {
	    	  
	    	    relations.add(rs.getString(column_related)) ;
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
	
	

	
	
	
	
	public static List<String> Realtions_collector (String keyword, String column1, String column2, String table, Connection conn ) {
		
		List<String> relations1 = new ArrayList<String>();
		List<String> relations2 = new ArrayList<String>();
		
		relations1=Realtions_searcher(keyword,column1,column2,table,conn);
		relations2=Realtions_searcher(keyword,column2,column1,table,conn);
		
		for(int i=0; i<relations2.size(); i++){
			if(!relations1.contains(relations2.get(i))){
				relations1.add(relations2.get(i));
				}
		}
		return relations1;
	}
	
	public static void main(String[] args)
	  {
		List<String> relations_1 = new ArrayList<String>();
		List<String> relations_0 = null;
		List<String> relations_list = new ArrayList<String>();
		
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      relations_list.add("PA443319");
	      
	      
	      while(relations_list.size()>0){
	    	
	    	  relations_1.clear();
	    	  relations_0 = new ArrayList<String>(relations_list);
	    	  relations_list.clear();
	               
	    	 
	    	  
	    	  for(int j=0; j<relations_0.size(); j++){
	    	  
	    	  if(!Nodes_containts(relations_0.get(j),conn)){
	    		  
	    		  Nodes_update(relations_0.get(j),conn); 
	    	      
	    		  relations_1=Realtions_collector(relations_0.get(j),"Entity1_id","Entity2_id","relationships",conn);
	      
	    	      for(int k=0; k<relations_1.size(); k++){
	  			   if(!relations_list.contains(relations_1.get(k))){
	  				relations_list.add(relations_1.get(k));
	  				  }
	    	        }// insert values to the list;
	    	  
	              Edges_update(relations_0.get(j),relations_1,conn);
	      
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
