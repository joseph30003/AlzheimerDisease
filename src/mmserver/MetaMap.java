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
	public String URL;
	
    public MetaMap(String url){
	 URL=url;			
	}
		
	
	public void SetUp(String conf){
	 	
		try{
			api = new MetaMapApiImpl(URL);
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
  		String UMLS="";
		try
	    {
		 term=term.replaceAll("[^a-zA-Z1-9 ]", "");	
         List<Result> resultList = api.processCitationsFromString(term);
		 Result result = resultList.get(0);
		 for (Utterance utterance: result.getUtteranceList()) {
			
			for (PCM pcm: utterance.getPCMList()) {
				
				for (Mapping map: pcm.getMappingList()) {
		            
		            for (Ev mapEv: map.getEvList()) {
		              
		               UMLS=UMLS+mapEv.getPreferredName()+"#"+mapEv.getConceptId()+"#"+Math.abs(mapEv.getScore())+";";
		            	              
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
  	
	public UMLS[] toUMLSArray(String str){
		if(!str.isEmpty()){
		String[] group = str.split(";");
		UMLS[] rs = new UMLS[group.length];
		
		for(int i=0;i<group.length;i++){
		
		rs[i]=toUMLS(group[i]);
		}
				return rs;
		}else return null;
	
	}
	
	public UMLS toUMLS(String str){
		if(str!=null){
		String[] m = str.split("#");
		return new UMLS(m[1],Integer.parseInt(m[2]),m[0]);
				
		}else return null;
	
	}
	
	
	
}
