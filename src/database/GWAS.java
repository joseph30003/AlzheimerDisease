package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import alzheimer.String_handler;

public class GWAS extends NetTable {
		
public GWAS(Connection con) {
		super("GWAS", con);
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
	   
    String query="select PUBMEDID,DISEASE_TRAIT,UPSTREAM_GENE_ID,DOWNSTREAM_GENE_ID,SNP_GENE_IDS,MAPPED_GENE,SNPS from GWAS";
    
   
    List<String> relations = new ArrayList<String>();
    List<String> types = new ArrayList<String>();
    
    Statement st = conn.createStatement();
     
    
    ResultSet rs = st.executeQuery(query);
    while (rs.next())
    {
  	 
  	  String gene=Gene_handler(rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)).replaceAll(" ", "");
  	  if(!rs.getString(2).equals("")){
  		
  		 relations.add(rs.getString(2));
  		 types.add("disease");
      	 
      	 }
       if(!gene.equals("")){
      	 relations.add(gene);
      	 types.add("gene");
      	
      	 }
       if(!rs.getString(7).equals("")){
      	 relations.add(rs.getString(7));
      	 types.add("snp");
      	 
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
	
	public  void relations_seperate(List<String> relations,List<String> types) {
		
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
	
	public  void relations_update(String node1,String type1,String node2,String type2) {
		
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
	
	public String Gene_handler(String gene1,String gene2) {
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
	public String Gene_handler(String gene1,String gene2,String gene3,String gene4) {
	    
	    if( !gene3.equals("") ){
	    	return gene3.replaceAll("[,;]", "#");
	    }else if( !gene1.equals("") && !gene2.equals("")){
	    	return gene1+"#"+gene2;
	    }else{
	    	return gene4.replaceAll("[-,]","#");
	    }
		
		
	}
	
	
	
	

}
