package openNP;

import java.io.FileInputStream;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class NLP_Parser {
  
	 List<String> result = new ArrayList<String>();
     private String Path;
	 
	 NLP_Parser(String P,String text){
		Path=P;
		try{
		// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
			InputStream is = new FileInputStream(Path+"/en-sent.bin");
			SentenceModel model = new SentenceModel(is);
			SentenceDetectorME sdetector = new SentenceDetectorME(model);
		 
			String sentences[] = sdetector.sentDetect(text);
		 
			for(String st: sentences){
				Sentence(st.replace(",",""));
			}
			is.close();}
		catch(Exception e){
			System.err.println("Got an exception! ");
		    e.printStackTrace();
			
		}
		}
	private void Sentence(String S){
		 try{
				// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
				InputStream is = new FileInputStream(Path+"/en-parser-chunking.bin");
			 	ParserModel model = new ParserModel(is);
			 	opennlp.tools.parser.Parser parser = ParserFactory.create(model);
			 	Parse topParses[] = ParserTool.parseLine(S, parser, 1);
			 	
			 	for (Parse pa : topParses)
			 		find_PP(pa);
				is.close();}
				catch(Exception e){
					System.err.println("Got an exception! ");
				    e.printStackTrace();
					
				}
		 
	 }
	 
	 
	 private void find_PP(Parse p){
			
			
			if (p.getType().equals("PP")) {
				//p.show();
				find_NP(p);
				
			}else{
			for(Parse child : p.getChildren()){
				find_PP(child);
			}
			}
		}
	 
	 private void find_NP(Parse p){
		 if (p.getType().equals("NP")) {
				result.add(p.getCoveredText());
				
			}else{
			for(Parse child : p.getChildren()){
				find_NP(child);
			}
			}
	 }
	
	
}
