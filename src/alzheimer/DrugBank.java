package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import com.mysql.jdbc.PreparedStatement;

public class DrugBank {

public static void Edges_update(int node1,int node2,Connection conn) {
		
	    
    	try{
    		
    			
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO DrugBank_edges(node1,node2) VALUES(?,?)");
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
    	
		
		
	public static String GetDrug_UMLS(int id,String name,Connection conn) {
		
	    String result=name;
	 
		
    	try{
    		
    	    
    		
    		String query_node= "select UMLS from DrugBank_Metamap where DrugBank_Metamap.`id` = "+id+" order by score limit 1;";
    		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
    		
    		if(rs_node.next()){
    			result=rs_node.getString(1);
    			
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
	
	public static int Nodes_update(String name,String type,int oldID,Connection conn) {
		
	    int id=1;
	 
		
    	try{
    		
    	    
    		
    		String query_node= "select id from DrugBank_nodes where name=\""+name+"\" and type=\""+type+"\"";
    		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
    		
    		if(rs_node.next()){
    			id=rs_node.getInt(1);
    			
    		}else{
    			
    			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO DrugBank_nodes(name,type,old_id) VALUES(?,?,?)");
	            pst_user.setString(1, name);
	            pst_user.setString(2, type);
	            pst_user.setInt(3, oldID);
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
	
public static int Nodes_update(String name,String type,Connection conn) {
		
	    int id=1;
	 
		
    	try{
    		
    	    
    		
    		String query_node= "select id from DrugBank_nodes where name=\""+name+"\" and type=\""+type+"\"";
    		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
    		
    		if(rs_node.next()){
    			id=rs_node.getInt(1);
    			
    		}else{
    			
    			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO DrugBank_nodes(name,type) VALUES(?,?)");
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

public static boolean isRealUMLS(String UMLS){
	boolean rs=true;
	String[] pool={"C1457887","C0012634","C3687832","C1273517","C1397014","C0030193","C1444656","C0278140"};
	for(String s: pool){
		if (s.equals("UMLS")) rs=false;
	}
	
	return rs;
	
}




public static void disease_update(int new_id,int old_id,Connection conn) {
	
   
 
	
	try{
		
		String disease_query="select UMLS from DrugBank_disease where id="+old_id+" and score > 900 order by score desc";
		Statement st = conn.createStatement();
        ResultSet rs_d = st.executeQuery(disease_query);
        while(rs_d.next()){
        	if(isRealUMLS(rs_d.getString(1))){  
        int disease_id=Nodes_update(rs_d.getString(1),"disease",conn);
        
        Edges_update(new_id,disease_id,conn);
      	break;  
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

public static void gene_update(int new_id,int old_id,Connection conn) {
	
   
 
	
	try{
		
		String p_query="select gene from DrugBank_gene where id="+old_id;
		Statement st = conn.createStatement();
        ResultSet rs_p = st.executeQuery(p_query);
        while(rs_p.next()){
       	  
        int p_id=Nodes_update(rs_p.getString(1),"gene",conn);
        Edges_update(new_id,p_id,conn);
      	  
        }
		        
		
		
		
				
	}
	catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
	

}
		

	
	public static void main(String[] args)
	  {
		try{
		// create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String query="select id,name from DrugBank_drug";
	      
	      
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java result set
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      { 
	    	  int old_id=rs.getInt(1);
	          String drug_name=GetDrug_UMLS(rs.getInt(1),rs.getString(2),conn);
	          int new_id=Nodes_update(drug_name,"drug",old_id,conn);
	          
	          disease_update(new_id,old_id,conn);
	          
	          gene_update(new_id,old_id,conn);
	    	 
	        	 
	       }
	         
	         
	      
		
	      
	      
	      
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
