package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Flights {

	String source;
	String destination;
	String seatClass;
	String bookingId;
	long phone_number;
	int totalTickets;
	String flightNumber;
	static Scanner input=new Scanner(System.in);
	TextFileReader tickets=new TextFileReader();
	private static BufferedWriter writer;
	HashMap<String,List<String>> bookedIdMap=new HashMap<>();

	List<String> passengerDeatils=new ArrayList<>();
	List<String> passengerDeatilsList=new ArrayList<>();
	List<String> businessSeatNumbers=new ArrayList<>();
	List<String> economySeatNumbers=new ArrayList<>();
	List<String> seatNumbersList=new ArrayList<>();
	FlightSeats seat=new FlightSeats();
	Flights(String source, String destination){
		this.source=source.toLowerCase();
		this.destination=destination.toLowerCase();
	}
	Flights(){

	}
	public void showFlights() {
		// View All Flights Task 1
		while(true) {
			boolean flag=false;
			try {
				int flightCount=0;

				File file=new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights");
				if(file.isDirectory()) {
					File[] flightNames=file.listFiles();
					for(int i=0;i<flightNames.length;i++) {
						String[] flight=flightNames[i].getName().split("[-.]");

						if(flight[0].equals("Flight")&& source.equals(flight[2].toLowerCase()) && destination.equals(flight[3].toLowerCase()))
						{
							if(flight[0].equals("Flight")) {
								flightCount++;
								System.err.println(flightCount+". "+flight[1]+" "+flight[2]+" "+flight[3]);
								flightNumber=flight[1];

								flag=true;
							}
						}
					}
					if(flag==false) {
						System.err.println("   Oops..! Flight is not available.. \n");
						Home home=new Home();
						home.findFlights();
					}

					System.out.println("Number Of flights: "+flightCount);
				}
				else {
					System.err.println("No such directory find..");
				}
				System.err.println("For Booking Enter Flight Number:");
				String bookingFlightNumber=input.next();

				bookedIdMap=getBookedFileData(bookingFlightNumber);
				if(FlightSeats.flightIds.contains(bookingFlightNumber)) {
					viewAvailableTicket(bookingFlightNumber);
					if(requestBooking()) {
						bookTicket(bookingFlightNumber);
					}
				}
				else
				{
					System.out.println("Enter valid Flight Number..");
					showFlights();
				}
			}

			catch(Exception e) {
				System.err.println("Flights class Exception..");
			}
		}
	}

	public void viewAvailableTicket(String bookingFlightNumber) {

		while(true) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Press Economy/ Business class");
			System.out.println("1.Business");
			System.out.println("2.Economy");
			System.out.println("3.View Flights");
			System.out.println("4.View Business class Flights");
			System.out.println("5.Flight Summary");
			System.out.println("0.Exit");
			System.out.println("-------------------------------------------------------------");
			try {
				int val=input.nextInt();
				switch(val) {
				case 1:
					seatClass="Business";
					tickets.viewTicket("Business-"+bookingFlightNumber);
					break;
				case 2:
					seatClass="Economy";
					tickets.viewTicket("Economy-"+bookingFlightNumber);
					break;
				case 3:
					viewAllFlights();
					break;
				case 4:
					viewBusinessClassFlights();
					break;
				case 5:
					System.out.println("Flight Number : "+ bookingFlightNumber);
					System.out.println("Passengers    : "+ passengerDeatilsList.size());
					break;
				case 0:
					Home home=new Home();
					home.menu();
				}break;
			}

			catch(Exception e) {
				System.err.println("Exception in flights class..");
				showFlights();
			}
			finally {
				System.out.println();
			}
		}

	}
	public HashMap<String, List<String>> getBookedFileData(String bookingFlightNumber) {

		// getBooked file data from booked flights
		String fileName=seat.findTextFile(bookingFlightNumber);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;

			int row=0;
			while ((line = reader.readLine()) != null) {
				if(!line.isEmpty()) {
					ArrayList<String> passengers=new ArrayList<>();
					String[] key=line.split(":");
					String value[]=key[1].split("[$ ,]+");
					for(String s:value) {
						passengers.add(s.substring(0,s.length()-2));
					}
					bookedIdMap.put(key[0], passengers);
				}
			}

		}
		catch(Exception e) {
			System.err.println("Exception in storing from file.."+e.getMessage()); 
		}
		return bookedIdMap;
	}

	public void viewAllFlights() {
		try {
			File file=new File("Flights");
			if(file.isDirectory()) {
				File[] flightNames=file.listFiles();
				int fileLength=flightNames.length-1;
				System.out.println("Number Of flights: "+fileLength);
				for(int i=0;i<flightNames.length;i++) {
					String[] flight=flightNames[i].getName().split("[-.]");
					if(flight[0].equals("Flight"))
						System.out.println(flight[1]+" "+flight[2]+" "+flight[3]);
				}
				System.out.println("");

			}
			else {
				System.err.println("No such directory find..");
			}
			System.out.println();
			System.out.print("Check For availablity?(yes/no) : ");
			String userInput = input.next();

			if (userInput.equalsIgnoreCase("yes")) {
				String flightNo=input.next();
				String name=FlightSeats.findTextFile(flightNo);
			} else {
				viewAvailableTicket(flightNumber);
			}
			System.out.println("-------------------------------------------------------------");

		}
		catch(Exception e) {
			System.err.println("Flights class Exception..");
		}
	}

	public void bookTicket(String bookingFlightNumber) {
		flightNumber=bookingFlightNumber;
		System.err.println("Enter Number of Seats to Book");
		int numOfSeats=input.nextInt();
		try {

			System.out.println("Enter Phone Number: ");
			phone_number=input.nextLong();
			for(int i=0;i<numOfSeats;i++) {
				System.out.print("Enter Passenger "+(i+1)+" Name: ");
				String name=input.next();

				System.out.print("Enter age: ");
				int age =input.nextInt();

				String classId=seatClass +"-"+bookingFlightNumber;
				while(true) {
					System.out.print("Enter preferred seat(Example: 1_D, 2_B) : ");
					String seatNo=input.next();


					int[] rowCol=getRowAndColValue(classId);
					boolean isAvail=seatChecking(rowCol[0],rowCol[1],seatNo);

					String regexSeat = "\\d{1,2}_[A-Z]";
					boolean isMatchSeat = Pattern.matches(regexSeat, seatNo);
					//					 && checkAvailability(seatNo);
					if(isMatchSeat && isAvail ) {
						if(checkAvailability(seatNo)){
							seatNumbersList.add(seatNo);
							System.out.print("Meal Preference: ");
							String meal=input.next();

							String temp=name+"&&"+(age+"")+"&&"+seatNo+"&&"+meal;
							passengerDeatils.add(temp);

							System.out.println();
							break;
						}
						else {
							System.out.println("The seat " + seatNo + " is already booked..\n");
						}
					}
					else {
						System.err.println("Enter valid seat");
					}
				}

			}

			System.out.println("To confirm booking (1/0)");
			int val=input.nextInt();

			if(val==1) {
				paymentInitial(bookingFlightNumber,numOfSeats);
			}
			else {
				Home home=new Home();
				home.menu();

			}
		}
		catch(Exception e) {
			System.out.println("Please enter vaild Input");
		}
	}

	private boolean seatChecking(int row, int col, String seatNo) {
		String seatingArray[][] = new String[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				int number = i + 1;
				char letter = (char) ('A' + j);
				String value = number + "_" + letter;
				seatingArray[i][j] = value;
			}
		}

		//		for (int i = 0; i < row; i++) {
		//			for (int j = 0; j < col; j++) {
		//				System.out.print(seatingArray[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		boolean containsString = false;
		for (String[] s : seatingArray) {
			for (String element : s) {
				if (element.equals(seatNo)) {
					containsString = true;
					break;
				}
			}
			if (containsString) {
				break;
			}
		}

		if (containsString) {
			return true;
		} else {
			return false;
		}
	}
	private boolean checkAvailability(String seatNo) {
		for (Map.Entry<String, List<String>> entry : bookedIdMap.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();
			for(String val:values) {
				String valueArray[]=val.split("&&");
				if(key.charAt(0)=='E')
					economySeatNumbers.add(valueArray[2]);
				if(key.charAt(0)=='B')
					businessSeatNumbers.add(valueArray[2]);
			}
		}
		if(businessSeatNumbers.isEmpty() || economySeatNumbers.isEmpty())
			return true;
		if(businessSeatNumbers.contains(seatNo)&& seatClass.charAt(0)=='B')
		{
			return true;
		}
		if(economySeatNumbers.contains(seatNo)&& seatClass.charAt(0)=='E') {
			return true;
		}

		return false;
	}

	


	private void viewBusinessClassFlights() {

		//view only business class flights only

		String directoryPath = "F:\\eclipse-workspace\\FlightTicketBooking\\Flights"; // Specify the directory path where the text files are located

		File directory = new File(directoryPath);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().startsWith("Flight")) {
						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String line = reader.readLine();
							if (line != null && line.trim().isEmpty()) {
								System.out.println("File " + file.getName() + " is empty.");
							} else {
								String nextLine = reader.readLine();
								if (nextLine == null) {
									System.out.println(file.getName()); 
								}
							}
						} catch (IOException e) {
							System.err.println("Error reading file: " + file.getName());
						}
					}
				}
			}
		} else {
			System.err.println("Invalid directory path: " + directoryPath);
		}

	}
	public  boolean requestBooking() {

		System.out.print("Do you want to book tickets..?(1/0)");
		int in=input.nextInt();
		try {
			switch(in) {
			case 1:
				return true;
			case 0:
				return false;
			}
		}
		catch(Exception e) {
			System.out.println();
		}
		return false;

	}


	public void generateBookingId(String bookingFlightNumber, String seatClass, long phone_number) {

		HashMap<String,List<String>> tempBookedIdMap=new HashMap<>();
		bookingId=seatClass.charAt(0)+""+bookingFlightNumber+"@"+phone_number;
		tempBookedIdMap.put(bookingId, passengerDeatilsList);
		String fileName=FlightSeats.findTextFile(bookingFlightNumber);
		System.out.println(tempBookedIdMap);
		writeTextToFile(tempBookedIdMap,fileName);

	}

	public void paymentInitial(String bookingFlightNumber, int numOfSeats) {
		// payment initialisd
		Home home=new Home();
		long price=tickets.checkPrice(bookingFlightNumber,seatClass,numOfSeats);
		long otherCost=processWindowOrAisle(bookingFlightNumber);
		price+=otherCost;
		System.out.println("You need to be paid..INR "+price+" \n");
		System.out.print("  Press 1 to continue..");
		int temp=input.nextInt();
		if(temp==1) {
			addPriceToList(price);
			generateBookingId(bookingFlightNumber,seatClass,phone_number);
			System.err.println("Congratulations! Your bookingId :"+bookingId+" your ticket is booked Successfully..\n");
			System.err.println("Thank You for Using ZoFlight Booking System..\n");
			System.out.println();
			home.menu();
		}
		else {
			home.menu();
		}
	}

	private long processWindowOrAisle(String bookingFlightNumber) {
		String classId=seatClass+"-"+bookingFlightNumber;
		int[] rowCol=new int[2];
		rowCol=getRowAndColValue(classId);
		int numRows=rowCol[0],numCols=rowCol[1];
		String array[][] =getAvailSeat(rowCol[0],rowCol[1],classId);
		String seatingArray[][] = new String[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				int number = i + 1;
				char letter = (char) ('A' + j);
				String value = number + "_" + letter;
				seatingArray[i][j] = value;
			}
		}
		// Print the array
//								for (int i = 0; i < numRows; i++) {
//									for (int j = 0; j < numCols; j++) {
//										System.out.print(array[i][j] + " ");
//									}
//									System.out.println();
//								}
		long cost=0;
		for(String s:seatNumbersList) {
			for (int m= 0; m < numRows; m++) {
				for (int n = 0; n < numCols; n++) {
					if(seatingArray[m][n].equals(s)) {
						if(array[m][n].equals("W") || array[m][n].equals("A")) {
							cost+=100;
							break;
						}
					}
				}
			}
		}
		return cost;
	}

	public String[][] getAvailSeat(int numRows, int cols, String classId) {
		String array[][]=new String[numRows][cols];
		try (BufferedReader reader = new BufferedReader(new FileReader("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booking_ticket.txt"))) {
			String line;

			// Skip the first line
			String firstLine=reader.readLine();
			while(firstLine!=null) {
				//System.out.println(firstLine);
				if(firstLine.equals(classId)) {
					int row = 0;
					while ((line = reader.readLine()) != null) {
						if(line.isEmpty()) break;
						String[] values = line.split(",");
						for (int col = 0; col < cols; col++) {
							array[row][col] = values[col];
						}
						row++;
					}

					//					 Print the array for verification
					//					for (int i = 0; i < numRows; i++) {
					//						for (int j = 0; j < cols; j++) {
					//							System.out.print(array[i][j] + " ");
					//						}
					//						System.out.println();
					//					}
				}
				firstLine=reader.readLine();
			}
		} catch (Exception e) {
			System.out.println("Exception in get avail seat method");
		}
		return array;
	}

	public int[] getRowAndColValue(String classId) {
		int[] rowCol=new int[2];
		try (BufferedReader reader = new BufferedReader(new FileReader("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booking_ticket.txt"))) {
			int numRows=0, Cols=0;
			// Read the first line to determine the number of columns
			String firstLine=reader.readLine();
			while(firstLine!=null) {
				//				System.out.println(firstLine);
				if(firstLine.equals(classId)) {
					firstLine = reader.readLine();
					String[] numCols = firstLine.split(",");
					int i=0;
					for(String s:numCols) {
						i++;
					}
					Cols=i;
					// Count the number of rows
					numRows = 0;
					while ((firstLine = reader.readLine()) != null) {
						if(firstLine.isEmpty()) break;;
						numRows++;

					}
					numRows++;
				}
				firstLine=reader.readLine();
			}
			rowCol[0]=numRows;
			rowCol[1]=Cols;
		} catch (Exception e) {
			System.out.println("Exception geting row column");		
		}
		return rowCol;
	}


	public void addPriceToList(long price) {
		// add price to list
		for(String data:passengerDeatils) {
			passengerDeatilsList.add(data+"&&"+price+"$");
		}
	}

	public void writeTextToFile(HashMap<String, List<String>> bookedIdMap,String name) {
		// write text to booked files 
		try {

			if(writer==null)
				writer = new BufferedWriter(new FileWriter(name));

			for (Map.Entry<String, List<String>> entry : bookedIdMap.entrySet()) {
				String key = entry.getKey();
				List<String> values = entry.getValue();

				writer.write(key + ":");
				for (String value : values) {
					writer.write(value + ", ");
				}
				writer.newLine();
				writer.flush();
			}

		}
		catch(Exception e) {
			System.out.println("Exxception in writing file");
		}
	}

	public void requestForCancellation() {

		System.out.print("How many tickets do you want to cancel? ");
		int numOfTickets = input.nextInt();

		try {
			if (numOfTickets > 0 && numOfTickets<=totalTickets ) {

				System.out.println("Ticket cancellation process initiated for " + numOfTickets + " ticket(s).");
				System.out.println();
				System.out.println("Cancellation Charge per seat is INR 200\n");
				long paid=getPrice(bookingId);
				long refundedAmount=paid-(numOfTickets*200);
				System.out.println("Refunded Amount :"+refundedAmount);
				updateFile(bookingId, numOfTickets);
				System.out.println("\n\tThank you for using ZoFlight System..");
			} else {
				System.out.println("Enter valid count from 1 to "+totalTickets);
			}
		}
		catch(Exception e) {
			System.out.println("Enter valid input");
		}
	}

	public long getPrice(String bookingId2) {

		HashMap<String,List<String>> map=new HashMap<>();
		File fileName=new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\");
		try {
			if(fileName.isDirectory()) {

				File[] bookedflightNames=fileName.listFiles();
				for(int i=0;i<bookedflightNames.length;i++) {
					ArrayList<String> list=new ArrayList<>();
					String[] flight=bookedflightNames[i].getName().split("[_]");
					if(flight[0].equals("booked") ) {
						try (BufferedReader br = new BufferedReader(new FileReader(bookedflightNames[i])))
						{
							String line;
							while ((line = br.readLine()) != null) {
								String[] splitId=line.split("[:]+");
								list.add(splitId[1]);
								map.put(splitId[0], list);
							}
						} catch (IOException e) {
							System.err.println("Exception in getFlightSeatsFromInput..!");
						}
					}
				}

			}
			else {
				System.err.println("No such directory find..");
			}
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				String key = entry.getKey();
				if(key.equals(bookingId2)) {
					List<String> value = entry.getValue();

					String[] v=value.get(0).split("[$]");

					int k=1;
					for (String str : v) {
						if(v.length==k)
							break;
						String[] details=str.split("&&");
						return Long.valueOf(details[4]);

					}
				}
			}

		}
		catch(Exception e) {
			System.err.println("FlightSeats class Exception..");
		}return 0;

	}

	private void updateFile(String bookingId2, int numOfTickets) {

		String fileName=seat.findTextFile(bookingId2.substring(1,5));
		System.out.println(fileName);
	}
}
