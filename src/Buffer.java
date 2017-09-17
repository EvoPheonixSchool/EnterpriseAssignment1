/**an interface for blocking queue buffer
 * Created by Sheldon on 2017-09-17.
 */
public interface Buffer {
    //puts data into the buffer
    public String putData(Cabbage c) throws InterruptedException;

    //gets data from the buffer
    public Cabbage getData() throws InterruptedException;
}
