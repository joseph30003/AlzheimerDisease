package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class GKB_nomalization {

private static String UMLSTrimer(String str){
		
		return str.substring(str.indexOf("UMLS")+5,str.indexOf("UMLS")+13);
		
		
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
	
	public static void nodesUpdate(String id, String UMLS, Connection conn){
		
		try
	    { 
		  PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update GKB_nodes set reference_name = ? where id_gkb = ?");
		  node_update.setString(2, id);
		  node_update.setString(1, UMLS);
		  node_update.executeUpdate();
		}
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
	public static void UMLSUpdate(String type,Connection conn){
		try{
		 String query="select Accession_Id,External_Vocabulary,Name from pharmGKB_"+type;
	      Statement st = conn.createStatement();
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	  if(rs.getString(2).contains("UMLS")){
	    		  
	    		  nodesUpdate(rs.getString(1),UMLSTrimer(rs.getString(2)),conn);
	         }
	      }
	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	      e.printStackTrace();
	    }
	}
	
	public static void createNew(String network,Connection conn) {
        String dropEdges="DROP TABLE IF EXISTS "+network+"_edges";
        String dropNodes="DROP TABLE IF EXISTS "+network+"_nodes";
        
        
		String nodesTable = "CREATE TABLE "+network+"_nodes (" 
            + "id INT NOT NULL AUTO_INCREMENT,"  
            + "name VARCHAR(100)," 
            + "reference_name VARCHAR(100)," 
            + "type VARCHAR(100),"
            + "oldid int,"
            + "FOREIGN KEY (oldid) REFERENCES GKB_nodes (id),"
            + "PRIMARY KEY (id))";  
        
        String edgesTable = "CREATE TABLE "+network+"_edges(" 
                + "node1 int,"  
                + "node2 int," 
                + "FOREIGN KEY (node1) REFERENCES "+network+"_nodes (id),"
                + "FOREIGN KEY (node2) REFERENCES "+network+"_nodes (id))";
        
                    
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            
            st.executeUpdate(dropEdges);
            st.executeUpdate(dropNodes);
            st.executeUpdate(nodesTable);
            st.executeUpdate(edgesTable);
          ;
            System.out.println("network  "+network+" Created");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public static void copyTable(String table,Connection conn) {
        String myTableName = "insert into "+table+"(oldid,reference_name,name,type) select id,reference_name,name,type from GKB_nodes where type!='haplotype'";  
        
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(myTableName);
            System.out.println("Table copyed");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public static void copyTable(String table1, String table2,String referen_table,Connection conn) {
		try{
		    
		    PreparedStatement node_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+table2+"(node1,node2) VALUES(?,?)");
		    String query = "select node1,node2 from "+table1;
			ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				int node1_new=node_contain(rs.getInt(1),referen_table,conn);
				int node2_new=node_contain(rs.getInt(2),referen_table,conn);
				if(node1_new!=0 && node2_new!=0){
					  node_insert.setInt(1, node1_new);
					  node_insert.setInt(2, node2_new);
				      node_insert.executeUpdate();
				}
				      
				
			}
			
	}
	catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    }
	
public static int node_contain(int oldid,String table,Connection conn) {
		
		int result = 0;
	    
    	try{
    		
    		String query = "select id from "+table+" where oldid="+oldid;
    		
    		ResultSet rs = conn.createStatement().executeQuery(query);
    		  
    	     
    		if(rs.next()){
    			
    			result = rs.getInt(1);
    		}	
    		
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
		return result;
	}
	
	
	public static void main(String[] args) {
		try{
			  String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      
		      
		      createNew("PharmGKB",conn);
		      copyTable("PharmGKB_nodes",conn);
		      copyTable("GKB_edges","PharmGKB_edges","PharmGKB_nodes",conn);
		     
		      
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
