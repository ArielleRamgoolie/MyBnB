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
                    System.out.println("Welcome to your Airbnb App Admin! What would you like to do today:");
                    System.out.println("1. Total bookings in a specific date range by city");
                    System.out.println("2. Total bookings in a specific date range by postal code");
                    System.out.println("3. Total number of listings per country");
                    System.out.println("4. Total number of listings per country per city");
                    System.out.println("5. Total number of listings per country per city per postal code");
                    System.out.println("6. Rank hosts by number of listings of listings per country");
                    System.out.println("7. Rank renters by number of bookings in a specific time period");
                    System.out.println("8. Rank renters by number of bookings in a specific time period per city");
                    System.out.println("9. Rank renters who have made at least 2 bookings in the year");
                    System.out.println("10. Hosts with the largest number of cancellations per year");
                    System.out.println("11. Renters with the largest number of cancellations per year");
                    System.out.println("12. Exit");
                    int option = sc.nextInt();
                    sc.nextLine();
                    switch (option) {
                        case 1:
                            Reports.BookingsPer(con, true);
                            App.startApp(con);
                        case 2:
                            Reports.BookingsPer(con, false);
                            App.startApp(con);
                        case 3:
                            Reports.ListingsPer(con, 1);
                            App.startApp(con);
                        case 4:
                            Reports.ListingsPer(con, 2);
                            App.startApp(con);
                        case 5:
                            Reports.ListingsPer(con, 3);
                            App.startApp(con);
                        case 6: 
                            Reports.RankHosts(con);
                            App.startApp(con);
                        case 7: 
                            Reports.BookingsPerRenter(con, false);
                            App.startApp(con);
                        case 8: 
                            Reports.BookingsPerRenter(con, true);
                            App.startApp(con);
                        case 9: 
                            Reports.BookingsPerRenterInYear(con);
                            App.startApp(con);
                        case 10: 
                            Reports.HostCancelsPerYear(con);
                            App.startApp(con);
                        case 11:
                            Reports.RenterCancelsPerYear(con);
                            App.startApp(con);
                        case 12: 
                            System.out.println("Goodbye!");
                            break;
                    }
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
}