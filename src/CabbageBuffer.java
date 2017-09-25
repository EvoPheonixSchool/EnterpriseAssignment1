import java.util.ArrayList;

/**Uses an array of 100 as a buffer
 * Created by Sheldon on 2017-09-17.
 */
public class CabbageBuffer implements Buffer{
    private ArrayList<Cabbage> CabbageFarm;
    private Cabbage cabbage;
    private String message = "";
    private int writen = 0;
    private int read = 0;

    /**
     * Default constructor initializes the arraylist
     * By: Sheldon McGrath
     */
    public CabbageBuffer(){
        CabbageFarm = new ArrayList();
    }

    /**
     * reads the next cabbage into the buffer
     * @param c Cabbage object to be placed in the buffer
     * @return String message about the cabbage placed in the buffer
     * @throws InterruptedException
     * By: Sheldon McGrath
     */
    public synchronized String putData(Cabbage c) throws InterruptedException {
        //checks if the array is at its max
        if(CabbageFarm.size() == 100){
            message = "Buffer is full, waiting for space to open.";
            System.out.println(message);
            //wait for space in array
            wait();
        }

        //adds cabbage from the arraylist
        CabbageFarm.add(c);
        read ++;
        //prints information about the records read
        message = "Cabbage added to buffer with ID: " + c.getId() + ". Buffer size: " + CabbageFarm.size();
        //notifies that there is data in the arraylist
        notifyAll();
        //return message about the cabbage id
        return message;
    }


    /**
     * Gets the next cabbage from the buffer
     * @return Cabbage next object to be written
     * @throws InterruptedException
     * By: Sheldon McGrath
     */
    public synchronized Cabbage getData() throws InterruptedException {
        //check to see if buffer is empty
        if(CabbageFarm.size() == 0){
            System.out.println("The buffer is empty, waiting.");
            //waits for buffer to be populated
            wait();
            return cabbage;
        }

        //take next cabbage
        cabbage = CabbageFarm.get(0);
        //remove cabbage taken from buffer
        CabbageFarm.remove(0);
        if(cabbage.getAlpha() == "end"){
            cabbage.setBeta("C");
            return cabbage;
        }
        writen ++;
        //notify cabbage has been taken
        notifyAll();
        //print message about cabbage removed from the buffer
        /*message = "Cabbage removed from buffer with ID: " + cabbage.getId();
        System.out.println(message);*/
        //print message about records written
        message = writen + " records have been written.";
        System.out.println(message);
        //returns cabbage to write
        return cabbage;
    }
}
