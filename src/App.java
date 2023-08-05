import java.sql.*;
import java.util.Scanner;

public class App {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/MyBnB";
    private static final String DB_USERNAME = "root";
    
    private static final String DB_PASSWORD = "cscc43p1";
    private static final Scanner sc = new Scanner(System.in);

    private static Connection con;
    
    public static void main(String[] args) {
        try {
            // Create a connection to the database
        	con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Start the airbnb app
            startApp(con);

            // Close the database connection
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void startApp(Connection con) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    	
        // Provide a basic text interface to interact with app
        System.out.println("Welcome to our Airbnb App!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Admin");
        System.out.println("4. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                Users.loginUser(con);
                break;
            case 2:
                Users.createUser(con);
                break;
            case 3:
                // Reports.BookingsPer(con, true);
                // Reports.BookingsPer(con, false);
                Reports.ListingsPer(con, 1);
                Reports.ListingsPer(con, 2);
                Reports.ListingsPer(con, 3);
                Reports.RankHosts(con);
                break;
            case 4:
                clearScreen();
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                startApp(con);
        }
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
	
	
	public void removeListing(int listing, int hostID) {
		
	}
	
	public void changePrice(int listing, float newPrice) {
		
	}
	
	public void addAnemity(int listing, String anemity) {
		
	}
	
	public void createBooking(int listing, int Renter, String startDate, String endDate, float cost, String status) {
		
	}
	
	public void cancelBooking(int bookingId, int user) {
		
	}
}