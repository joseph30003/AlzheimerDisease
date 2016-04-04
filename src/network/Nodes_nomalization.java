package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class Nodes_nomalization {
	public static void createColumn(String table_name,Connection conn) {
        String myTableName = "ALTER TABLE "+table_name+" ADD reference_name VARCHAR(100) AFTER name";  
        
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(myTableName);
            }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	
	public static void updateReference(String table,String reference,Connection conn) {
		try
	    {
	    
	      
	      PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table+" set reference_name = ? where name = ?");
		  String query = "select id,name from "+reference;
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				String id=rs.getString(1);
				String node=rs.getString(2);
				
				System.out.println(id+" "+node);
					  node_update.setString(1, id);
					  node_update.setString(2, node);
				      node_update.executeUpdate();
				
				
			}
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	public static boolean isRealUMLS(String UMLS){
		boolean rs=true;
		String[] pool={"C1457887","C0012634","C3687832","C1273517","C1397014","C0030193","C1444656","C0278140"};
		for(String s: pool){
			if (s.equals("UMLS")) rs=false;
		}
		
		return rs;
		
	}
	
	public static void metamapUpdate(Connection conn) {
		
		String[] sources={"KEGG","GWAS","PheWAS","FDA","PharmGKB"};
		
		try
	    {
	      
		 for(String table:sources){
			 
			 String query_search = "select id from "+table+"_nodes where type = 'disease' or type='drug'";
			 ResultSet rs1 = conn.createStatement().executeQuery(query_search);
			 while(rs1.next()){
				int id=rs1.getInt(1);
				 String query = "select UMLS,source,source_id,score from MetaTable where score>900 and source=\""+table+"\" and source_id="+id+" order by score desc";
				 //System.out.println(query);
				 ResultSet rs = conn.createStatement().executeQuery(query); 
				 while(rs.next()){
					 String UMLS =	rs.getString(1);		
					 if(isRealUMLS(UMLS)){
					   				
						
						//System.out.println(table);
						PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update "+table+"_nodes set reference_name = ? where id = ?");
						
							  node_update.setString(1, UMLS);
							  node_update.setInt(2, id);
						      node_update.executeUpdate();
						   break;  
						}
				 
			 }
			 
		 }
	       
		 }
			
			
	      
		  //String query = "select a.UMLS,a.source,a.source_id from MetaTable a inner join (select source,source_id,max(score) max_score from MetaTable group by source,source_id ) b on a.source=b.source and a.source_id=b.source_id and a.score=b.max_score and a.score>=900";
           
	      
	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
	public static void main(String[] args)
	  {
		
		
	    try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      metamapUpdate(conn);
	      
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	  }
	
	
	
}
