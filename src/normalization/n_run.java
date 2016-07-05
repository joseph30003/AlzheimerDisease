package normalization;

import java.sql.Connection;
import java.sql.DriverManager;

import database.NetTable;


public class n_run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String[] sources={"KEGG","GWAS","PheWAS","FDA","PharmGKB"};
			NetTable[] nets = new NetTable[5];
			 try
			    {
			     String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
			     Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
			     
			     for(int i=0;i< sources.length;i++){

			    	 nets[i] = new NetTable(sources[i],conn);
			    	 Disease D_normal = new Disease(nets[i]);
				     D_normal.Normalization();
			  	   
			    	 
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
