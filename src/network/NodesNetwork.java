package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Statement;


import com.mysql.jdbc.PreparedStatement;

public class NodesNetwork {

	public static void createTable(String table_name,Connection conn) {
        String myTableName = "CREATE TABLE "+table_name+" (" 
            + "id INT NOT NULL AUTO_INCREMENT,"  
            + "name VARCHAR(100)," 
            + "type VARCHAR(100)," 
            + "net2_id INT, PRIMARY KEY (`id`))";  
        
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(myTableName);
            System.out.println("Table Created");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public static void copyTable(String table1, String table2,Connection conn) {
        String myTableName = "insert into "+table2+"(id,name,type) select id,name,type from "+table1;  
        
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(myTableName);
            System.out.println("Table copyed");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	
	
	public static int node_contain(String node,String table,Connection conn) {
		
		int result = 0;
	    
    	try{
    		
    		String query = "select id from "+table+" where name=\""+node+"\"";
    		
    		ResultSet rs = conn.createStatement().executeQuery(query);
    		  
    	     
    		if(rs.next()){
    			
    			result = rs.getInt(1);
    		}	
    		
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
		return result;
	}	
	
	

	
	
	public static void tableCombine(String table1,String table2,Connection conn) {
		
	    
    	try{
    		    PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table1+" set net2_id = ? where id = ?");
    		    PreparedStatement node_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+table1+"(name,net2_id) VALUES(?,?)");
    		    String query = "select id,name from "+table2;
    			ResultSet rs = conn.createStatement().executeQuery(query);
    			  
    		     
    			while(rs.next()){
    				int table2_id=rs.getInt(1);
    				String node=rs.getString(2);
    				int table1_id=node_contain(node,table1,conn);
    				if(table1_id>0){
    					  node_update.setInt(1, table2_id);
    					  node_update.setInt(2, table1_id);
    				      node_update.executeUpdate();
    				}else{
    				  node_insert.setString(1, node);
  					  node_insert.setInt(2, table2_id);
  				      node_insert.executeUpdate();
    				}
    			}
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
	}	
    
	public static void run(String table1,String table2,String final_table,Connection conn){
		
		  createTable(final_table,conn);
	      copyTable(table1,final_table,conn);
	      tableCombine(final_table,table2,conn);	
		
	}
	
	
	
	
	public static void main(String[] args)
	  {
		
		String net_1="table1";
		String net_2="table2";
		String net_final=net_1+net_2;
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      createTable(net_final+"_nodes",conn);
	      copyTable(net_1+"_nodes",net_final+"_nodes",conn);
	      tableCombine(net_final+"_nodes",net_2+"_nodes",conn);
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }

}
