package gene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Gene {
  
	public String name;
	public int ID;
	private Connection conn;
	
	public Gene(int id,Connection con){
		conn=con;
		ID=id;
		name=Parse(ID);
	}
	
	public Gene(String id,Connection con){
		conn=con;
		ID=toID(id);
		name=Parse(ID);
	}
    
	public String Parse(int id){
		
		String result=null;
		if(!(id == -1)){
		try{
    		Statement st = conn.createStatement();    
     		String query= "select Symbol from GeneInfo where GeneID=\""+id+"\"";
     		ResultSet rs = st.executeQuery(query);
     		
     		if(rs.next()){
     			result=rs.getString(1).toUpperCase();     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
		}
		else System.out.println("wrong GeneID no result");
		return result;
	}
	
	public int toID(String id){
		 String str = id.replaceAll("[^-?0-9]+", "");
		 if(str.equals("")) return -1;
		 else return Integer.parseInt(str);		
	}
	
	
	
	
}
