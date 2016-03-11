package result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class File_creator {

	public static void writetofile(String line,String file) throws IOException {
		// Construct BufferedReader from FileReader
		//PrintWriter out = new PrintWriter(new FileWriter(log, true));
		PrintWriter fw0 = new PrintWriter(new FileWriter(file,true));
		
					
		    fw0.write(line+"\n");
		    fw0.close();
					
	}
	
	
	public static String Readfile(String filename) throws IOException {
		// Construct BufferedReader from FileReader
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line=null;
		
		while ((line = br.readLine()) != null) {
			
			line = br.readLine();
			}
		
		
		br.close();
		return line;
	}
	
	
}
