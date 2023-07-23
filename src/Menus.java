import java.sql.Connection;
import java.util.Scanner;

public class Menus {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void hostMenu(Connection con, int hostID) {
        // Provide a basic text interface to interact with app
        System.out.println("Welcome Host! What would you like to do today?");
        System.out.println("1. View my listings");
        System.out.println("2. Create new listing");
        System.out.println("3. Update a current listing");
        System.out.println("4. Delete my account and all listings");
        System.out.println("5. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
                App.clearScreen();
            	App.readListings(con, hostID);
                break;
            case 2:
                // create listings
                App.clearScreen();
            	App.createListing(con, hostID);
                break;
            case 3:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                hostMenu(con, hostID);
        }
    }

    public static void renterMenu(Connection con, int renterID){
        // Provide a basic text interface to interact with app
        System.out.println("Welcome Renter! What would you like to do today?");
        System.out.println("1. View all listings");
        System.out.println("2. Create a booking");
        System.out.println("3. See my booking future bookings");
        System.out.println("4. See my booking past bookings");
        System.out.println("5. Delete my account and cancel all bookings");
        System.out.println("6. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
                App.clearScreen();
            	App.readListings(con, -1);
                break;
            case 6:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                renterMenu(con, renterID);
        }
    }
}
