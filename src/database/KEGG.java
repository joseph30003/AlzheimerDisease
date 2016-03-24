package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import alzheimer.String_handler;

public class KEGG extends NetTable {
	
public KEGG(Connection con) {
		super("KEGG", con);
		// TODO Auto-generated constructor stub
	}

public void build(){
	dropNet();
	createNet();
	relations_extrator();
}

public void relations_extrator()
{
	try{
	
    String query="select Entry,Name,Gene,Drug,Reference from KEGG_Disease order by Entry";
    
   
    List<String> relations = new ArrayList<String>();
    List<String> types = new ArrayList<String>();
    
    Statement st = conn.createStatement();
     
    // execute the query, and get a java resultset
    ResultSet rs = st.executeQuery(query);
    while (rs.next())
    {
  	    	  
  	  if(!rs.getString("Name").equals("")){
  		 String[] dis=rs.getString("Name").split("#");
  		 String disease="";
  		 for(int i=0;i<dis.length;i++){
  			 disease=disease+dis[i]+"["+rs.getString("Entry")+"]"+"#";
  			
  		 }
  		 relations.add(disease);
  		System.out.println("disease");
  		 types.add(disease);
      
      	 }
       if(!rs.getString("Gene").equals("")){
      	 relations.add(rs.getString("Gene"));
      	System.out.println(rs.getString("Gene"));
      	 types.add("Gene");
      	
      	 }
       if(!rs.getString("Drug").equals("")){
      	 relations.add(rs.getString("Drug"));
      	 types.add("Drug");
      	 System.out.println(rs.getString("Drug"));
      	 }
       
       relations_seperate(relations,types);
       
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
    e.printStackTrace();
  }
	
	
	
}

	public void relations_seperate(List<String> relations,List<String> types) {
		
	    if(relations.size() >=2){
	    try{
	    	 
	    	        for (int i=0; i<relations.size();i++){
	    			for (int j=i+1; j<relations.size();j++){
	    				String Node1=String_handler.Nodes_sperate(relations.get(i));
	    				String type1=types.get(i);
	    				String Node2=String_handler.Nodes_sperate(relations.get(j));
	    				String type2=types.get(j);
	    				relations_update(Node1,type1,Node2,type2);
	    			}
	    			}
	    	}
	    	catch (Exception e)
	        {
	          System.err.println("Got an exception! ");
	          System.err.println(e.getMessage());
	          e.printStackTrace();
	        }
			
		}	
	
	}
	
	public void relations_update(String node1,String type1,String node2,String type2) {
		
	   	String[] node_1=String_handler.String_spliter(node1);
	   	String[] node_2=String_handler.String_spliter(node2);
	   	int id_1;
	   	int id_2;
	   	for (int i=0; i<node_1.length;i++){
	   		
	   		id_1=nodeInsert(node_1[i],type1);
	   		
			for (int j=0; j<node_2.length;j++){
			
			id_2=nodeInsert(node_2[j],type2);	
			
			edgeInsert(id_1,id_2);	
				
				
			}
	   	}	
	
	}
	
	
	
	

}
