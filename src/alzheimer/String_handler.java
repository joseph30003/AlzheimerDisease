package alzheimer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String_handler {
	
	public static String[] String_spliter(String in){
		String[] array=in.split("#");
		return array;
			
	}
	
    public static String Attributes_spliter(String in){
    	
    	Pattern p = Pattern.compile("\\[(.*?)\\]");
    	Matcher m = p.matcher(in);
		String out=in.replaceAll("\\[.*?\\] ?", "").replaceAll("[\\$]", " ").replaceAll("\\s+$", "");
		while(m.find()) {
		    out=out+"$"+m.group(1);
		}
    	return out;
    }
	
public static String Nodes_sperate (String in ) {
		
		String nodes ="";
		
        String[] array=String_spliter(in);
		
		
		for(int i=0; i<array.length ;i++){
		
	        nodes=nodes+Attributes_spliter(array[i])+"#";
	        //System.out.println(nodes);
		}
			
		return nodes;
	}
	
	public static void main(String[] args) {
		String[] in={"Acute$lymphoblastic$leukemia$(ALL)$(precursor$B$lymphoblastic$leukemia)","BCR-ABL$(translocation)$[HSA:613$25]$[KO:K08878$K06619]#MLL-AF4$(rearrangement)$[HSA:4297$4299]$[KO:K09186$K15184]#E2A-PBX1$(translocation)$[HSA:6929$5087]$[KO:K09063$K09355]#c-MYC$(translocation)$[HSA:4609]$[KO:K04377]#TEL-AML1$(translocation)$[HSA:2120$861]$[KO:K03211$K08367]","Vincristine$[DG:DG00690]#Prednisone$[DG:DG00094]#Daunorubicin$[DG:DG00697]#Asparaginase$[DR:D02997]#Methotrexate$[DG:DG00681]#Cyclophosphamide$[DG:DG00675]#Vincristine$[DG:DG00690]#Doxorubicin$[DG:DG00696]#Dexamethasone$[DG:DG00011]#Cytarabine$[DG:DG00686]#6-mercaptopurine$[DG:DG00683]"};
        for(int j=0; j<in.length; j++){
        System.out.println(in[j]);
		
        String[] result=String_spliter(Nodes_sperate(in[j]));
        for(int i=0; i<result.length ;i++){
        	System.out.println(result[i]);
        }
        
        }
        
        System.out.println("result"+GWAS.Gene_handler("ADORA2A-AS1, BCRP3, CABIN1, DKFZp434K191, DQ570150, DQ571461, DQ576853, EU036692, FAM211B, GGT1, GGT5, GSTT1, GSTTP2, GUCD1, LOC391322, PIWIL3, POM121L10P, POM121L9P, SGSM1, SNRPD3, SPECC1L, SUSD2, TMEM211, TOP1P2, UNQ2565, UPB1", "ADORA2A-AS1"));
        
	}
	
	

}
