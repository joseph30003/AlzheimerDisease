package alzheimer;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Version {

    public static void main(String[] args) throws ClassNotFoundException {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

       
       
        String myUrl = "jdbc:mysql://biomedinformatics.is.umbc.edu/drugabuse";
        
        String user = "weijianqin";
        String password = "weijianqin";
        
       

        try {
            con = DriverManager.getConnection(myUrl, user, password);;
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException ex) {
        	System.err.println("Got an exception! ");
            System.err.println(ex.getMessage());

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
