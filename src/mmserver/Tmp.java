package mmserver;

public class Tmp {

	public static void test(String[] args){
		String original="uduude887  �32)$#@@";
		String fixed = original.replaceAll("[^a-zA-Z1-9 ]", "");
		System.out.println(fixed);
	}
	
	 
		   public static void main(String[] args) {
		      String testString = "KO:K05081,hsa:5618,";
		      System.out.println(testString.toUpperCase().indexOf("HSA:"));
		     // System.out.println(testString.substring(testString.indexOf("HSA:"),testString.lastIndexOf("CDE")));
		     
		   }
		
	
}
