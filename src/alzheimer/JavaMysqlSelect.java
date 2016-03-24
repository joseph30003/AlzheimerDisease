package alzheimer;

import java.sql.*;
 
/**
 * A Java MySQL SELECT statement example.
 * Demonstrates the use of a SQL SELECT statement against a
 * MySQL database, called from a Java program.
 * 
 * Created by Alvin Alexander, <a href="http://devdaily.com" title="http://devdaily.com">http://devdaily.com</a>
 */
public class JavaMysqlSelect
{
 
  public static void main(String[] args)
  {
    try
    {
      // create our mysql database connection
      
      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
      
      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
       
      // our SQL SELECT query. 
      // if you only need a few columns, specify them by name instead of using "*"
      String query = "SELECT Accession_Id,Name,External_Vocabulary FROM pharmGKB_diseases";
 
      // create the java statement
      Statement st = conn.createStatement();
       
      // execute the query, and get a java resultset
      ResultSet rs = st.executeQuery(query);
       
      // iterate through the java resultset
      while (rs.next())
      {
        
        String A_ID = rs.getString("Accession_Id");
        String Name = rs.getString("Name");
        String EV = rs.getString("External_Vocabulary");
         
        // print the results
        if( !EV.equals("null")) {
        	
        System.out.format("%s, %s, %s\n", A_ID, Name, EV);}
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