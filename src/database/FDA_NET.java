package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;


public class FDA_NET extends NetTable {

	
	
	
	
	
	public FDA_NET(Connection con) {
		super("FDA", con);
		
	}

	public void build(){
		dropNet();
		createNet();
		relations_extrator();
	}
	
	
	public void relations_extrator(){
		
		try{
			 String query="select FDA.`Therapeutic_Area`,FDA.`Drug`,FDA.`Biomarkeri` from FDA;";
		      
		      List<String> relations = new ArrayList<String>();
		      List<String> types = new ArrayList<String>();
		      
		      
		      Statement st = conn.createStatement(); 
		      ResultSet rs = st.executeQuery(query);
		      while (rs.next())
		      {
		    	 
		    	  
		    	  if(!rs.getString(1).equals("")){
		    		
		    		 relations.add(rs.getString(1));
		    		 types.add("disease");
		        	 
		        	 }
		    	  if(!rs.getString(2).equals("")){
			        	 relations.add(rs.getString(2));
			        	 types.add("drug");
			        	 
			        	 }
		    	  
		         if(!rs.getString(3).equals("")){
		        	 if(rs.getString(3).contains(",")){
		        	 String[] gene=rs.getString(3).split(",");
		        	 for(int i=0;i<gene.length;i++){
		        		 relations.add(gene[i]);
			        	 types.add("gene");
		        	 }
		        		 
		        		 
		        	 }else{
		        	 relations.add(rs.getString(3));
		        	 types.add("gene");
		        	 }
		        	 }
		         
		         
		         relations_seperate(relations,types);
		         
		         relations.clear();
		         types.clear();
		         }
		      //return null;
		      
		     st.close();
		 
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