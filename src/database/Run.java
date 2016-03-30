package database;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.util.ArrayList;
//import java.util.List;

//import gene.Gene;
//import mmserver.MetaMap;

public class Run {
	
     	
	
		public static void main(String[] args)
	  {
			
		
	
		try{
			// create our mysql database connection
		      
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		   
		      NetTable gwas = new GWAS(conn);
		     
		      
		      
		     
		      Node[] nn=gwas.getNodes();
		      //List<Gene> gg = new ArrayList<Gene>();
		     
		      
		      for(int i=0;i<nn.length;i++){
		    	 
		    	  System.out.println(nn[i].name); 
		    	  
		    	 
		      }
		      
		      
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
