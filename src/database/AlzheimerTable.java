package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class AlzheimerTable {
	
	
public static String update(NetTable sr,NetTable ne,int level,int id){
	
	Node sr_node=sr.getNode(id);
	int srId=ne.nodeInsert(sr_node.name, sr_node.type,Integer.toString(level));
	String rs=sr.findEdge(id);
	String[] edges=rs.split(",");
	Node node;
	for(int i=0;i<edges.length;i++){
	node=sr.getNode(Integer.parseInt(edges[i]));
	int newid=ne.nodeInsert(node.name, node.type, Integer.toString(level+1));
	ne.edgeInsert(srId, newid);
	}
	
	return rs;
	
	
}
	
	
	
	
	
	
	
public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      NetTable sr=new NetTable("FDAPharmGKBPheWASKEGGGWASDrugBank",conn);
	      NetTable n=new NetTable("AlzheimerAll",conn);
	      n.build();
	      sr.Report();
	      int id=sr.findNode("C0002395", "disease");
	      List<Integer> allnodes = new ArrayList<Integer>();
	      allnodes.add(id);
	      List<Integer> stack=new ArrayList<Integer>();
	      stack.add(id);
	      int level=0;
	      while(!stack.isEmpty()){
	      
	      String edges="";	  
	    	 
	      for(int nn:stack){
	     	  
	      edges=edges+update(sr,n,level,nn);
	      }
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
	      conn.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	
	
	}	
	
	
	
	

}
