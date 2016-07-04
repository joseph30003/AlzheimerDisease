package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Arrays;


import com.mysql.jdbc.PreparedStatement;

public class PharmGKB extends NetTable {

    		
	public PharmGKB(Connection con) {
		super("PharmGKB", con);
		// TODO Auto-generated constructor stub
	}
 
	
	
	
	
	public void build(){
		String tmpTable="GKBtmp";
		dropNet();
		createNet();
		dataPreparation();
		createTmpEdge(tmpTable);
		relations_extrator(tmpTable);
		
	}
	
	 private String TypeNormalization(String type){
		 if (type.equals("VariantLocation")) return "snp";
		 else return type.toLowerCase();
	 }
	
	
	 public void createTmpEdge(String temTable){
  	   copyTable(temTable);
  	   String query="select id from GKB_nodes where type='haplotype'";   
  	   try {
      	   
           	Statement st = conn.createStatement();
           	ResultSet rs = st.executeQuery(query);
           	while(rs.next()){
      		int id=rs.getInt(1);
      		removeNode(id,temTable);
           	       			
      		}
      
      	    st.close();
          
           }
           catch (Exception e ) {
           	System.err.println("Got an exception! ");
               System.err.println(e.getMessage());
           } 
  
     }	
	
	  public void dataPreparation(){
		  NetTable GKB = new NetTable("GKB",this.conn);
		  GKB.build();
		  try{
				
			    String query="select Entity1_id,Entity1_name,Entity1_type,Entity2_id,Entity2_name,Entity2_type from PharmGKB";
			    
			   
			  
			    
			    Statement st = conn.createStatement();
			     
			    // execute the query, and get a java resultset
			    ResultSet rs = st.executeQuery(query);
			    while (rs.next())
			    {
			    	int node1=GKB.nodeInsert(rs.getString("Entity1_name"), TypeNormalization(rs.getString("Entity1_type")), rs.getString("Entity1_id"));
			    	int node2=GKB.nodeInsert(rs.getString("Entity2_name"), TypeNormalization(rs.getString("Entity2_type")), rs.getString("Entity2_id"));
			  	    GKB.edgeInsert(node1, node2);	  
			  	  
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
	
	
       public void copyTable(String tmpTable){
   	    dropTable(tmpTable);
   	    String Table= "CREATE TABLE "+tmpTable+" (" 
                      + "node1 int,"  
                      + "node2 int)";
   	    String Insert="INSERT "+tmpTable+" SELECT node1,node2 FROM GKB_edges";
          try {
              
          	Statement st = conn.createStatement();
              //The next line has the issue
              
              if(!Table.isEmpty()){
              
   			st.executeUpdate(Table);
   			st.executeUpdate(Insert);
              }else{
              	System.out.println("no sql statements!");
              }
              System.out.println("network  "+tmpTable+" Created");
              st.close();
          }
          catch (Exception e ) {
          	System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
          }
      }  
       
      
       
  
     public void removeNode(int id,String tmpTable){
    	 //System.out.println(id+"____"+findEdge(id,tmpTable)); 
    	 int[] edge=trimEdges(findEdge(id,tmpTable).split(","));
         
    	 for(int i=0;i<edge.length;i++){
    		 for(int j=i+1;j<edge.length;j++){
    			edgeInsert(edge[i],edge[j],tmpTable);
    		 }
    	  }
    	removeEdges(id,tmpTable); 
    	 
       }
    public void removeEdges(int id,String tmpTable){
    	 
    	 try
    	    {
    	     
    	      String query = "delete from "+tmpTable+" where node1=? or node2=?";
    	      PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
    	      preparedStmt.setInt(1, id);
    	      preparedStmt.setInt(2, id);
    	        	      
    	      preparedStmt.execute();
    	      preparedStmt.close(); 
    	     
    	    }
    	    catch (Exception e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	 
    	 
    	 
    	 
     }
          
    private void quickSort(int[] arr, int s, int e) {

   	    if (s < e) {
   	        int pivot = findPivot(arr, s, e);
   	       
   	        // sort left
   	        quickSort(arr, s, pivot - 1);
   	        // sort right
   	        quickSort(arr, pivot + 1, e);
   	    }

   	}

   	private int findPivot(int[] arr, int s, int e) {
   	    int p = arr[e];
   	    int i = s;
   	    for (int j = s; j < e; j++) {
   	        if (arr[j] <= p) {
   	            swap(arr, i, j);
   	            i = i + 1;
   	        }
   	    }
   	    swap(arr, e, i);
   	    return i;
   	}

   	private void swap(int[] arr, int i, int j) {
   	    int t = arr[i];
   	    arr[i] = arr[j];
   	    arr[j] = t;
   	}
    
   private int[] removeDuplicates(int[] A) {
		
	   quickSort(A,0,A.length-1);
	   
   		if (A.length < 2)
			return A;
	 
		int j = 0;
		
	 
		for(int i=1;i<A.length;i++){
			if (A[i] == A[j]) {
				
			} else {
				j++;
				A[j] = A[i];
			}
		}
	 
		int[] B = Arrays.copyOf(A, j + 1);
	 
		return B;
	}
   	
       
    private int[] trimEdges(String[] tmp){
    	 
    	 int[] edges=new int[tmp.length];
    	 for(int i=0;i<tmp.length;i++){
    		 edges[i] = Integer.parseInt(tmp[i]);
    	 }
    	  	 return removeDuplicates(edges);
    	  	   
       }
          
    public String findUMLS(String Aid){
  	    String UMLS=null;     	    
  	    String query="select UMLS from pharmGKB_rawUMLS where Accession_Id =\""+Aid+"\"";
         try {
             
         	Statement st = conn.createStatement();
         	ResultSet rs = st.executeQuery(query);
     		
     		
     		if(rs.next()){
     			UMLS=rs.getString(1);    			
     		}
     	    st.close();
           
         }
         catch (Exception e ) {
         	System.err.println("Got an exception! ");
             System.err.println(e.getMessage());
         } 
        return UMLS; 
   }
       
    
    
    public void relations_extrator(String temTable){
 	   
 	   String query="select node1,node2 from "+temTable;   
 	   try {
     	   
          	Statement st = conn.createStatement();
          	ResultSet rs = st.executeQuery(query);
          	while(rs.next()){
     		int node1=copyNodes(rs.getInt(1));
     		int node2=copyNodes(rs.getInt(2));
     		if(node1>0&&node2>0){
     			edgeInsert(node1,node2);
     		}
     		
          	       			
     		}
     
     	    st.close();
         
          }
          catch (Exception e ) {
          	System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
          } 
 
    }
    
    
       public int copyNodes(int id){
    	  int newid=-1; 
    	  String query="select reference_name,name,type from GKB_nodes where id="+id;
   	    
          try {
        	   
          	Statement st = conn.createStatement();
          	ResultSet rs = st.executeQuery(query);
          	if(rs.next()){
     		String UMLS=findUMLS(rs.getString(1));
     		newid=nodeInsert(rs.getString(2),rs.getString(3),UMLS);
     			
     		}
     
     	    st.close();
              
              
          }
          catch (Exception e ) {
          	System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
          } 
	
	 return newid;
   }
	
	
	
	
	
	
}
