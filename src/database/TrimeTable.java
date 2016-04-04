package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;

public class TrimeTable {
	
	public static void swap(Node[] nodes,int i, int j){
		Node n=nodes[i];
		nodes[i]=nodes[j];
		nodes[j]=n;
	}
	
	public static String findDuplication(Node[] arr){
		  String rs="";
		  int k=0;
		  for(int i=0 ; i<arr.length;){
			  k=i+1;
			  for(int j=i+1; j<arr.length; j++){
				   if(arr[i].same(arr[j])){
					 //System.out.println(arr[i].id+" equals "+arr[j].id); 
					 rs=rs+arr[j].id+",";
					 swap(arr,j,k);
					  k++; 
					 }
			  }			  
			i=k;  
			  
		  }
		
		return rs;	
			
		}
	
	public static boolean contains(int[] arr, int targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}
	
	public static void trimer(String net,Connection conn){
		
		NetTable o=new NetTable(net,conn);
	      NetTable n=new NetTable(net+"_trimed",conn);
	      n.build();
	      Node[] oldnodes=o.getNodes();
	      HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
	      //quickSort(oldnodes,0,oldnodes.length-1);
	      int id;
	      for(Node nn:oldnodes){
	    	      if(nn.reference_name==null) id=n.nodeInsert(nn.name, nn.type);
	    		  else id=n.nodeInsert(nn.reference_name, nn.type);
	    		  hmap.put(nn.id, id);
	    
	      }
	      for(Node nn:oldnodes){
	      int node1 = nn.id;
	      String[] edges=o.findEdge(node1).split(",");
	       
	      for(String ed:edges){
	      int node2=Integer.parseInt(ed);  
	      n.edgeInsert(hmap.get(node1), hmap.get(node2));
	      }
	      
	      }
		
		
	}
	
	
	
	public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      
	      String[] sources={"KEGG","GWAS","PheWAS","PharmGKB","FDA"};
	      for(int i=0;i < sources.length; i++){
	      trimer(sources[i],conn);
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
