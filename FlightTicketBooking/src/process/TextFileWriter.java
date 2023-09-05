package process;

import java.io.BufferedWriter;
import java.io.*;


public class TextFileWriter {

	public static  File FILE_NAME =new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booking_ticket.txt");
	private static BufferedWriter writer;
	private static TextFileWriter instance;
	private static String flightId;

	FlightSeats flight=new FlightSeats();

	private TextFileWriter(String flightId) {
		try {

			if(writer==null)
				writer = new BufferedWriter(new FileWriter(FILE_NAME));
		} catch (IOException e) {
			System.err.println("Error initializing FileWriter: " + e.getMessage());
		}
	}
	public static TextFileWriter getInstance(String flightId) {
		setFlightId(flightId);
		if (instance == null) {
			instance = new TextFileWriter(flightId);
		}
		return instance;
	}
	public void writeTextToFile(String[][] seats, String classTicket)  {
		try {
			writer.write(classTicket+"-"+flightId);

			writer.newLine();
			for (int i = 0; i < seats.length; i++) {
				for (int j = 0; j < seats[i].length; j++) {
					writer.write(seats[i][j]);
					if (j < seats[i].length - 1) {
						writer.write(",");
					}
				}
				writer.newLine();
			}
			writer.newLine();
			writer.flush();

		} catch (IOException e) {
			System.err.println("Error writing text to file: " + e.getMessage());
		}
	}
	
	
	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException e) {
			System.err.println("Error closing FileWriter: " + e.getMessage());
		}
	}

	public static String getFlightId() {
		return flightId;
	}
	public static void setFlightId(String flightId) {
		TextFileWriter.flightId = flightId;
	}

}
