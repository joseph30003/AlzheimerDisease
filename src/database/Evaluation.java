package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluation {

	public static String update(NetTable sr,int level,int id){
		
			
		String rs=sr.findEdge(id);
				
		return rs;
		
		
	}
		
		
		
		
		
		
		
	public static void main(String[] args) {
			try
		    {
		    
		      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
		      NetTable sr=new NetTable("FDAPharmGKBPheWASKEGGGWAS",conn);
		      //NetTable n=new NetTable("Alzheimer",conn);
		      //n.build();
		      //sr.Report();
		      int id=sr.findNode("C0002395", "disease");
		      List<Integer> allnodes = new ArrayList<>();
		      allnodes.add(id);
		      List<Integer> stack=new ArrayList<Integer>();
		      stack.add(id);
		      int level=0;
		      while(level<2){
		      
		      String edges="";	  
		    	 
		      for(int nn:stack){
		     	  
		      edges=edges+update(sr,level,nn);
		      }
		      //System.out.println(edges);
		      stack.clear();
		      String[] edge=edges.split(",");
		      for(String e:edge){
		      int eid=Integer.parseInt(e);
		      if(!allnodes.contains(eid)) {
		    	  allnodes.add(eid);
		    	  stack.add(eid);
		      }
		        
		      }
		      
		       
		      level++;
		      }
		      
		     
		   Set<Integer> hs = new HashSet<>();
		  
		   hs.addAll(allnodes);
		   
		  
		      
		      
		      
		      System.out.println(allnodes.size());
		      System.out.println(hs.size());
		      
		      conn.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      e.printStackTrace();
		    }
		
		
		}
	
	
	
	
}
