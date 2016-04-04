package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public String Handler(String in){
    String out=in.replaceAll("\\[.*?\\] ?", "").replaceAll("[\\$#]", " ").replaceAll("\\s+$", "");
	return out;
    }
	
 public String geneHandler(String in){
	 String out=HSAfinder(in);
	 if(out==""){
		 return Handler(in);
	 }else return out;
 }

 public String HSAfinder(String in){
 	in=in.toUpperCase();
 	Pattern p = Pattern.compile("\\[HSA:(.*?)\\]");
 	Matcher m = p.matcher(in);
		String out = "";
		if(m.find()) {
		    out = m.group(1);
		}
 	return out;
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
  		
  		 String disease=Handler(rs.getString("Name"));
  		
  		 relations.add(disease);
  		 //System.out.println(disease);
  		 types.add("disease");
      
      	 }
       if(!rs.getString("Gene").equals("")){
    	 
    	 String[] genes = rs.getString("Gene").split("#");
    	 for(String g: genes){
    		 
    		 String[] gene = geneHandler(g).split("\\$");
    		 for(String g1 : gene){
    		 //System.out.println(g1);
    		 relations.add(g1);
    	     types.add("gene");}
        	 }

      	 }
       if(!rs.getString("Drug").equals("")){
    	 String[] drugs = rs.getString("Drug").split("#");
      	 for(String d: drugs){  
    	 String drug=Handler(d);
    	 //System.out.println(drug);  
      	 relations.add(drug);
      	 types.add("drug");
      	 }      	 
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
		    	 
  	        for (int i=0; i<relations.size();i++){
  			for (int j=i+1; j<relations.size();j++){
  				String Node1=relations.get(i);
  				String type1=types.get(i);
  				String Node2=relations.get(j);
  				String type2=types.get(j);
  				relations_update(Node1,type1,Node2,type2);
  			}
  			}
  
		
	}	
	
	}
	
	
	public void relations_update(String node1,String type1,String node2,String type2) {
		
		int id_1;
	   	int id_2;
	   	 		
	    id_1=nodeInsert(node1,type1);
	   	id_2=nodeInsert(node2,type2);	
		edgeInsert(id_1,id_2);	
	
	}
	

}
