import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**this class will read the info from a file, create the cabbage objects, and moves it to the buffer
 * Created by Sheldon on 2017-09-21.
 */
public class Producer implements Runnable {
    private int recordsRead;

    private Scanner cabbageScanner = null;
    private CabbageCleaner cc;
    private Buffer buffer;

    private final String connectionString = "jdbc:mysql://localhost/assignment1";
    private final String username = "assignment1-1";
    private final String password = "password";

    public Producer(Buffer buf){
        buffer = buf;
    }

    /**
     * this opens the file, inserts data into the cabbage class then moves it to the buffer
     * Method taken from the dataloader class called ProccessRecords built by Stanley Pieda
     * modified by Sheldon McGrath
     */
    @Override
    public void run() {
        // open file
        // open connection to database
        // delete old data
        // starting time stamp
        // loop over file reading records
        //   load data into Cabbage
        //   insert new data
        // end loop
        // ending time stamp
        // output to screen
        try {
            openFile();
            cc = new CabbageCleaner(connectionString, username, password);

            while(cabbageScanner.hasNext()){
                String line = cabbageScanner.nextLine(); // read raw data
                String[] fields = line.split(","); // split on delimiter
                Cabbage cabbage = new Cabbage();
                cabbage.setLineNumber(Integer.parseInt(fields[0]));
                cabbage.setAlpha(fields[1]);
                cabbage.setBeta(fields[2]);
                cabbage.setCharlie(fields[3]);
                cabbage.setDelta(fields[4]);
                recordsRead ++;
                bufferCabbage(cabbage);
                System.out.printf("%d records read%n", recordsRead);

            }
            Cabbage cab = new Cabbage();
            cab.setAlpha("end");
            bufferCabbage(cab);


            //System.out.printf("Buffer type is %s%n", buffer.getClass().getName()); // tip for students threaded program
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeFile();
        }

    }

    /**
     * this opens the file and moves it into the scanner object
     * Method taken from the dataloader class built by Stanley Pieda
     */
    private void openFile() {
        try {
            //cabbageScanner = new Scanner(new FileReader(new File("DataSet17F_5000.csv")));
            cabbageScanner = new Scanner(new FileReader(new File("DataSet17F.csv")));
            //cabbageScanner = new Scanner(new FileReader(new File("test1.csv")));
        }
        catch(IOException ex){
            System.out.println("Problem opening file: "
                    + ex.getMessage());
        }
    }

    /**
     * this closes the connection to the file
     * Method taken from the dataloader class built by Stanley Pieda
     */
    private void closeFile() {
        try {
            if(cabbageScanner != null) {cabbageScanner.close();}
        }
        catch(Exception ex) {
            System.out.println("Problem closing file: "
                    + ex.getMessage());
        }
    }

    /**
     * this moves the cabbage onject created to the buffer
     * @param cab Cabbage created
     * By: Sheldon McGrath
     */
    private void bufferCabbage(Cabbage cab) throws InterruptedException {
        buffer.putData(cab);
    }

}
