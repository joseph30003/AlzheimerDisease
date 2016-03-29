package database;

import java.sql.Connection;
import java.sql.DriverManager;

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
		      
		      
		      gwas.build();
		     
		      /*Node[] nn=gwas.getNodes("gene");
		      Gene[] gg=new Gene[nn.length];
		      for(String t:gwas.types){
		    	  System.out.println(t);
		      }
		      
		      for(int i=0;i<nn.length;i++){
		    	  if(!nn[i].name.replace(" ", "").matches("[0-9]+")){
		    	  System.out.println(nn[i].name); 
		    	  }
		    	  //gg[i]=new Gene(nn[i].name,conn);
		      }
		      
		      /*for(Gene g:gg){
		    	  System.out.println(g.name); 
		    	  System.out.println(g.ID);
		      }*/

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
