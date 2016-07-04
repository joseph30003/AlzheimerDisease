package gene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class DiseaseSynonyms {
	
public static String term_split (String str){
		
		if (str.contains("\"")) { 
			String[] arr=str.split("\",\"");
			for(int i=0;i<arr.length;i++){
				arr[i]=arr[i].replace("\"", "");
			}
					 return String.join("&",arr);
					
				}
			else return str;

}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
	    {
	      // create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer?autoReconnect=true&useSSL=false";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	       
	      // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "select Name,Alternate_Names,Accession_Id from PharmGKB_diseases";
	 
	      // create the java statement
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO DiseaseSynonyms(Synonyms,Disease,Accession_Id) VALUES(?,?,?)");
	       
	      // iterate through the java resultset
	      while (rs.next())
	      {
	        
	        if(rs.getString(2).length()>0){
	        String disease = rs.getString("Name");
	        String AN = term_split(rs.getString(2));
	        String AI = rs.getString(3);
	        //System.out.print(disease);
	         
	        String[] AN_arr = AN.split("&");
	        
	        // print the results
	        
	        for (String str : AN_arr){
	       // System.out.print(str);
            pst_user.setString(1, str);
            pst_user.setString(2, disease);
            pst_user.setString(3, AI);
            pst_user.execute();
	        }
	        
	       // System.out.print("\n");
	        
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


