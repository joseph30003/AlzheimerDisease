package alzheimer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class DrugBankNetwork {

	
	
	
	
	public static void main(String[] args) {

		try{
			  String myUrl_A = "jdbc:mysql://biomedinformatics.is.umbc.edu/Alzheimer";
		      Connection conn_a = DriverManager.getConnection(myUrl_A, "weijianqin", "weijianqin");
		      PreparedStatement pst_user =  (PreparedStatement) conn_a.prepareStatement("INSERT INTO DrugBank(WID,Name,Indication,Resourse,Indentifier,p_id,p_name) VALUES(?,?,?,?,?,?,?)");
		      
		      
		      String myUrl_D = "jdbc:mysql://biomedinformatics.is.umbc.edu/drugbank";
		      Connection conn_d = DriverManager.getConnection(myUrl_D, "weijianqin", "weijianqin");
		      String query="select DrugBank.WID,DrugBank.Name,DrugBank.Indication,DrugBankExternalIdentifier.Resource,DrugBankExternalIdentifier.Identifier,DrugBankTargetPolypeptide.Id,DrugBankTargetPolypeptide.Name from DrugBank,DrugBankExternalIdentifier,DrugBankTarget,DrugBankTargetPolypeptide where DrugBank.WID = DrugBankExternalIdentifier.DrugBank_WID and DrugBank.WID = DrugBankTarget.DrugBank_WID and DrugBankTargetPolypeptide.DrugBankTarget_WID = DrugBankTarget.WID";
		      Statement st_d = conn_d.createStatement();
		      ResultSet rs_d = st_d.executeQuery(query);
		      
		      while(rs_d.next()){
		    	  pst_user.setInt(1,rs_d.getInt(1));
		          pst_user.setString(2,rs_d.getString(2));
		          pst_user.setString(3,rs_d.getString(3));
		          pst_user.setString(4,rs_d.getString(4));
		          pst_user.setString(5,rs_d.getString(5));
		          pst_user.setString(6,rs_d.getString(6));
		          pst_user.setString(7,rs_d.getString(7));
		          pst_user.execute();
		    	 }
		            
		     
		      st_d.close();
		      conn_d.close();
		      conn_a.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

	}

}
