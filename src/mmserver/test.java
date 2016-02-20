package mmserver;




import java.util.List;

import gov.nih.nlm.nls.metamap.*;

public class test {

	public static void run(String terms,MetaMapApi api)  {
		// TODO Auto-generated method stub
		try{
		
		
		List<Result> resultList = api.processCitationsFromString(terms);
		
		
		Result result = resultList.get(0);
		
			
		for (Utterance utterance: result.getUtteranceList()) {
			System.out.println("Utterance:");
			System.out.println(" Id: " + utterance.getId());
			System.out.println(" Utterance text: " + utterance.getString());
			System.out.println(" Position: " + utterance.getPosition());
			for (PCM pcm: utterance.getPCMList()) {
				System.out.println("Phrase:");
				System.out.println(" text: " + pcm.getPhrase().getPhraseText());     
				System.out.println("Mappings:");
				for (Mapping map: pcm.getMappingList()) {
		            System.out.println(" Map Score: " + map.getScore());
		            for (Ev mapEv: map.getEvList()) {
		              System.out.println("   Score: " + mapEv.getScore());
		              System.out.println("   Concept Id: " + mapEv.getConceptId());
		              System.out.println("   Concept Name: " + mapEv.getConceptName());
		              System.out.println("   Preferred Name: " + mapEv.getPreferredName());
		              System.out.println("   Matched Words: " + mapEv.getMatchedWords());
		              System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
		              System.out.println("   MatchMap: " + mapEv.getMatchMap());
		              System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
		              System.out.println("   is Head?: " + mapEv.isHead());
		              System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
		              System.out.println("   Sources: " + mapEv.getSources());
		              System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
		              
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
	
	public static void main(String[] args){
		MetaMapApi api = new MetaMapApiImpl("biomedinformatics.is.umbc.edu");
		api.setOptions("-I -J dsyn,fndg,neop,mobd,patf,sosy"); 
		run("Alzheimer's disease",api); 
	}
	
}
	
