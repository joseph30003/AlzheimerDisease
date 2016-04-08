package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;


public class Alter_name {
	
	
	public static void createTable(Connection conn) {
        
		
		String dropTable="DROP TABLE IF EXISTS Alters";
		String dropUMLS="drop table if exists Alters_UMLS";
		String UMLSTable="CREATE TABLE Alters_UMLS ("
				+ "id INT NOT NULL AUTO_INCREMENT,"
				+ "Name VARCHAR(600) NULL DEFAULT NULL,"
				+ "UMLS VARCHAR(100) NULL DEFAULT NULL,"
				+ "PRIMARY KEY (id))";

		String Table="CREATE TABLE Alters (" 
                + "id int,"  
                + "Alters VARCHAR(600),"
                + "foreign KEY (id) references Alters_UMLS (id))";
           
              
        try {
            
        	Statement st = conn.createStatement();
        	st.executeUpdate(dropTable);
        	st.executeUpdate(dropUMLS);
            //The next line has the issue
        	System.out.println("Table  droped");
        
        	st.executeUpdate(UMLSTable);
			st.executeUpdate(Table);
			
          
            System.out.println("network   Created");
           
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	
	
	public static int findNode(String name,Connection conn){
		int id=-1;
    	try{
    		Statement st = conn.createStatement();    
     		String query_node= "select id from Alters_UMLS where Name=\""+name+"\"";
     		ResultSet rs_node = st.executeQuery(query_node);
     		
     		if(rs_node.next()){
     			id=rs_node.getInt(1);     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return id;
	 }
	 public static int nodeInsert(String name,Connection conn) {
			
		    try{
	   		if(findNode(name,conn)==-1){
	    			
	    		      			
	    			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO Alters_UMLS (Name) VALUES(?)");
		            pst_user.setString(1, name);
		          
		            pst_user.execute();
		            pst_user.close();          		
		            
	    		}
	    	    		
	    				
	    	}
	    	catch (Exception e)
	        {
	          System.err.println("Got an exception! ");
	          System.err.println(e.getMessage());
	          e.printStackTrace();
	        }
	    	return findNode(name,conn);

	}    
	
	
	
	
	public static void main(String[] args)
	  {
			
		
	
		try{
			// create our mysql database connection
		      
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      createTable(conn);
		      PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO Alters(id,Alters) VALUES(?,?)");
		      String query="select Name,Alternate_Names from pharmGKB_diseases";
		      
		     Statement st = conn.createStatement();
		       
		      // execute the query, and get a java result set
		      ResultSet rs = st.executeQuery(query);
		      while (rs.next())
		      {
		       System.out.println(rs.getString(1));
		       int id=nodeInsert(rs.getString(1),conn);
		       if(!rs.getString(2).isEmpty()){
		       String[] alters=rs.getString(2).split("\",");
		       for(String a:alters){
		    	   
		    	   
   	            pst_user.setInt(1, id);
   	            pst_user.setString(2, a.replaceAll("[^a-zA-Z]", ""));
   	            pst_user.execute();
   	            
		    	   
		    	   
		    	   System.out.println(a.replaceAll("[^a-zA-Z]", ""));
		       }
		       }
		      }
		      pst_user.close();
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
