package openNP;



public class Test {

	public static void main(String[] args){
		
		String text1="Used orally for jet lag, insomnia, shift-work disorder, circadian rhythm disorders in the blind (evidence for efficacy), and benzodiazepine and nicotine withdrawal. Evidence indicates that melatonin is likely effective for treating circadian rhythm sleep disorders in blind children and adults. It has received FDA orphan drug status as an oral medication for this use. A number of studies have shown that melatonin may be effective for treating sleep-wake cycle disturbances in children and adolescents with mental retardation, autism, and other central nervous system disorders. It appears to decrease the time to fall asleep in children with developmental disabilities, such as cerebral palsy, autism, and mental retardation. It may also improve secondary insomnia associated with various sleep-wake cycle disturbances. Other possible uses for which there is some evidence for include: benzodiazepine withdrawal, cluster headache, delayed sleep phase syndrome (DSPS), primary insomnia, jet lag, nicotine withdrawal, preoperative anxiety and sedation, prostate cancer, solid tumors (when combined with IL-2 therapy in certain cancers), sunburn prevention (topical use), tardive dyskinesia, thrombocytopenia associated with cancer, chemotherapy and other disorders.";
		//String text2="For use as adjunctive therapy to diet to reduce elevated LDL-C, Total-C,Triglycerides and Apo B, and to increase HDL-C in adult patients with primary hypercholesterolemia or mixed dyslipidemia (Fredrickson Types IIa and IIb)";
		//String text3="For detection of residueal or recurrent thyroid cancer";
		
			NLP_Parser p=new NLP_Parser("C:/Users/xf37538/Downloads",text1);
			for(String t : p.result){
				System.out.println(t);
				
			
		}
		
		
		
		
		
	}

}
