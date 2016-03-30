package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Integration {
	
public static void nodesIntegrate(NetTable n,Node[] nodes,NodeMap nm){
	
	 int id;
     for(Node nn:nodes){
   	      if(nn.reference_name==null) id=n.nodeInsert(nn.name, nn.type);
   		  else id=n.nodeInsert(nn.reference_name, nn.type);
   		  nm.put(nn.id, id);
   	      }
	
}

public static void edgesIntegrate(NetTable n,NetTable old,NodeMap nm,Connection conn){
	     
	     String table=old.edgeTable;
	     
	
	     String query="select * from "+table;
			
			try{
	 		    
				Statement st = conn.createStatement();
	     		ResultSet rs = st.executeQuery(query);
	     		
	     		while(rs.next()){
	     		int	node1=nm.getValue(rs.getInt(1));
	     		int node2=nm.getValue(rs.getInt(2));
	     		if(node1!=node2){
	     		n.edgeInsert(node1, node2);}
	     		}
	     		st.close();
	     	}
	     	catch (Exception e)
	         {
	           System.err.println("Got an exception! ");
	           System.err.println(e.getMessage());
	           e.printStackTrace();
	         }
	     	
}
	
	
	
	
	
public static String Conbine(String net1,String net2,Connection conn){
		
		  NetTable s1=new NetTable(net1,conn);
		  NetTable s2=new NetTable(net2,conn);
	      NetTable n=new NetTable(net1+net2,conn);
	      n.build();
	      
	      NodeMap nm1=new NodeMap("nm1",conn);
	      NodeMap nm2=new NodeMap("nm2",conn);
	      
	      Node[] old_1=s1.getNodes();
	      Node[] old_2=s2.getNodes();
	      
	     	  
		 nodesIntegrate(n,old_1,nm1);
		 nodesIntegrate(n,old_2,nm2);
		 
		 edgesIntegrate(n,s1,nm1,conn);
		 edgesIntegrate(n,s2,nm2,conn);
		 
		 n.Report();
		 return n.NetName;
	}


    public static String getNumber(String[] arr,int num,Connection conn){
     
     if(num == 1) return Conbine(arr[num-1],arr[num],conn);
     
     else return Conbine(arr[num],getNumber(arr,num-1,conn),conn);
    	
    	
    }

	
public static void main(String[] args) {
	try
    {
    
      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
      
      String[] sources={"KEGG","GWAS","PheWAS","PharmGKB","FDA"};
      
      System.out.println(getNumber(sources,4,conn));
      
      
     //Conbine("table1","table2",conn);
      
      conn.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      e.printStackTrace();
    }


}	
	
	
	
	
	

}
