package openNP;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

import opennlp.tools.parser.Parse;

public class IndicationNLP {

	public static void createNet(Connection conn) {
        String dropNLP="DROP TABLE IF EXISTS indication_NLP";
       
		String createTable = "CREATE TABLE indication_NLP (" 
            + "id int,"  
            + "indication text," 
            + "NLP text,"
            + "FOREIGN KEY (id) REFERENCES DrugBank_drug (id))";  
        
                      
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(dropNLP);
            
            st.executeUpdate(createTable);
            
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public static void updateTable(int id, String indication, String NLP,Connection conn) {
		try{
		    
		    PreparedStatement node_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO indication_NLP(id,indication,NLP) VALUES(?,?,?)");
		    
			
			  
		     
			
					  node_insert.setInt(1, id);
					  node_insert.setString(2, indication);
					  node_insert.setString(3, NLP);
				      node_insert.executeUpdate();
				
				      
				
		}
		catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
			
	}
	
	
	public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      createNet(conn);
	      String query = "select id,indication from DrugBank_drug where indication is not NULL";
		  ResultSet rs = conn.createStatement().executeQuery(query);
	      while(rs.next()){
	    	  Parser pp = new Parser(rs.getString(2));
	    	  for(Parse t : pp.result){
	    	  updateTable(rs.getInt(1),rs.getString(2),t.getParent().getParent().getParent().getCoveredText(),conn);
	    	  }
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
