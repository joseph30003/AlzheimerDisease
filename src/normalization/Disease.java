package normalization;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;


import database.*;

public class Disease { 
	
	protected Connection conn;
	public NetTable net;
	Hashtable<String, String> dic = new Hashtable<String, String>();
	Node[] nodes;
	
	public Disease(NetTable Net){
		net = Net;
		conn = Net.conn;
		
		iniDic();
	}

	public void iniDic(){
	try{
		
		Statement st = conn.createStatement();    
 		String query= "select Disease,UMLS from DiseaseUMLS";
 		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
		if( ! dic.containsKey(rs.getString(1).toLowerCase()) ){	
		dic.put(rs.getString(1).toLowerCase(),rs.getString(2));
		}
		}
	}
	catch(Exception e){
		System.err.println("Got an exception! ");
	    e.printStackTrace();
	}	
	}
	
	public String getUMLS(String name){
	   
		if (dic.containsKey(name.toLowerCase())) return dic.get(name.toLowerCase());
		else return null;		
		
	}
	
	public String getUMLS(String name,int id){
		String UMLS=null;   
		try{
			
			Statement st = conn.createStatement();    
	 		String query= "select UMLS from MetaTable where source =\""+name+"\" and source_id="+id+" and score >= 900 limit 1";
	 		ResultSet rs = st.executeQuery(query);
	        while(rs.next()){
			UMLS = rs.getString(1);
			}
		}
		catch(Exception e){
			System.err.println("Got an exception! ");
		    e.printStackTrace();
		}			
		return UMLS;
	}
	
	
	
	public void Normalization(String type){
		
	Node[] nodes = net.getNodes(type);
	if(nodes != null){
	  for(Node node:nodes){
		if( node.reference_name == null ){
			
			if (getUMLS(node.name) != null) net.UpdateRef(getUMLS(node.name), node.id);
			else if( getUMLS(net.NetName,node.id) != null )  net.UpdateRef(getUMLS(net.NetName,node.id), node.id);
		}
		
	
	  }
	}
	}
	
	public void Normalization(){
		
    Normalization("disease");
    Normalization("drug");
		
	}
	
	
		
	
	
	
	

}
