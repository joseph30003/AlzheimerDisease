package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GWAS_normalization {

	public static void main(String[] args) {
		try{
			  String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      String query="select id,name from GWAS_nodes where type='gene'";
		      Statement st = conn.createStatement();
		      ResultSet rs = st.executeQuery(query);
		      while (rs.next())
		      {
		    	  if (rs.getString(2).matches("[0-9]+") && (rs.getString(2).length() > 2)) {
		    	  KEGG_normalization.nodesUpdate(rs.getInt(1),KEGG_normalization.getGene(rs.getInt(2),conn),"GWAS",conn);
		    	  }
		      }
		      st.close();
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
