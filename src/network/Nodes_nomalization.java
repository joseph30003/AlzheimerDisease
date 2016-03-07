package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class Nodes_nomalization {
	public static void createColumn(String table_name,Connection conn) {
        String myTableName = "ALTER TABLE "+table_name+" ADD reference_name VARCHAR(100) AFTER name";  
        
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(myTableName);
            }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	
	public static void updateReference(String table,String reference,Connection conn) {
		try
	    {
	    
	      
	      PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table+" set reference_name = ? where name = ?");
		  String query = "select id,name from "+reference;
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				String id=rs.getString(1);
				String node=rs.getString(2);
				
				System.out.println(id+" "+node);
					  node_update.setString(1, id);
					  node_update.setString(2, node);
				      node_update.executeUpdate();
				
				
			}
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
	public static void metamapUpdate(Connection conn) {
		try
	    {
	    
	      
	      
		  String query = "select a.id,a.source,a.source_id from Metamap a inner join (select source,source_id,max(score) max_score from Metamap group by source,source_id ) b on a.source=b.source and a.source_id=b.source_id and a.score=b.max_score and a.score>=900";
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				String id=rs.getString(1);
				String table=rs.getString(2);
				int source_id=rs.getInt(3);
				
				PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table+"_nodes set reference_name = ? where id = ?");
				
					  node_update.setString(1, id);
					  node_update.setInt(2, source_id);
				      node_update.executeUpdate();
				
				
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
		
		
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      metamapUpdate(conn);
	      
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }
	
	
	
}
