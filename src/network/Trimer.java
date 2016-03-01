package network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class Trimer {
	public static void createNet(String network,Connection conn) {
        String dropEdges="DROP TABLE IF EXISTS "+network+"_edges_trimed";
        String dropNodes="DROP TABLE IF EXISTS "+network+"_nodes_trimed";
        String dropRef="DROP TABLE IF EXISTS "+network+"_reference";
        
		String nodesTable = "CREATE TABLE "+network+"_nodes_trimed (" 
            + "id INT NOT NULL AUTO_INCREMENT,"  
            + "name VARCHAR(100)," 
            + "type VARCHAR(100),PRIMARY KEY (id))";  
        
        String edgesTable = "CREATE TABLE "+network+"_edges_trimed (" 
                + "node1 int,"  
                + "node2 int," 
                + "FOREIGN KEY (node1) REFERENCES "+network+"_nodes_trimed (id),"
                + "FOREIGN KEY (node2) REFERENCES "+network+"_nodes_trimed (id))";
        
        String referenTable = "CREATE TABLE "+network+"_reference (" 
                + "new int,"  
                + "old int," 
                + "FOREIGN KEY (new) REFERENCES "+network+"_nodes_trimed (id),"
                + "FOREIGN KEY (old) REFERENCES "+network+"_nodes (id))";
              
        try {
            
            Statement st = conn.createStatement();
            //The next line has the issue
            st.executeUpdate(dropRef);
            st.executeUpdate(dropEdges);
            st.executeUpdate(dropNodes);
            st.executeUpdate(nodesTable);
            st.executeUpdate(edgesTable);
            st.executeUpdate(referenTable);
            System.out.println("network  "+network+" Created");
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public static void nodeUpdate(String network,Connection conn){
		try
	    {
	       
	      
		  String query = "select id,name,reference_name,type from "+network+"_nodes";
		  PreparedStatement node_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_nodes_trimed(name,type) VALUES(?,?)");
		  PreparedStatement ref_insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_reference(new,old) VALUES(?,?)");
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
				String node;
				int old_id=rs.getInt(1);
				if(rs.getString(3)==null){
				 node=rs.getString(2);
				}else{
				 node=rs.getString(3);}
				node=node.toUpperCase();
				String node_type=rs.getString(4);
								
				if(NodesNetwork.node_contain(node, network+"_nodes_trimed", conn)==0){
				node_insert.setString(1, node);
				node_insert.setString(2, node_type);
				node_insert.executeUpdate();		
				}
				int new_id=NodesNetwork.node_contain(node, network+"_nodes_trimed", conn);
				ref_insert.setInt(1, new_id);
				ref_insert.setInt(2, old_id);
				ref_insert.executeUpdate();	  
								
			}
	      
	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
public static int newID(int old_id,String network,Connection conn) {
		
		int result = 0;
	    
    	try{
    		
    		String query = "select new from "+network+"_reference where old="+old_id;
    		
    		ResultSet rs = conn.createStatement().executeQuery(query);
    		   	     
    		if(rs.next()){
    			
    			result = rs.getInt(1);
    		}	
    		
    			
    	}
    	catch (Exception e)
        {
          System.err.println("Got an exception! ");
          System.err.println(e.getMessage());
        }
		return result;
	}	
	
	
	public static void edgesUpdate(String network,Connection conn){
		try
	    {
	       
	      
		  String query = "select node1,node2 from "+network+"_edges";
		  PreparedStatement insert =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+network+"_edges_trimed(node1,node2) VALUES(?,?)");
		 
		  ResultSet rs = conn.createStatement().executeQuery(query);
			  
		     
			while(rs.next()){
												
				
				insert.setInt(1,newID(rs.getInt(1),network,conn));
				insert.setInt(2,newID(rs.getInt(2),network,conn));
				insert.executeUpdate();		
				
				
								
			}
	      
	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
	
	
	
	public static void main(String[] args) {
		try
	    {
	    
	      String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	      Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	      String[] sources={"KEGG","GWAS","PheWAS","PharmGKB","FDA"};
	      for(int i=0;i < sources.length; i++){
	      createNet(sources[i],conn);
	      nodeUpdate(sources[i],conn);
	      edgesUpdate(sources[i],conn);
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
