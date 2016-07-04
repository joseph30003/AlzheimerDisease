package database;

import java.sql.Connection;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

import mmserver.MetaMap;
import mmserver.UMLS;

public class MetaTable {
	
	protected Connection conn;
	public  String Name;
	public MetaMap api;
	public NetTable source;
	
	public MetaTable (Connection con,MetaMap API){
		Name="MetaTable";
		conn=con;
		api=API;
	}
	
	
	public NetTable getSource() {
		return source;
	}


	public void setSource(NetTable source) {
		this.source = source;
	}


	public void dropTable(String table){
		String dropTable="DROP TABLE IF EXISTS "+table;
        try {
            Statement st = conn.createStatement();
        	st.executeUpdate(dropTable);
                                   
            System.out.println("Table  "+table+" droped");
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
		
	}
	
	public void createTable() {
        
		dropTable(Name);
		
	 	String 	Table = "CREATE TABLE "+Name+" (term varchar(600),"
	 			+ "prefer_name varchar(1000), "
	 			+ "UMLS varchar(100), "
	 			+ "type varchar(50), "
	 			+ "score int(11), "
	 			+ "source varchar(30),"
	 			+ "source_id int(11))";
                    
        try {
            
        	Statement st = conn.createStatement();
            //The next line has the issue
            
            if(!Table.isEmpty()){
            
			st.executeUpdate(Table);
            }else{
            	System.out.println("no sql statements!");
            }
            System.out.println("network  "+Name+" Created");
            st.close();
        }
        catch (Exception e ) {
        	System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
	
	public UMLS[] getUMLS(Node node){
	String Conf="-I";	
	if(node.type.equals("disease")) Conf="-I -J dsyn,fndg,neop,mobd,patf,sosy,acab,anab,comd,cgab,emod,fngs,inpo,inpr";
	api.SetUp(Conf);
	System.out.println(node.getName());
	String rs=api.Paser(node.getName());
	System.out.println("**********"+rs);
	return api.toUMLSArray(rs);
	}
	
	public void nodeInsert(String term,String prefer_name,String UMLS,int score,String src,int src_id){
		  try{
		   		
		    			
		    		      			
		    			PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO "+Name+"(term,prefer_name,UMLS,score,source,source_id) VALUES(?,?,?,?,?,?)");
			            pst_user.setString(1, term);
			            pst_user.setString(2, prefer_name);
			            pst_user.setString(3, UMLS);
			            pst_user.setInt(4, score);
			            pst_user.setString(5, src);
			            pst_user.setInt(6, src_id);
			            pst_user.execute();
			            pst_user.close();          		
			            
		    		
		    	    		
		    				
		    	}
		    	catch (Exception e)
		        {
		          System.err.println("Got an exception! ");
		          System.err.println(e.getMessage());
		          e.printStackTrace();
		        }
		    	
		
		
	}
	
	public void run(){
		createTable();
	    source.Report();
		Node[] nodes=source.getNodes("disease");
		for(Node n: nodes){
		UMLS[] um = getUMLS(n);
		if(um!=null){
			for(UMLS u:um){
			nodeInsert(n.getName(),u.getPrefer_name(),u.getUMLS(),u.getScore(),source.NetName,n.getId());
			}
		}
		}
		
	}
	

}
