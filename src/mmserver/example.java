package mmserver;

public class example {

	public static void main(String[] args) {

    MetaMap mm = new MetaMap("biomedinformatics.is.umbc.edu","-I -J dsyn,fndg,neop,mobd,patf,sosy");
    String result = mm.Paser("cancer");
    
    System.out.println(result);

    mm.close();
	}

}
