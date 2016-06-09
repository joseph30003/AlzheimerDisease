package gene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class GeneSynonyms {
	
public static String term_split (String str){
		
		if (str.contains("\"")) { 
			String[] arr=str.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			for(int i=0;i<arr.length;i++){
				arr[i]=arr[i].replace("\"", "");
			}
					 return String.join("&",arr);
					
				}
			else return str;

}
	
	
public static void main(String[] args)
	  {
	    try
	    {
	      // create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://localhost:3306/Alzheimer?autoReconnect=true&useSSL=false";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	       
	      // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "select Name,Symbol,Alternate_Names,Alternate_Symbols from pharmGKB_genes";
	 
	      // create the java statement
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO gene_syn(c_name,syn) VALUES(?,?)");
	       
	      // iterate through the java resultset
	      while (rs.next())
	      {
	        
	       
	        String Symbol = rs.getString("Symbol");
	        String AN = term_split(rs.getString(3));
	        String AS = term_split(rs.getString(4));
	         
	        String[] AN_arr = AN.split("&");
	        String[] AS_arr = AS.split("&");
	        // print the results
	        
	        for (String str : AN_arr){
            pst_user.setString(1, Symbol);
            pst_user.setString(2, str);
            
            pst_user.execute();
	        }
	        
	        for (String str : AS_arr){
	            pst_user.setString(1, Symbol);
	            pst_user.setString(2, str);
	            
	            pst_user.execute();
		        }
	        
	        }
	    
	      st.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
	  }	
	
	
	

}
