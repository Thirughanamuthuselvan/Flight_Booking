package process;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextFileReader {

	private static  File FILE_NAME =new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booking_ticket.txt");

	HashMap<String,List<String>> bookedIdMap=new HashMap<>();
	ArrayList<String> passengerDeatils=new ArrayList<>();
	
	public void viewTicket(String string) {
//		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//			String line;
			int temp=0;
			boolean flag= false;
			Flights flight=new Flights();
			System.err.println("For Booking.....");
//			System.out.println("-----------------------------------------");
//			System.out.println("  A B C D E F G H I J K L M N O P Q R S T");
//			System.out.println("-----------------------------------------");
			
			int[] rowCol=flight.getRowAndColValue(string);
			int numRows=rowCol[0],numCols=rowCol[1];
			String[][] seatArray=flight.getAvailSeat(rowCol[0],rowCol[1],string);
		
			char startChar='A';
			System.out.println("-----------------------------------------");
			System.out.print("  ");
			for(int i=0;i<numCols;i++)
			System.out.print((char)(startChar+i)+" ");
			System.out.println();
			System.out.println("-----------------------------------------");
			
			for (int i = 0; i < numRows; i++) {
				System.out.print(i+1+" ");
				for (int j = 0; j < numCols; j++) {
					System.out.print(seatArray[i][j] + " ");
				}
				System.out.println();
			}
			System.out.println("-----------------------------------------");
//		} catch (IOException e) {
//			System.err.println("Error reading file: " + e.getMessage());
//		}
	}


	public long checkPrice(String bookingFlightNumber, String seatClass,int count) {
		
		// Price calculating for booking ticket
		long economyTicketPrice=1000,businessTicketPrice=2000;
		String fileName="F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booked_"+bookingFlightNumber+".txt";
		File newFile=new File(fileName);
		if(newFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(newFile))) {
				String line = reader.readLine();
				if(line==null) {
					if(seatClass.charAt(0)=='B') {
						businessTicketPrice=2000;
						return count*businessTicketPrice;
					}
					if(seatClass.charAt(0)=='E') {
						economyTicketPrice=1000;
						return count*economyTicketPrice;
					}
				}
				
		        // Surge pricing : After each successful booking, price of the ticket increases by INR 100 for Economy class and INR 200 for Business class 
				while (line != null) {
					if(line.charAt(0)=='B')
						businessTicketPrice+=200;
					if(line.charAt(0)=='E')
						economyTicketPrice+=100;
					line=reader.readLine();
				}

			}

			catch(Exception e) {
				System.err.println("Exception in price checking.."+e.getMessage()); 
			}
		}
		if(seatClass.equals("Business")) {
			return count*businessTicketPrice;
		}
		else {
			return count*economyTicketPrice;
		}

	}

	
}
