package process;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FlightSeats {

	File file=new File("Flights");
	File bookingFile = new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking");
	static HashMap<String,List<String>> idMap=new HashMap<>();
	static ArrayList<String> flightIds=new ArrayList<>();

	FlightSeats(){

	}
	public   void fillSeats() {
		//create booking file for booking information
		getIdmap();

		generateRowColumn();
	}

	public void generateRowColumn() {
		for (String key : idMap.keySet()) {
			List<String> value = idMap.get(key);
			for(String cls:value) {
				String[] details=cls.split("[:{}, ]");
				int row=Integer.valueOf(details[6]);
				int column=(Integer.valueOf(details[2]))+(Integer.valueOf(details[3]))+(Integer.valueOf(details[4]));
				generateSeatingArrangements(details[0],Integer.valueOf(details[2]),Integer.valueOf(details[3]),Integer.valueOf(details[4]),row,column,key);

			}
		}

	}

	public void generateSeatingArrangements(String classTicket,Integer i, Integer j, Integer k, int row, int column, String key) {
		String[][] seats=new String[row][column];
		for(int a=0;a<row;a++) {
			for(int b=0;b<i;b++) {
				if(b==0) seats[a][b]="W";
				else if(b==i-1) seats[a][b]="A";
				else seats[a][b]="M";
			}
			for(int c=0;c<j;c++) {
				if(c==0 || c==j-1) seats[a][c+i]="A";
				else seats[a][i+c]="M";
			}
			for(int d=0;d<k;d++) {
				if(d==0) seats[a][i+j+d]="A";
				else if(d==k-1) seats[a][i+j+d]="W";
				else seats[a][i+j+d]="M";
			}
		}
		//		System.out.println(idMap);
		TextFileWriter textFileWriter=TextFileWriter.getInstance(key);
		textFileWriter.writeTextToFile(seats,classTicket);

	}

	public static String findTextFile(String key) {
		try {
			Path directory = Paths.get("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking");  // Specify the directory where the text files are located
			String fileName = Files.walk(directory)
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.contains(key))
					.findFirst()
					.orElse(null);

			if (fileName != null) {
				return directory+"\\"+fileName.substring(0, fileName.lastIndexOf('.'))+".txt";
			} else {
				return "File not found.";
			}
		} catch (IOException e) {
			return "Error finding text file: " + e.getMessage();
		}
	}
	public  void getIdmap() {

		String flightNumber;

		if(file.isDirectory()) {
			File[] flightNames=file.listFiles();
			getAllFlightIds();
			for(int i=0;i<flightNames.length;i++) {
				String[] flight=flightNames[i].getName().split("[-.]");
				if(flight[0].equals("Flight")) {
					flightNumber=flight[1];
					getFlightSeatsFromInput(flightNumber);
				}
			}

		}
		else {
			System.err.println("No such directory find..");
		}
	}

	public void getFlightSeatsFromInput(String flightNumber) {
		try {
			ArrayList<String> flightSeats=new ArrayList<>();
			if(file.isDirectory()) {

				File[] flightNames=file.listFiles();
				for(int i=0;i<flightNames.length;i++) {
					String[] flight=flightNames[i].getName().split("[-]");
					if(flight[0].equals("Flight") && flight[1].equals(flightNumber)) {
						try (BufferedReader br = new BufferedReader(new FileReader(flightNames[i])))
						{
							String line;
							while ((line = br.readLine()) != null) {
								flightSeats.add(line);
							}
							idMap.put(flight[1],flightSeats);
						} catch (IOException e) {
							System.err.println("Exception in getFlightSeatsFromInput..!");
						}
					}
				}

			}
			else {
				System.err.println("No such directory find..");
			}

		}
		catch(Exception e) {
			System.err.println("FlightSeats class Exception..");
		}
	}
	public  void getAllFlightIds() {
		if(file.isDirectory()) {
			File[] flightNames=file.listFiles();
			for(int i=0;i<flightNames.length;i++) {
				String[] flight=flightNames[i].getName().split("[-.]");
				if(flight[0].equals("Flight")) {
					flightIds.add(flight[1]);
				}
			}
			createFile();
			createBookedFile();
		}
		else {
			System.err.println("No such directory find..");
		}
	}

	public void createFile() {

		File newFile=new File("F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booking_ticket.txt");
		if(!newFile.exists())
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				System.out.println("failed to create file..");
			}
	}
	public void createBookedFile() {

		for(String i:flightIds) {
			String fileName="F:\\eclipse-workspace\\FlightTicketBooking\\Flights\\booking\\booked_"+i+".txt";
			File newFile=new File(fileName);
			if(!newFile.exists())
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					System.out.println("failed to create file..");
				}
		}
	}
}




