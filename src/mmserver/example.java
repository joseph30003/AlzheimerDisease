package mmserver;

public class example {

	public static void main(String[] args) {

    MetaMap mm = new MetaMap("");
    String DiseaseConf="-I -J dsyn,fndg,neop,mobd,patf,sosy";
    mm.SetUp(DiseaseConf);
        
    String result = mm.Paser("cancer");
    
    System.out.println(result);
    mm.close();
    //String st="Malignant Neoplasms,C0006826,1000;Primary malignant neoplasm,C1306459,1000;";
    UMLS[] rs=mm.toUMLSArray(null);
    if(rs!=null){
    for(UMLS u:rs){
    	System.out.println(u.Prefer_name+u.UMLS+u.Score);
    }}
	}

}
