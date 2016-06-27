package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.PreparedStatement;

public class GKBrawUMLS {
	
	 public static String findUMLS(String in){
	    	
	    	Pattern p = Pattern.compile("\\\"UMLS:(.*?)\\\"");
	    	Matcher m = p.matcher(in);
			String out=null;//in.replaceAll("\\[.*?\\] ?", "");
			if(m.find()) {
			  out=m.group(1);
			  out=out.substring(0, out.indexOf("("));
			}
	    	return out;
	    }
	 
	 public static void dropTable(Connection conn){
		    String table="pharmGKB_rawUMLS";
			String dropTable="DROP TABLE IF EXISTS "+table;
	        try {
	            Statement st = conn.createStatement();
	        	st.executeUpdate(dropTable);
	                                   
	            System.out.println("Table  "+table+" droped");
	            st.close();
	        }
	        catch (Exception e ) {
	        	System.err.println("Got an exception! ");
	            System.err.println(e.getMessage());
	        }
	        
			
		}
		
	public static void createTable(Connection conn) {
	        
			String Table= "CREATE TABLE pharmGKB_rawUMLS ("
	    				+ "Accession_Id VARCHAR(11) NOT NULL,"
	    				+ "Name VARCHAR(100) NULL DEFAULT NULL,"
	    				+ "Type VARCHAR(20),"
	    				+ "UMLS VARCHAR(20) NULL DEFAULT NULL,"
	    				+ "PRIMARY KEY (Accession_Id))";
	           
	             
	       try {
	            
	        	Statement st = conn.createStatement();
	            //The next line has the issue
	            
	            if(!Table.isEmpty()){
	            
				st.executeUpdate(Table);
	            }else{
	            	System.out.println("no sql statements!");
	            }
	            System.out.println("network  pharmGKB_rawUMLS Created");
	            st.close();
	        }
	        catch (Exception e ) {
	        	System.err.println("Got an exception! ");
	            System.err.println(e.getMessage());
	        }
	    }
	
	
public static void Insert(String Aid,String Name,String UMLS,String type,Connection conn) {
		
	    
    	try{
    			  	
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO pharmGKB_rawUMLS (Accession_Id,Name,UMLS,Type) VALUES(?,?,?,?)");
    	            pst_user.setString(1, Aid);
    	            pst_user.setString(2, Name);
    	            if(UMLS == null) pst_user.setNull(3,java.sql.Types.CHAR);
    	            else 
    	            	pst_user.setString(3, UMLS);
    	            pst_user.setString(4, type);
    	            pst_user.execute();
    	            pst_user.close(); 
    		    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
	}	
	
	 public static void main(String[] args){
	 
		 
		 try{
				// create our mysql database connection
			      
			      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
			      
			      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
			      Statement st = conn.createStatement(); 
			      dropTable(conn);
			      createTable(conn);
			      String[] type={"disease","drug"};
			      for(String t:type){
			      String query="select Accession_Id,Name,External_Vocabulary from PharmGKB_"+t+"s";
			      
			      ResultSet rs = st.executeQuery(query);
		     		
		     		while(rs.next()){
		     		String ex=rs.getString(3);
		     		Insert(rs.getString(1),rs.getString(2),findUMLS(ex),t,conn);
		     		
		     		}
			      } 
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
