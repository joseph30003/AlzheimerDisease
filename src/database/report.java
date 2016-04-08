package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

public class report {
	
	public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String[] sources={"KEGG","GWAS","PheWAS","PharmGKB","FDA","DrugBank"};
	      NetTable[] nets = new NetTable[sources.length];
	      for(int i=0;i < sources.length; i++){
	      nets[i] = new NetTable(sources[i],conn);
	      }
	      for(NetTable net : nets){
	    	  net.Report();
	    	  System.out.println(net.NetName);
	    	  System.out.println(Arrays.toString(net.types));
	    	  System.out.println(Arrays.toString(net.TypeNum));
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
