import java.sql.Connection;
import java.util.Scanner;

public class Menus {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void hostMenu(Connection con, int hostID) {
        // Provide a basic text interface to interact with app
        System.out.println("Welcome Host! What would you like to do today?");
        System.out.println("1. View my listings");
        System.out.println("2. Create new listing");
        System.out.println("3. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
                System.out.print("\033[H\033[2J");
                System.out.flush();
            	App.readListings(con, hostID);
                break;
            case 2:
                // create listings
                System.out.print("\033[H\033[2J");
                System.out.flush();
            	App.createListing(con, hostID);
                break;
            case 3:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                hostMenu(con, hostID);
        }
    }

}
