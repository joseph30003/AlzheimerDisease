package mmserver;

public class example {

	public static void main(String[] args) {

    MetaMap mm = new MetaMap("biomedinformatics.is.umbc.edu");
    String DiseaseConf="-I -J dsyn,fndg,neop,mobd,patf,sosy,acab,anab,comd,cgab,emod,fngs,inpo,inpr";
    mm.SetUp(DiseaseConf);
        
    String result = mm.Paser("Congenital pigmentary anomalies of skin");
    
    System.out.println(result);
    mm.close();
   
	}

}
