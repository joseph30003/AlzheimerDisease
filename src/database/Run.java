package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Run {
	
		public static void main(String[] args)
	  {
		try{
			// create our mysql database connection
		      
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		   
		      NetTable KEGG = new KEGG(conn);
		      KEGG.build();
		     
		      System.out.println(KEGG.NodesNum);
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