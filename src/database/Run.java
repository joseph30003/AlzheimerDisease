package database;

import java.sql.Connection;
import java.sql.DriverManager;


import mmserver.MetaMap;

public class Run {
	
		public static void main(String[] args)
	  {
			
			
			
			
		try{
			// create our mysql database connection
		      
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		   
		      NetTable kegg = new GWAS(conn);
		      MetaMap mm=new MetaMap("");
		      MetaTable mt=new MetaTable(conn,mm);
		      mt.setSource(kegg);
		      mt.run();
		      
		      

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
