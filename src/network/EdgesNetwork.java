package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class EdgesNetwork {

public static int node_new(int node_id,String table,Connection conn) {
		
		int result = 0;
	    
    	try{
    		
    		String query = "select id from "+table+" where net2_id=\""+node_id+"\"";
    		
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
	
	public static void createTable(String table_name,Connection conn) {
        String myTableName = "CREATE TABLE "+table_name+" (" 
            + "node1 INT,"
            + "node2 INT)";  
        
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
        String myTableName = "insert into "+table2+"(node1,node2) select node1,node2 from "+table1;  
        
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
	
	public static void copyTable(String table1, String table2,String referen_table,Connection conn) {
		try{
		    
		    PreparedStatement node_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+table2+"(node1,node2) VALUES(?,?)");
		    String query = "select node1,node2 from "+table1;
			ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
						
				      node_insert.setInt(1, node_new(rs.getInt(1),referen_table,conn));
					  node_insert.setInt(2, node_new(rs.getInt(2),referen_table,conn));
				      node_insert.executeUpdate();
				
			}
			
	}
	catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    }
	

	public static void run(String table1,String table2,String final_table,String Ref_table,Connection conn){
		
		  createTable(final_table,conn);
	      copyTable(table1,final_table,conn);
	      copyTable(table2,final_table,Ref_table,conn);
	      
	}
	
	
	
	
	public static void main(String[] args) {
		String net_1="table1";
		String net_2="table2";
		String net_final=net_1+net_2;
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      createTable(net_final+"_edges",conn);
	      copyTable(net_1+"_edges",net_final+"_edges",conn);
	      copyTable(net_2+"_edges",net_final+"_edges",net_final+"_nodes",conn);
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }

	}


