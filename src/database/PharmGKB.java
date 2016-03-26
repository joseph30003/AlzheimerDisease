package database;

import java.sql.Connection;
import java.sql.Statement;

public class PharmGKB extends NetTable {

    		
	public PharmGKB(Connection con) {
		super("PharmGKB", con);
		// TODO Auto-generated constructor stub
	}
    
	public void build(){
		//String tmpTable="GKBtmp";
		dropNet();
		createNet();
		
	}
	
	
   public void copyTable(String tmp){
	    dropTable(tmp);
	    String Table= "CREATE TABLE "+tmp+" (" 
                   + "node1 int,"  
                   + "node2 int)";
	    String Insert="INSERT "+tmp+" SELECT node1,node2 FROM GKB_edges";
       try {
           
       	Statement st = conn.createStatement();
           //The next line has the issue
           
           if(!Table.isEmpty()){
           
			st.executeUpdate(Table);
			st.executeUpdate(Insert);
           }else{
           	System.out.println("no sql statements!");
           }
           System.out.println("network  "+tmp+" Created");
           st.close();
       }
       catch (Exception e ) {
       	System.err.println("Got an exception! ");
           System.err.println(e.getMessage());
       }
	
	
   }
	
	
	
	
	
	
}
