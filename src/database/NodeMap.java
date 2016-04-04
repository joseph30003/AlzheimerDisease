package database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import com.mysql.jdbc.PreparedStatement;
public class NodeMap {
	
	String table;
	Connection conn;
	
	
	
	
	public NodeMap(String table, Connection conn2) {
		
		this.table = table;
		this.conn = conn2;
		dropTable();
		createTable();
	}

	public void dropTable(){
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
	
	public void createTable() {
        
		String  Table = "CREATE TABLE "+table+" (" 
                    + "oldId int,"  
                    + "newId int," 
                    + "PRIMARY KEY (oldId))";
          
              
        try {
            
        	Statement st = conn.createStatement();
            //The next line has the issue
            
            if(!Table.isEmpty()){
            
			st.executeUpdate(Table);
            }else{
            	System.out.println("no sql statements!");
            }
            System.out.println("network  "+table+" Created");
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public void put(int oldid,int newid){
		
		try{
	   				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+table+"(oldId,newId) VALUES(?,?)");
		            pst_user.setInt(1, oldid);
		            pst_user.setInt(2, newid);
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
	
	public int getValue(int key){
		int result=0;
		String query="select newId from "+table+" where oldId="+key;
		
		try{
 		    
			Statement st = conn.createStatement();
     		ResultSet rs = st.executeQuery(query);
     		
     		if(rs.next()){
     			result=rs.getInt(1);     			
     		}
     		st.close();
     	}
     	catch (Exception e)
         {
           System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
           e.printStackTrace();
         }
     	return result;
	}
	

}
