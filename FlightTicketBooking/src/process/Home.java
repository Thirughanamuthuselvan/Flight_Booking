package process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Home {
	static Scanner input=new Scanner(System.in);
	Flights flights=new Flights();
	public void menu() {


		while(true) {
			//Thread is initiated for booking system..
			ThreadCalling thread=new ThreadCalling();
			String greetings ="   \tWelcome to ZoFlight Booking System..!";
			thread.welcome(greetings);

			manageAdminOrUser();

		}
	}

	public void manageAdminOrUser() {
		while(true) {
			//Manage admin or user
			System.out.println("\t\t1. Admin");
			System.out.println("\t\t2. User");
			System.out.println("\t\t0. Exit");

			System.out.println("-------------------------------------------------------------");

			int value=0;
			try {
				value=input.nextInt();
			}
			catch(Exception e) {
				System.err.println("\tPlease Enter 1 or 2 or 0");
				input.nextLine(); 
				manageAdminOrUser();
			}
			switch(value) {
			case 1:
				adminLogin();
				break;
			case 2:
				userLogin();
				findFlights();
				break;
			case 0:
				break;
			}
		}
	}

	public void userLogin() {
		//user login details
		System.err.print("If already booked? (yes/no): ");
		try {
			String value=input.next();

			if(value.equalsIgnoreCase("yes")) {
				System.out.print("Enter BookingId: ");
				String bookingId=input.next();
				if(bookingId.charAt(0)=='B' || bookingId.charAt(0)=='E') {
					checkBookedId(bookingId);
				}
				System.out.print("Do you want to cancel your ticket? (yes/no): ");
				String userInput = input.next();

				if (userInput.equalsIgnoreCase("yes")) {
					System.out.println("Ticket cancellation process initiated.");
					flights.bookingId=bookingId;
					flights.requestForCancellation();
				} else {
					System.out.println("Ticket cancellation not requested.");
					menu();
				} 

			}
			else
				findFlights();
		}
		catch(Exception e) {
			System.err.println("Please Enter Valid Input..!");
			input.nextLine();
			userLogin();
		}


	}


	public void adminLogin() {
		String adminUsername="admin";
		String adminPassword="admin";

		System.out.print("Enter Username: ");
		String inputUserName=input.next();

		System.out.print("Enter Password: ");
		String inputPass=input.next();

		if(adminUsername.equals(inputUserName) && adminPassword.equals(inputPass)) {
			System.out.println("-------------------------------------------------------------");
			findFlights(adminUsername);
		}
		else {
			System.err.println("Enter valid Username or Password..!");
			System.out.println("-------------------------------------------------------------");
			input.nextLine();
			adminLogin();
		}
	}

	public  void findFlights() {
		while(true) {

			// Task 2 :  search flights using source and destination
			String source, destination;
			System.out.println("-------------------------------------------------------------");
			System.out.print("\tFrom Source (Ex: chennai)   : ");
			source=input.next();
			System.out.print("\n\tTo Destination (Ex: Mumbai) : ");
			destination=input.next();

			System.out.println("-------------------------------------------------------------");
			showAvailableFlights(source, destination);
		}
	}
	public void showAvailableFlights(String source, String destination) {
		Flights flights=new Flights(source, destination);
		flights.showFlights();
	}

	public void findFlights(String adminUsername) {
		while(true) {
			int adminInput=0;
			System.out.println("\t1.View All flights\n\t");
			System.out.println("\t0. Exit");
			System.out.println("-------------------------------------------------------------");

			try {
				adminInput=input.nextInt();

			}
			catch(Exception e){
				System.err.println("Please Enter Valid Input..!");
				input.nextLine();
				findFlights(adminUsername);
			}if(adminInput==0 || adminInput==1 || adminInput==2) {
				switch(adminInput) {
				case 1:
					flights.viewAllFlights();
					findFlights("admin");
					break;

				case 0:
					menu();
					break;
				}
			}
			else {
				System.err.println("Please Enter Valid Input..!");
				findFlights("admin");
			}
		}
	}

	public void showBookingConfirmation() {

		System.out.println("Congratulations! your ticket is booked Successfully..");
		menu();
	}
	public void checkBookedId(String bookingId) {
		HashMap<String,List<String>> bookedIdMap=new HashMap<>();

		ArrayList<String> passengerDeatils=new ArrayList<>();

		String name=FlightSeats.findTextFile(bookingId.substring(1,5));
		try (BufferedReader br = new BufferedReader(new FileReader(name)))
		{
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitId=line.split("[:]+");
				passengerDeatils.add(splitId[1]);
				bookedIdMap.put(splitId[0], passengerDeatils);
			}
		} catch (IOException e) {
			System.err.println("Exception in getFlightSeatsFromInput..!");
		}



		for (Map.Entry<String, List<String>> entry : bookedIdMap.entrySet()) {
			String key = entry.getKey();
			if(key.equals(bookingId)) {
				List<String> value = entry.getValue();

				String[] v=value.get(0).split("[$]");
				flights.totalTickets=v.length-1;
				System.out.println(flights.totalTickets);
				int k=1;
				for (String str : v) {

					if(v.length==k)
						break;
					String[] details=str.split("&&");
					if(k==1)
						System.out.println("Passenger "+k+" Name   : " + details[0]);
					else
						System.out.println("Passenger "+k+" Name    : " + details[0].substring(2,details[0].length()));
					System.out.println("Age                 : " + details[1]);
					System.out.println("Seat Number         : " + details[2]);
					System.out.println("Meal Preferred      : " + details[3]);
					System.out.println("-------------------------------------------------------------");

					k++;
				}

			}
		}

	}


}


