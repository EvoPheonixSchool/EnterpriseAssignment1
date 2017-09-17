import java.util.concurrent.ArrayBlockingQueue;

/**
 * Places items into the buffer and takes items from the buffer
 * implements Buffer
 * Created by Sheldon on 2017-09-17.
 */
public class BlockingQueueBuffer implements Buffer{
    private final ArrayBlockingQueue<Cabbage> buffer;
    //string used for creating messages to return
    private String message = "";
    private Cabbage cabbage = null;

    /**
     * default constructor initializes the buffer for cabbage
     */
    public BlockingQueueBuffer(){
        buffer = new ArrayBlockingQueue<Cabbage>(1);
    }

    /**
     * takes given data and puts it in the buffer
     * @param c Cabbage object to be placed
     * @return String message about the buffer placement
     * @throws InterruptedException
     */
    public String putData(Cabbage c) throws InterruptedException {
        //puts cabbage into the buffer
        buffer.put(c);
        //creates a message about the insertion
        message = "Producer writes cabbage with ID: " +  c.getId() + ". Buffer size: " + buffer.size();
        return message;
    }

    /**
     * gets the next cabbage object in the buffer
     * @return Cabbage the next cabbage object
     * @throws InterruptedException
     */
    public Cabbage getData() throws InterruptedException {
        //takes the next Cabbage object from the buffer
        cabbage = buffer.take();
        //creates and displays message about the Cabbage removal
        message = "Cabbage taken has ID: " + cabbage.getId() + ". Buffer size: " + buffer.size();
        System.out.println(message);
        return cabbage;
    }
}
