package network;

import java.sql.Connection;
import java.sql.DriverManager;

public class Network_integeration {

	public static void networkGenerator(String net_1, String net_2,String net_final, Connection conn){
		  
		  NodesNetwork.createTable(net_final+"_nodes",conn);
	      NodesNetwork.copyTable(net_1+"_nodes",net_final+"_nodes",conn);
	      NodesNetwork.tableCombine(net_final+"_nodes",net_2+"_nodes",conn);
	      EdgesNetwork.createTable(net_final+"_edges",conn);
	      EdgesNetwork.copyTable(net_1+"_edges",net_final+"_edges",conn);
	      EdgesNetwork.copyTable(net_2+"_edges",net_final+"_edges",net_final+"_nodes",conn);
	      
	}
	
	
	public static void main(String[] args)
	  {
		
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      networkGenerator("table1","table2","newtable",conn);
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }
	
	
	
}
