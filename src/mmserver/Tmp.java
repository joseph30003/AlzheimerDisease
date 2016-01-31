package mmserver;

public class Tmp {

	public static void main(String[] args){
		String original="uduude887  �32)$#@@";
		String fixed = original.replaceAll("[^a-zA-Z1-9 ]", "");
		System.out.println(fixed);
	}
	
	
}
