package alzheimer;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;


public class Relation_collect {
	
	
	public static void relations_seperate(List<String> relations,List<String> types,String tags,String PMID,Connection conn) {
		
    if(relations.size() >=2){
    	try{
    		for (int i=0; i<relations.size();i++){
    			for (int j=i+1; j<relations.size();j++){
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO Relations(node1_name,node1_type,node2_name,node2_type,tag,PMID) VALUES(?,?,?,?,?,?)");
    	            pst_user.setString(1, relations.get(i).toString());
    	            pst_user.setString(2, types.get(i).toString());
    	            pst_user.setString(3, relations.get(j).toString());
    	            pst_user.setString(4, types.get(j).toString());
    	            pst_user.setString(5, tags);
    	            pst_user.setString(6, PMID);
    	            pst_user.execute();
    				
    			}
    			}
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
		
	}

	}
	
	
	public static void main(String[] args)
	  {
	    try
	    {
	      // create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String query="select Name,Gene,Drug,Reference from KEGG_Disease";
	      
	      String tags="KEGG",PMID=null;
	      List<String> relations = new ArrayList<String>();
	      List<String> types = new ArrayList<String>();
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java result set
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	 
	    	  if(rs.getString("Name")!=null){
	        	 relations.add(rs.getString("Name"));
	        	 types.add("disease");
	        	 }
	         if(rs.getString("Gene")!=null){
	        	 relations.add(rs.getString("Gene"));
	        	 types.add("gene");
	        	 }
	         if(rs.getString("Drug")!=null){
	        	 relations.add(rs.getString("Drug"));
	        	 types.add("drug");
	        	 }
	         
	         PMID=rs.getString("Reference");
	         relations_seperate(relations,types,tags,PMID,conn);
	         relations.clear();
	         types.clear();
	         }
	      //return null;
	      
	      st.close();
	    
	      
	      
	      
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	    }
	  }
	
}
