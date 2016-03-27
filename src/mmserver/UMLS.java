package mmserver;


public class UMLS {

	String UMLS;
	int Score;
	String Prefer_name;	
	
	public UMLS(String uMLS, int score, String prefer_name) {
		UMLS = uMLS;
		Score = score;
		Prefer_name = prefer_name;
	}

	public boolean equal(UMLS umls){
		if(this.UMLS.equals(umls.UMLS)) return true;
		else return false;
	}

	public String getUMLS() {
		return UMLS;
	}

	public void setUMLS(String uMLS) {
		UMLS = uMLS;
	}

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public String getPrefer_name() {
		return Prefer_name;
	}

	public void setPrefer_name(String prefer_name) {
		Prefer_name = prefer_name;
	}
	
	
	
}
