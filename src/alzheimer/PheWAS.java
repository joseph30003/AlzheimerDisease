package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class PheWAS {

public static void Edges_update(int node1,int node2,Connection conn) {
		
	    
    	try{
    		
    			
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO PheWAS_edges(node1,node2) VALUES(?,?)");
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
    	
	public static int Nodes_update(String name,String type,Connection conn) {
    		
    	    int id=1;
    	   
        	try{
        		
        	    
        		
        		String query_node= "select id from PheWAS_nodes where name=\""+name+"\"";
        		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
        		
        		if(rs_node.next()){
        			id=rs_node.getInt(1);
        			
        		}else{
        			
        			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO PheWAS_nodes(name,type) VALUES(?,?)");
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
	
	public static void relations_seperate(List<String> relations,List<String> types,Connection conn) {
		
	    if(relations.size() >=2){
	    try{
	    	 
	    	        for (int i=0; i<relations.size();i++){
	    			for (int j=i+1; j<relations.size();j++){
	    				String Node1=String_handler.Nodes_sperate(relations.get(i));
	    				String type1=types.get(i);
	    				String Node2=String_handler.Nodes_sperate(relations.get(j));
	    				String type2=types.get(j);
	    				relations_update(Node1,type1,Node2,type2,conn);
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
	
	public static void relations_update(String node1,String type1,String node2,String type2,Connection conn) {
		
	   	String[] node_1=String_handler.String_spliter(node1);
	   	String[] node_2=String_handler.String_spliter(node2);
	   	int id_1;
	   	int id_2;
	   	for (int i=0; i<node_1.length;i++){
	   		
	   		id_1=Nodes_update(node_1[i],type1,conn);
	   		
			for (int j=0; j<node_2.length;j++){
			
			id_2=Nodes_update(node_2[j],type2,conn);	
			
			Edges_update(id_1,id_2,conn);	
				
				
			}
	   	}	
	
	}
	
	public static void main(String[] args)
	  {
		try{
		// create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String query="select snp,phewas_phenotype,gene_name from phewas";
	      
	     
	      List<String> relations = new ArrayList<String>();
	      List<String> types = new ArrayList<String>();
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java result set
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	 
	    	  
	    	  
	    	  if(!rs.getString(1).equals("")){
	    		
	    		 relations.add(rs.getString(1));
	    		 types.add("snp");
	        	
	        	 }
	         if(!rs.getString(2).equals("")){
	        	 relations.add(rs.getString(2).replaceAll("\"", ""));
	        	 types.add("disease");
	        	 
	        	 }
	         if(!rs.getString(3).equals("")&&!rs.getString(3).equals("NULL")){
	        	 relations.add(rs.getString(3));
	        	 types.add("gene");
	        	 
	        	 }
	         
	         relations_seperate(relations,types,conn);
	         
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
