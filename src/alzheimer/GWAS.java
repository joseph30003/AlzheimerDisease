package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class GWAS {

public static void Edges_update(int node1,int node2,String entry,Connection conn) {
		
	    
    	try{
    		
    			
    				
    				PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO GWAS_edges(node1,node2,PUBMEDID) VALUES(?,?,?)");
    	            pst_user.setInt(1, node1);
    	            pst_user.setInt(2, node2);
    	            pst_user.setString(3, entry);
    	            pst_user.execute();
    				
    			
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
          e.printStackTrace();
        }
	}	
    	
	public static int Nodes_update(String name,String type,Connection conn) {
    		
    	    int id=1;
    	 
    		
        	try{
        		
        	    
        		
        		String query_node= "select id from GWAS_nodes where name=\""+name+"\"";
        		ResultSet rs_node = conn.createStatement().executeQuery(query_node);
        		
        		if(rs_node.next()){
        			id=rs_node.getInt(1);
        			
        		}else{
        			
        			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO GWAS_nodes(name,type) VALUES(?,?)");
    	            pst_user.setString(1, name);
    	            pst_user.setString(2, type);
    	           
    	            pst_user.execute();
    	            
    	            ResultSet rs_node_new = conn.createStatement().executeQuery(query_node);
    	            if(rs_node_new.next()){
            			id=rs_node_new.getInt(1);
            			
            		}
    	            
        		}
        		
        		
        				
        	}
        	catch (Exception e)
            {
              System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
              e.printStackTrace();
            }
        	return id;

	}
	
	public static void relations_seperate(List<String> relations,List<String> types,String entry,Connection conn) {
		
	    if(relations.size() >=2){
	    try{
	    	 
	    	        for (int i=0; i<relations.size();i++){
	    			for (int j=i+1; j<relations.size();j++){
	    				String Node1=String_handler.Nodes_sperate(relations.get(i));
	    				String type1=types.get(i);
	    				String Node2=String_handler.Nodes_sperate(relations.get(j));
	    				String type2=types.get(j);
	    				relations_update(Node1,type1,Node2,type2,entry,conn);
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
	
	public static void relations_update(String node1,String type1,String node2,String type2,String entry,Connection conn) {
		
	   	String[] node_1=String_handler.String_spliter(node1);
	   	String[] node_2=String_handler.String_spliter(node2);
	   	int id_1;
	   	int id_2;
	   	for (int i=0; i<node_1.length;i++){
	   		
	   		id_1=Nodes_update(node_1[i],type1,conn);
	   		
			for (int j=0; j<node_2.length;j++){
			
			id_2=Nodes_update(node_2[j],type2,conn);	
			
			Edges_update(id_1,id_2,entry,conn);	
				
				
			}
	   	}	
	
	}
	
	public static String Gene_handler(String gene1,String gene2) {
	    String result="";
	    if( !gene1.equals("") || !gene2.equals("")){
		String[] gene_1=gene1.split(",");
		String[] gene_2=gene2.split(",");
		List<String> gene = new ArrayList<String>();
		for(int i=0;i<gene_1.length;i++){
			
		gene.add(gene_1[i]);
		
		}
		
		for(int i=0;i<gene_2.length;i++){
		if(!gene.contains(gene_2[i])){
			gene.add(gene_2[i]);
		}	
		}
		
		for(int i=0;i<gene.size();i++){
			if(!gene.get(i).equals(""))
			result=gene.get(i)+"#"+result;
		}
		
		gene.clear();}
		return result;
		
	}
	
	
	public static void main(String[] args)
	  {
		try{
		// create our mysql database connection
	      
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String query="select PUBMEDID,DISEASE_TRAIT,REPORTED_GENES,MAPPED_GENE,SNPS from GWAS";
	      
	     
	      List<String> relations = new ArrayList<String>();
	      List<String> types = new ArrayList<String>();
	      
	      Statement st = conn.createStatement();
	       
	      // execute the query, and get a java result set
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	    	 
	    	  String gene=Gene_handler(rs.getString(3),rs.getString(4));
	    	  if(!rs.getString(2).equals("")){
	    		
	    		 relations.add(rs.getString(2));
	    		 types.add("disease");
	        	 
	        	 }
	         if(!gene.equals("")){
	        	 relations.add(gene);
	        	 types.add("Gene");
	        	
	        	 }
	         if(!rs.getString(5).equals("")){
	        	 relations.add(rs.getString(5));
	        	 types.add("SNP");
	        	 
	        	 }
	         
	         relations_seperate(relations,types,rs.getString(1),conn);
	         
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
	
	
	
}
