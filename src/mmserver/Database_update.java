package mmserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class Database_update {

	public static void DataInput(String term,int source_id,String prefer_name, String Id, String type,int score,String source, Connection conn){
		try
		{
		PreparedStatement pst_user =  (PreparedStatement) conn.prepareStatement("INSERT INTO Metamap(term,prefer_name,ID,type,score,source,source_id) VALUES(?,?,?,?,?,?,?)");
        pst_user.setString(1,term);
        pst_user.setString(2,prefer_name);
        pst_user.setString(3,Id);
        pst_user.setString(4,type);
        pst_user.setInt(5,score);
        pst_user.setString(6,source);
        pst_user.setInt(7,source_id);
        pst_user.execute();
		}
		catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}
 
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] sources={"KEGG","GWAS","PheWAS","FDA"};
		String type="disease";
	    try
	    {
	     String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	     Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	     MetaMapApi api = new MetaMapApiImpl();
		 api.setOptions("-I -J dsyn,fndg,neop,mobd,patf,sosy"); 
		 
		  
		 
	     for(int i=0;i < sources.length; i++){
	    	    	 
	    	 
		 
		 
	     String query_search = "select id,name from "+sources[i]+"_nodes where type =\""+type+"\"" ;
	     //System.out.println(query_search);
	     
   	     ResultSet rs = conn.createStatement().executeQuery(query_search);
   	     
   	     
   	     while(rs.next()){
   	    	 
   		 String term=rs.getString(2).replaceAll("[^a-zA-Z1-9 ]", "");
   		 //test.run(term,api);
   		
   		 int source_id=rs.getInt(1);
         List<Result> resultList = api.processCitationsFromString(term);
		 Result result = resultList.get(0);
		 for (Utterance utterance: result.getUtteranceList()) {
			
			for (PCM pcm: utterance.getPCMList()) {
				
				for (Mapping map: pcm.getMappingList()) {
		            
		            for (Ev mapEv: map.getEvList()) {
		              
		                //System.out.println(term);
		            	DataInput(term,source_id,mapEv.getPreferredName(),mapEv.getConceptId(),type,Math.abs(mapEv.getScore()),sources[i],conn);
		            	              
		                                       }
				
		                                          }
			                                         } 
	                                                    }  
   		
    	  
      }
	     
	    
	     }
	      
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
	}

}
