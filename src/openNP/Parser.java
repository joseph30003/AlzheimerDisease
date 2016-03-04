package openNP;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;


public class Parser {

List<Parse> result = new ArrayList<Parse>();



public Parser(String s) throws InvalidFormatException, IOException{
	
	InputStream is = new FileInputStream("/Users/joseph/Downloads/en-parser-chunking.bin");
	 
	ParserModel model = new ParserModel(is);
 
	opennlp.tools.parser.Parser parser = ParserFactory.create(model);
	
	String sentence=s;
	
	Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
    
	isNode(topParses[0]);

	is.close();
}

	
public void isNode(Parse p){
	
	
	if (p.getType().equals("JJ")) {
		//System.out.println(p.getHeadIndex());
		result.add(p);
	}
	for(Parse child : p.getChildren()){
		isNode(child);
	}
	
}
	
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		String sentence="Urokinase can be used for the treatment of pulminary embolism coronary artery thrombosis IV catheter clearance and venous and arterial blood clots";
		Parser pp = new Parser(sentence);
		
		for(Parse t : pp.result){
			System.out.println(t.getParent().getCoveredText());
		}
	}
}
