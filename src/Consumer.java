import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**This class takes data from the buffer and moves it into a database
 * Created by Sheldon on 2017-09-21.
 */
public class Consumer implements Runnable{
    private Connection con = null;

    private Buffer buffer;

    private final String connectionString = "jdbc:mysql://localhost/assignment1";
    private final String username = "assignment1-1";
    private final String password = "password";

    public Consumer(Buffer buff){
        buffer = buff;
    }

    /**
     * Opens connection to database,
     * pull Cabbage form buffer,
     * insert Cabbage into database,
     * then closes connection
     *
     */
    @Override
    public void run() {
        /*
        gets a cabbage
        opens connection
        loop start
        insert cabbage
        next cabbage
        check if cabbage is null
        loop end
        close connection
         */
        Cabbage cab = null;
        try {
            cab = buffer.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openConnection();
        {
            insertCabbage(cab);
            try {
                cab = buffer.getData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }while(cab != null)
        closeConnection();
    }

    /**
     * Opens a connection to a database
     * Method taken from the dataloader class built by Stanley Pieda
     */
    private void openConnection(){
        try{
            if(con != null){
                System.out.println("Cannot create new connection, one exists already");
            }
            else{
                con = DriverManager.getConnection(connectionString, username, password);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database
     * Method taken from the dataloader class built by Stanley Pieda
     */
    private void closeConnection() {
        try{ if(con != null){ con.close(); }}
        catch(SQLException ex){System.out.println(ex.getMessage());}
    }

    /**
     * Takes Cabbage object and moves data into the database
     * Method taken from the dataloader class built by Stanley Pieda
     * @param cabbage
     */
    public void insertCabbage(Cabbage cabbage) {
        PreparedStatement pstmt = null;
        try{
            if(con == null || con.isClosed()) {
                System.out.println("Cannot insert records, no connection or connection closed");
            }


            pstmt = con.prepareStatement(
                    "INSERT INTO Cabbages (linenumber, alpha, beta, charlie, delta) " +
                            "VALUES(?, ?, ?, ?, ?)");
            pstmt.setInt(1, cabbage.getLineNumber());
            pstmt.setString(2, cabbage.getAlpha());
            pstmt.setString(3, cabbage.getBeta());
            pstmt.setString(4, cabbage.getCharlie());
            pstmt.setString(5,  cabbage.getDelta());
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
