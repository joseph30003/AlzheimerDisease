package gene;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class example {
	
public static String HSAfinder(String in){
	    	
	    	Pattern p = Pattern.compile("\\[HSA:(.*?)\\]");
	    	Matcher m = p.matcher(in);
			String out = "";
			if(m.find()) {
			    out = m.group(1);
			}
	    	return out;
	    }

	 
	 
 public static String diseaseHandler(String in){
    String out=in.replaceAll("\\[.*?\\] ?", "").replaceAll("[\\$#]", " ").replaceAll("\\s+$", "");
	return out;
    }
	
 public static String geneHandler(String in){
	 String out=HSAfinder(in);
	 if(out==""){
		 return diseaseHandler(in);
	 }else return out;
 }
	
	public static void main(String[] args)
	  {
		
         		      
		  String gene="BCR-ABL$(translocation)$[HSA:613$25]$[KO:K08878$K06619]";    
		      
		      System.out.println(geneHandler(gene));
		      
		      
		      
		      
		   
		
	
	
}
	
	

}
