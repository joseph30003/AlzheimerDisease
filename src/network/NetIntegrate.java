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
	      
	      NodesNetwork.run("GPK_nodes", "PharmGKB_nodes_trimed", "GPKG_nodes", conn);
	      EdgesNetwork.run("GPK_edges", "PharmGKB_edges_trimed", "GPKG_edges", "GPKG_nodes", conn);
	      
	      NodesNetwork.run("GPKG_nodes", "DrugBank_nodes", "GPKGD_nodes", conn);
	      EdgesNetwork.run("GPKG_edges", "DrugBank_edges", "GPKGD_edges", "GPKGD_nodes", conn);
	      
	      NodesNetwork.run("GPKGD_nodes", "FDA_nodes_trimed", "GPKGDF_nodes", conn);
	      EdgesNetwork.run("GPKGD_edges", "FDA_edges_trimed", "GPKGDF_edges", "GPKGDF_nodes", conn);
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }

	}

}
