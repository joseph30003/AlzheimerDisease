package result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.PreparedStatement;

public class Normalize {

	
	public static String raw_name(int id,Connection conn) {
		String result=null;
		try{
			  	      
		      String query = "select name,type from GPKGDF_nodes where id="+id;
			  ResultSet rs = conn.createStatement().executeQuery(query);
				  
			     
				while(rs.next()){
					result=rs.getString(1)+","+rs.getString(2);
								 
					
					
				}
		      
		     		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }
	
	return result;
	}
	public static String get_Pname(String UMID,Connection conn){
		String result=null;
		try{
			  	      
		      String query_1 = "select prefer_name from Metamap where ID=\""+UMID+"\"";
			  ResultSet rs_1 = conn.createStatement().executeQuery(query_1);
			  String query_2 = "select prefer_name from DrugBank_disease where UMLS=\""+UMID+"\"";
			  ResultSet rs_2 = conn.createStatement().executeQuery(query_2);  
			     
				if(rs_1.next()){
					result=rs_1.getString(1);	 
					
					
				}else if(rs_2.next()){
					result=rs_2.getString(1);
				}
		      
		     		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }
	
	return result;
	}
	public static String get_UMLS(String name,Connection conn){
		
		Pattern patt = Pattern.compile("C[0-9]+");
    	Matcher m = patt.matcher(name);
    	 
        // if we find a match, get the group 
        if (m.find())
        {
          
        	return get_Pname(name,conn);
 
        }else return name;
		
	}
	
	public static void Update(int id,String name,Connection conn) {
		try
	    { 
		  PreparedStatement node_update =  (PreparedStatement) conn.prepareStatement("update networkresult set prefer_name = ?  where id = ?");
		  node_update.setInt(2, id);
		  node_update.setString(1,name);
		 		  node_update.executeUpdate();
		}
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	public static void main(String[] args) {
		try{
			  String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      
		      
		      String query = "select id from networkresult";
			  ResultSet rs = conn.createStatement().executeQuery(query);
				  
			     
				while(rs.next()){
					int id=rs.getInt(1);
					String[] result=raw_name(id,conn).split(",");
					Update(id,get_UMLS(result[0],conn),conn);
					System.out.println(get_UMLS(result[0],conn));
						 
					
					
				}
		      
		      conn.close();
		     
		      
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
