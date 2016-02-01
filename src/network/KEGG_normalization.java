package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class KEGG_normalization {
   
	
	private static int geneTrimer(String str){
		
		return Integer.parseInt(str.substring(str.indexOf("HSA:")+4,str.length()-1));
		
		
	}
	
	public static String getGene(int Gene_ID,Connection conn){
		String result=null;
		try{
			  String query="select Symbol from GeneInfo where GeneID="+Gene_ID;
		      Statement st = conn.createStatement();
		         
		      ResultSet rs = st.executeQuery(query);
		      if (rs.next())
		      {
		    	  result = rs.getString(1).toUpperCase();
		    	 
		      }
		}
		catch (Exception e){
			System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		}
		return result;
	
	}
	
	public static void nodesUpdate(int id, String gene, String table, Connection conn){
		
		try
	    { 
		  PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table+"_nodes set reference_name = ? where id = ?");
		  node_update.setInt(2, id);
		  node_update.setString(1, gene);
		  node_update.executeUpdate();
		}
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		try{
			  String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      String query="select id,attributes from KEGG_nodes where type='gene'";
		      Statement st = conn.createStatement();
		      ResultSet rs = st.executeQuery(query);
		      while (rs.next())
		      {
		    	  if(rs.getString(2).toUpperCase().contains("HSA")){
		    	  nodesUpdate(rs.getInt(1),getGene(geneTrimer(rs.getString(2).toUpperCase()),conn),"KEGG",conn);
		         }else{
		          System.out.println(rs.getInt(1)+" "+rs.getString(2).toUpperCase());
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


