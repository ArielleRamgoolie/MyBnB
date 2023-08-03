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
        System.out.println("Welcome Renter! What would you like to do today?");
        System.out.println("1. View all listings");
        System.out.println("2. Sort listings");
        System.out.println("3. Create a booking");
        System.out.println("4. Cancel a booking");
        System.out.println("5. See my booking future bookings");
        System.out.println("6. See my booking past bookings");
        System.out.println("7. Delete my account and cancel all bookings");
        System.out.println("8. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                App.clearScreen();
            	Listings.readListings(con, -1);
                renterMenu(con, renterID);
            case 2:
                App.clearScreen();
            	sortingMenu(con);
                App.clearScreen();
                renterMenu(con, renterID);
            case 3:
                Bookings.createBooking(con);
                App.clearScreen();
                renterMenu(con, renterID);
            case 4:
                Bookings.cancelBooking(con);
                App.clearScreen();
                renterMenu(con, renterID);
            case 5:
                Bookings.readBookings(con, true);
                App.clearScreen();
                renterMenu(con, renterID);
            case 6:
                Bookings.readBookings(con, false);
                App.clearScreen();
                renterMenu(con, renterID);
            case 7:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                renterMenu(con, renterID);
        }
    }

    public static void sortingMenu(Connection con){
        System.out.println("What would you like to sort listings by?");
        System.out.println("1. Sort by price (ascending)");
        System.out.println("2. Sort by price (decending)");
        System.out.println("3. Listings above a certain price");
        System.out.println("4. Listings below a certain price");
        System.out.println("5. Sort by type of listing");
        System.out.println("6. Sort by type of distance (longitude and latitude)");
        System.out.println("7. Sort by multiple filters");
        System.out.println("8. Exit sort");

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
