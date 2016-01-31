package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;

public class GKB_normalization {

	public static void main(String[] args)
	  {
		
		
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update GKB_edges set node2 = ? where node2_gkb = ?");
		  String query = "select id,id_gkb from GKB_nodes";
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				int id=rs.getInt(1);
				String node=rs.getString(2);
				
				System.out.println(id+" "+node);
					  node_update.setInt(1, id);
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

}
