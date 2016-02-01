package mmserver;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class GKB_metamap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] sources={"GKB"};
		String[] types={"disease","Drug"};
	    try
	    {
	     String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
	     Connection conn = DriverManager.getConnection(myUrl, "weijianqin", "weijianqin");
	     MetaMapApi api = new MetaMapApiImpl();
		 api.setOptions("-y -k dsyn"); 
	     for(int i=0;i < sources.length; i++){
	    	 
	    	 for(int j=0;j < types.length; j++){
	    	 
		 
		 
	     String query_search = "select id,name from "+sources[i]+"_nodes where type =\""+types[j]+"\" and reference_name is null" ;
	     //System.out.println(query_search);
   	     ResultSet rs = conn.createStatement().executeQuery(query_search);
   	     
   	     
   	     while(rs.next()){
   		 String term=rs.getString(2).replaceAll("[^a-zA-Z1-9 ]", "");
   		 int source_id=rs.getInt(1);
         List<Result> resultList = api.processCitationsFromString(term);
		 Result result = resultList.get(0);
		 for (Utterance utterance: result.getUtteranceList()) {
			
			for (PCM pcm: utterance.getPCMList()) {
				
				for (Mapping map: pcm.getMappingList()) {
		            
		            for (Ev mapEv: map.getEvList()) {
		              
		                //System.out.println(term);
		            	Database_update.DataInput(term,source_id,mapEv.getPreferredName(),mapEv.getConceptId(),types[j],Math.abs(mapEv.getScore()),sources[i],conn);
		            	              
		                                       }
				
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
