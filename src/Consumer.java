import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**This class takes data from the buffer and moves it into a database
 * Created by Sheldon on 2017-09-21.
 */
public class Consumer implements Runnable{
    private Connection con = null;
    private int inserted = 0;
    private long start;

    private Buffer buffer;

    private final String connectionString = "jdbc:mysql://localhost/assignment1";
    private final String username = "assignment1-1";
    private final String password = "password";

    /**
     * Default constructor
     * By: Sheldon McGrath
     * @param buff the buffer to share between the producer and consumer
     * @param stime the starttime of the program
     */
    public Consumer(Buffer buff,long stime){
        buffer = buff;
        start = stime;
    }

    /**
     * Opens connection to database,
     * pull Cabbage form buffer,
     * insert Cabbage into database,
     * then closes connection
     * By: Sheldon McGrath
     */
    @Override
    public void run() {

        //opens connection to database
        openConnection();

        //sleep to let producer start
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start getting cabbages
        getCabbage();

        //close connection to the database
        closeConnection();

        //calculate time elapsed created by Stanley Pieda
        long endTime = System.currentTimeMillis();
        long elapsedTime;
        elapsedTime = endTime - start;
        int minutes = (int)elapsedTime / 1000 / 60;
        int seconds = (int)elapsedTime / 1000 % 60;

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");

        System.out.printf("%d mileseconds elapsed%n", elapsedTime);
        System.out.printf("%02d minutes, %02d seconds, %03d millisecs%n", minutes, seconds, elapsedTime % 1000);
        System.out.printf("Program by: Sheldon McGrath run on %s%n",dateTime.format(format));
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
    public synchronized void insertCabbage(Cabbage cabbage) {
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
            inserted ++;
            System.out.println(inserted + " have been inserted");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{ if(pstmt != null){ pstmt.close(); }}
            catch(SQLException ex){System.out.println(ex.getMessage());}
        }
    }


    /**
     * Loops getting cabbage objects from the buffer until there aren't any more
     * then sends them to be inserted into the database
     * By: Sheldon McGrath
     */
    private synchronized void getCabbage(){
        Cabbage cab = new Cabbage();
        //loops until the alpha value set is "end
        while(cab.getAlpha() != "end"){
            try {
                //gets cabbage
                cab = buffer.getData();
                //checks for end
                if(cab.getAlpha() != "end"){
                    //send to be inserted if not end
                    insertCabbage(cab);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
