package gene;

public class test {
	
public static String term_split (String str){
		
		if (str.contains("\"")) { 
			String[] arr=str.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			for(int i=0;i<arr.length;i++){
				arr[i]=arr[i].replace("\"", "");
			}
					 return String.join("&",arr);
					
				}
			else return str;

}	
	
	
public static void main(String[] args){	
String str="cnn.rest,ttes,tte";
 System.out.println(term_split(str));
}
}
