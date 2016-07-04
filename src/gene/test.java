package gene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class test {
	
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
	
	
public static void main(String[] args){	
	try
    {
      // create our mysql database connection
      
      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer?autoReconnect=true&useSSL=false";
      
      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
       
      // our SQL SELECT query. 
      // if you only need a few columns, specify them by name instead of using "*"
      String query = "select Name,Alternate_Names,Accession_Id from PharmGKB_diseases where Accession_Id=\"PA443319\"";
 
      // create the java statement
      Statement st = conn.createStatement();
       
      // execute the query, and get a java resultset
      ResultSet rs = st.executeQuery(query);
      //PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO DiseaseSynonyms(Synonyms,Disease,Accession_Id) VALUES(?,?,?)");
       
      // iterate through the java resultset
      while (rs.next())
      {
        
       
        //String disease = rs.getString("Name");
        String AN = term_split(rs.getString(2));
        //String AI = rs.getString(3);
        System.out.println(rs.getString(2));
        System.out.println(AN);
         
        String[] AN_arr = AN.split("&");
       for(String str: AN_arr){
       System.out.println(str);
        // print the results
        
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
