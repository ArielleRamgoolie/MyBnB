import java.sql.Connection;
import java.util.Scanner;

public class Menus {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void hostMenu(Connection con, int hostID) {
        // Provide a basic text interface to interact with app
        App.clearScreen();
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
                Listings.readListings(con, hostID);
                hostMenu(con, hostID);
            case 2:
                // create listings
                App.clearScreen();
            	Listings.createListing(con, hostID);
                hostMenu(con, hostID);
            case 5:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                hostMenu(con, hostID);
        }
    }

    public static void renterMenu(Connection con, int renterID) {
        // Provide a basic text interface to interact with app
        App.clearScreen();
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
                App.clearScreen();
            	Listings.readListings(con, -1);
                searchMenu(con);
                renterMenu(con, renterID);
            case 2:
                Bookings.createBooking(con);
                App.clearScreen();
                renterMenu(con, renterID);
            case 3:
                Bookings.readBookings(con, true);
                App.clearScreen();
                renterMenu(con, renterID);
            case 4:
                Bookings.readBookings(con, false);
                App.clearScreen();
                renterMenu(con, renterID);
            case 6:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                renterMenu(con, renterID);
        }
    }

    public static void searchMenu(Connection con){
        System.out.println("Would you like to search listings? (Y/N)");
        String ans = sc.next();
        if (ans.equals("Y")){
            System.out.println("Which of the following would you like to search by:");
            System.out.println("1. Rank Listings by prices (Ascending)");
            System.out.println("2. Rank Listings by prices (Descending)");
            System.out.println("3. Search by distance (longitude and latitude)");
            System.out.println("4. Search by postal code");
            System.out.println("5. Search by address");
            System.out.println("6. Advance Search with multiple filters");
            System.out.println("7. Cancel search, return to menu");
        }

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                App.clearScreen();
            	Sorts.ascendingPrices(con);
                break;
            case 2:
                App.clearScreen();
            	Sorts.decendingPrices(con);
                break;
            case 3:
                App.clearScreen();
                System.out.println("Enter a min price for the listings you would like to see");
                int max = sc.nextInt();
            	Sorts.maxListing(max, con);
                break;
            case 4:
                App.clearScreen();
                System.out.println("Enter a max price for the listings you would like to see");
                int min = sc.nextInt();
            	Sorts.minListing(min, con);
                break;
            case 5:
                App.clearScreen();
                System.out.println("Which of the following types of listing you you like to see?");
                System.out.println("house");
                System.out.println("apartment");
                System.out.println("guesthouse");
                System.out.println("hotel");
                String type = sc.next();
            	Sorts.typeSort(type, con);
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                return;
        }
    }
}
