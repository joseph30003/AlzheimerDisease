package network;

import java.sql.Connection;
import java.sql.DriverManager;

public class NetIntegrate {

	public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      NodesNetwork.run("GWAS_nodes_trimed", "PheWAS_nodes_trimed", "GP_nodes", conn);
	      EdgesNetwork.run("GWAS_edges_trimed", "PheWAS_edges_trimed", "GP_edges", "GP_nodes", conn);
	      
	      NodesNetwork.run("GP_nodes", "KEGG_nodes_trimed", "GPK_nodes", conn);
	      EdgesNetwork.run("GP_edges", "KEGG_edges_trimed", "GPK_edges", "GPK_nodes", conn);
	      
	      NodesNetwork.run("GPK_nodes", "GKB_nodes_trimed", "GPKG_nodes", conn);
	      EdgesNetwork.run("GPK_edges", "GKB_edges_trimed", "GPKG_edges", "GPKG_nodes", conn);
	      
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }

	}

}
