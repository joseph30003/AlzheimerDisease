package mmserver;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import java.util.List;

public class MetaMap{
	
	public MetaMapApi api;
	
	MetaMap(String url, String conf){
	 	
		try{
			api = new MetaMapApiImpl(url);
			api.setOptions(conf);
			   
		   }
		   catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      e.printStackTrace();
		    }
		
	}
	
   public void close(){
	   
	   try{
		   api.disconnect();
	   }
	   catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      e.printStackTrace();
	    }
   }
	
  	public String Paser(String term){
  		String UMLS=null;
		try
	    {
         List<Result> resultList = api.processCitationsFromString(term);
		 Result result = resultList.get(0);
		 for (Utterance utterance: result.getUtteranceList()) {
			
			for (PCM pcm: utterance.getPCMList()) {
				
				for (Mapping map: pcm.getMappingList()) {
		            
		            for (Ev mapEv: map.getEvList()) {
		              
		               UMLS=UMLS+mapEv.getPreferredName()+","+mapEv.getConceptId()+","+Math.abs(mapEv.getScore())+";";
		            	              
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
	    
	return UMLS;	
		      
	}
	
	
}
