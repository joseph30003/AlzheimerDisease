package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;


public class KEGG {

public static void Edges_update(int node1,int node2,String entry,Connection conn) {
		
	    
    	try{
    		
    			
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO KEGG_edges(node1,node2,entry) VALUES(?,?,?)");
    	            pst_user.setInt(1, node1);
    	            pst_user.setInt(2, node2);
    	            pst_user.setString(3, entry);
    	            pst_user.execute();
    				
    			
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
	}	
    	
	public static int Nodes_update(String node,String type,Connection conn) {
    		
    	    int id=1;
    	    String[] node_s=node.split("\\$");
    	    String name=node_s[0];
    	    System.out.println("----"+name);
    	    String attributes="";
    		for (int i=1; i<node_s.length;i++){
    			attributes=node_s[i]+","+attributes;
    		}
    		System.out.println(attributes);
        	try{
        		
        	    
        		
        		String query_node= "select id from KEGG_nodes where name=\""+name+"\"";
        		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
        		
        		if(rs_node.next()){
        			id=rs_node.getInt(1);
        			System.out.println("exist");
        		}else{
        			
        			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO KEGG_nodes(name,type,attributes) VALUES(?,?,?)");
    	            pst_user.setString(1, name);
    	            pst_user.setString(2, type);
    	            pst_user.setString(3, attributes);
    	            pst_user.execute();
    	            
    	            ResultSet rs_node_new = conn.createStatement().executeQuery(query_node);
    	            if(rs_node_new.next()){
            			id=rs_node_new.getInt(1);
            			System.out.println("new");
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
	
	public static int Nodes_containts(String node,String attributes,Connection conn) {
		int result=0; 
		try{
		String query = "select id from KEGG_nodes where name=\""+node+"\" and attributes =\""+attributes+"\"";
		ResultSet rs = conn.createStatement().executeQuery(query);
	      
	     
		if(rs.next()){
			
			result = rs.getInt("id");
		}
			
		}
		catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
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
	      e.printStackTrace();
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
	
	public static void relations_seperate(List<String> relations,List<String> types,String entry,Connection conn) {
		
	    if(relations.size() >=2){
	    try{
	    	 
	    	        for (int i=0; i<relations.size();i++){
	    			for (int j=i+1; j<relations.size();j++){
	    				String Node1=String_handler.Nodes_sperate(relations.get(i));
	    				String type1=types.get(i);
	    				String Node2=String_handler.Nodes_sperate(relations.get(j));
	    				String type2=types.get(j);
	    				relations_update(Node1,type1,Node2,type2,entry,conn);
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
	
	public static void relations_update(String node1,String type1,String node2,String type2,String entry,Connection conn) {
		
	   	String[] node_1=String_handler.String_spliter(node1);
	   	String[] node_2=String_handler.String_spliter(node2);
	   	int id_1;
	   	int id_2;
	   	for (int i=0; i<node_1.length;i++){
	   		System.out.println(node_1[i]);
	   		id_1=Nodes_update(node_1[i],type1,conn);
	   		
			for (int j=0; j<node_2.length;j++){
			System.out.println(node_2[j]);
			id_2=Nodes_update(node_2[j],type2,conn);	
			
			Edges_update(id_1,id_2,entry,conn);	
				
				
			}
	   	}	
	
	}
	
	public static void main(String[] args)
	  {
		try{
		// create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String query="select Entry,Name,Gene,Drug,Reference from KEGG_Disease order by Entry";
	      
	      System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	      List<String> relations = new ArrayList<String>();
	      List<String> types = new ArrayList<String>();
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	    	  System.out.println(rs.getString("Entry"));
	    	  
	    	  if(!rs.getString("Name").equals("")){
	    		 String[] dis=rs.getString("Name").split("#");
	    		 String disease="";
	    		 for(int i=0;i<dis.length;i++){
	    			 disease=disease+dis[i]+"["+rs.getString("Entry")+"]"+"#";
	    		 }
	    		 relations.add(disease);
	    		 types.add("disease");
	        	 System.out.println(disease);
	        	 }
	         if(!rs.getString("Gene").equals("")){
	        	 relations.add(rs.getString("Gene"));
	        	 types.add("Gene");
	        	 System.out.println(rs.getString("Gene"));
	        	 }
	         if(!rs.getString("Drug").equals("")){
	        	 relations.add(rs.getString("Drug"));
	        	 types.add("Drug");
	        	 System.out.println(rs.getString("Drug"));
	        	 }
	         
	         relations_seperate(relations,types,rs.getString("Entry"),conn);
	         
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
