import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class launches the program and sets all the threads
 * created by sheldon mcgrath
 */
public class Launcher {

	//created by sheldon mcgrath
	public static void main(String[] args) {
		//sets the start of the program
		long startTime = System.currentTimeMillis();
		//print the current timestamp
		System.out.println(new Timestamp(System.currentTimeMillis()));


		//choose between blocking queue or the cabbage buffer
		Buffer buffer = new BlockingQueueBuffer();
		//Buffer buffer = new CabbageBuffer();
		//creation of threads and executor
		ExecutorService exs = Executors.newCachedThreadPool();
		Thread producer = new Thread(new Producer(buffer));
		Thread consumer = new Thread(new Consumer(buffer,startTime));

		//set a name for each thread
		producer.setName("Producer");
		consumer.setName("Consumer");

		//start each thread
		exs.execute(producer);
		exs.execute(consumer);

		//shutdown thread pool
		exs.shutdown();

	}
}
