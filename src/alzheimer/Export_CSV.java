package alzheimer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Export_CSV {
	
	
	public static void main(String[] args) throws IOException
	  {
		PrintWriter fw0 = new PrintWriter(new FileWriter("C:/Users/xf37538/Documents/GKB_edges.csv",true));
	    try
	    {
	     
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	       
	      
	      String query = "SELECT node1,node2 FROM edges";
	 
	     
	      Statement st = conn.createStatement();
	       
	  
	      ResultSet rs = st.executeQuery(query);
	       
	     
	      while (rs.next())
	      {
	        
	        String node1 = rs.getString("node1");
	        String node2 = rs.getString("node2");
	        //String name = rs.getString("name");
	        //String type = rs.getString("type");
	        fw0.write(node1+";"+node2+"\n");
		   
	       
	      }
	      fw0.close();
	      st.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
	  }

}
