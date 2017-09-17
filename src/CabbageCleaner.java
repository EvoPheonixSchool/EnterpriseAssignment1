import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**Clears the table in the database
 * Created by Sheldon on 2017-09-17.
 */
public class CabbageCleaner {
    private String connectionString = null;
    private String username = null;
    private String password = null;
    private Connection con = null;

    /**
     * Constructor sets values for the database then deletes the cabbage from the database
     * @param cs connection string
     * @param un username
     * @param pw password
     */
    public CabbageCleaner(String cs,String un,String pw){
        connectionString = cs;
        username = un;
        password = pw;
        //calls the method for deleting the table
        deleteAllCabbage();
    }


    /**
     * Method taken from the dataloader class built by Stanley Pieda
     */
    public void deleteAllCabbage() {
        PreparedStatement pstmt = null;
        try{
            if(con == null || con.isClosed()) {
                System.out.println("Cannot delete records, no connection or connection closed");
            }

            pstmt = con.prepareStatement(
                    "TRUNCATE TABLE cabbages");
            pstmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{ if(pstmt != null){ pstmt.close(); }}
            catch(SQLException ex){System.out.println(ex.getMessage());}
        }
    }
}
