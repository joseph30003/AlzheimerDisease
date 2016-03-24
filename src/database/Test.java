package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {

	public static void main(String[] args)
	  {
		try{
			// create our mysql database connection
		      
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		   
		      NetTable GWAS = new GWAS(conn);
		      GWAS.build();
		     
		      System.out.println(GWAS.NodesNum);
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
