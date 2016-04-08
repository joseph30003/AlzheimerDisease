package database;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


import com.mysql.jdbc.PreparedStatement;

public class AlzheimerRenew {
	
	
	
    public static String findUMLS(String term, Connection conn){
    	String rsUMLS = null;
    	String Name=term.replaceAll("[^a-zA-Z]", "");
    	try
	    {
	      String query_search = "select Alters_UMLS.UMLS from Alters,Alters_UMLS where Alters.id = Alters_UMLS.id and Alters_UMLS.UMLS is not NULL "
	      		+ "and Alters.Alters=\""+Name+"\"" ;
	     //System.out.println(query_search);
	     
	     ResultSet rs = conn.createStatement().executeQuery(query_search);
	     
	     
	     if(rs.next()){
	    	 rsUMLS=rs.getString(1);
	     }
	     
	     	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
    	
    	
    	
    	
    	return rsUMLS;
    }
	
    
    public static void UpdateRef(String text,int id,Connection conn) {
		try
	    {
	    
				PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update AlzheimerAll_nodes set reference_name = ? where id = ?");
			
			    node_update.setString(1, text);
			    node_update.setInt(2, id);
				node_update.executeUpdate();
				
	   }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
    
    
    
	public static void main(String[] args) {
		
		
		 try
		    {
		     String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		     Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		     
			 
			 
			String query_search = "select id,name from AlzheimerAll_nodes where type=\"disease\"" ;
		     System.out.println(query_search);
		     
		    ResultSet rs = conn.createStatement().executeQuery(query_search);
		     
		     
		     while(rs.next()){
		    	 int id=rs.getInt(1);
		    	 String UMLS = findUMLS(rs.getString(2),conn);
		    	 if(UMLS!=null) UpdateRef(UMLS,id,conn);
		     }
		     	      
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      e.printStackTrace();
		    }
		

	}
	
	
	

}
