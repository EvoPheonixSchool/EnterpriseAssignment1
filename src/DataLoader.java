/* File: DataLoaderSingleThread.java
 * Author: Stanley Pieda
 * Date: August, 2017
 * Description: Single-threaded application to read a dataset file formatted as csv text
 *              and insert the data into a database
 * References:
 * Ram N. (2013).  Data Access Object Design Pattern or DAO Pattern [blog] Retrieved from
 * http://ramj2ee.blogspot.in/2013/08/data-access-object-design-pattern-or.html
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.PreparedStatement;

public class DataLoader {
	private long elapsedTime;
	private int recordsRead;
	private int recordsInserted;

	private Scanner cabbageScanner = null;
	private Connection con = null;

	private final String connectionString = "jdbc:mysql://localhost/assignment1";
	private final String username = "assignment1";
	private final String password = "password";


	public void processRecords() {
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
			openConnection();
			deleteAllCabbage();
			long startTime = System.currentTimeMillis();
			while(cabbageScanner.hasNext()){
				String line = cabbageScanner.nextLine(); // read raw data
				String[] fields = line.split(","); // split on delimiter
				Cabbage cabbage = new Cabbage();
				cabbage.setLineNumber(Integer.parseInt(fields[0]));
				cabbage.setAlpha(fields[1]);
				cabbage.setBeta(fields[2]);
				cabbage.setCharlie(fields[3]);
				cabbage.setDelta(fields[4]);
				recordsRead++;
				insertCabbage(cabbage);
				recordsInserted++;
				if(recordsInserted % 100 == 0) {
					System.out.printf("%d records read and inserted%n", recordsInserted);
				}
			}

			long endTime = System.currentTimeMillis();

			elapsedTime = endTime - startTime;
			int minutes = (int)elapsedTime / 1000 / 60;
			int seconds = (int)elapsedTime / 1000 % 60;
			
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");

			System.out.printf("%d records read%n", recordsRead);
			System.out.printf("%d records inserted%n", recordsInserted);
			System.out.printf("%d mileseconds elapsed%n", elapsedTime);
			System.out.printf("%02d minutes, %02d seconds, %03d millisecs%n", minutes, seconds, elapsedTime % 1000);
			System.out.printf("Program by: Stanley Pieda run on %s%n",dateTime.format(format));
			//System.out.printf("Buffer type is %s%n", buffer.getClass().getName()); // tip for students threaded program
		}
		finally {
			closeFile();
			closeConnection();
		}

	}

	private void openFile() {
		try {
			cabbageScanner = new Scanner(new FileReader(new File("DataSet17F.csv")));
		}
		catch(IOException ex){
			System.out.println("Problem opening file: "
					+ ex.getMessage());
		}
	}
	
	private void closeFile() {
		try {
			if(cabbageScanner != null) {cabbageScanner.close();}
		}
		catch(Exception ex) {
			System.out.println("Problem closing file: "
					+ ex.getMessage());
		}
	}

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

	private void closeConnection() {
		try{ if(con != null){ con.close(); }}
		catch(SQLException ex){System.out.println(ex.getMessage());}
	}

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
