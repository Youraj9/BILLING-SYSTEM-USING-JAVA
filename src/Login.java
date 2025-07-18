import java.sql.*;

public class Login {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/trial";
        String username = "root";
        String password = "giganigga";

        Connection con = null;

        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println(" here the connection is created");

            Statement st = con.createStatement();
            String query = "create table admin(username varchar(20), password varchar(20)) ";
            st.executeUpdate(query);
            System.out.println("QUERY HAS DONE ");
        } catch (Exception e) {
            System.out.println(" some error has ocurred " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                    System.out.println(" logged out from the databases");
                }
            } catch (SQLException e) {
                System.out.println(" errore s");

            }
        }
    }
}
