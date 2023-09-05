package process;

public class Controller {

	public static void main(String[] args) {
		//prefilling seats
		FlightSeats flightSeats=new FlightSeats();
		flightSeats.fillSeats();

		
		// Starting main method
		Home home=new Home();
		home.menu();
	}
}
