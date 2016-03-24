package database;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import alzheimer.String_handler;

public class PheWAS extends NetTable{
	
public PheWAS(Connection con) {
		super("PheWAS", con);
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
	
      
    String query="select snp,phewas_phenotype,gene_name from phewas";
    
   
    List<String> relations = new ArrayList<String>();
    List<String> types = new ArrayList<String>();
    
    Statement st = conn.createStatement();
     
    // execute the query, and get a java result set
    ResultSet rs = st.executeQuery(query);
    while (rs.next())
    {
  	 
  	  
  	  
  	  if(!rs.getString(1).equals("")){
  		
  		 relations.add(rs.getString(1));
  		 types.add("snp");
      	
      	 }
       if(!rs.getString(2).equals("")){
      	 relations.add(rs.getString(2).replaceAll("\"", ""));
      	 types.add("disease");
      	 
      	 }
       if(!(rs.getString(3)==null)){
      	 relations.add(rs.getString(3));
      	 types.add("gene");
      	 
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
