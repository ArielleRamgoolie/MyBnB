import java.sql.Connection;
import java.util.Scanner;

public class Menus {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void hostMenu(Connection con, int hostID) {
        App.clearScreen();
        System.out.println("Welcome Host! What would you like to do today?");
        System.out.println("1. View my listings");
        System.out.println("2. Create new listing");
        System.out.println("3. Update a current listing");
        System.out.println("4. Cancel a booking");
        System.out.println("5. See my booking future bookings");
        System.out.println("6. See my booking past bookings");
        System.out.println("7. Delete my account and all listings");
        System.out.println("8. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
                App.clearScreen();
                Listings.readListings(con);
                hostMenu(con, hostID);
            case 2:
                // create listings
                App.clearScreen();
            	Listings.createListing(con, hostID);
                hostMenu(con, hostID);
            case 3:
                // create listings
            	Listings.updateListing(con);
                hostMenu(con, hostID);
            case 4:
                Bookings.cancelBooking(con);
                App.clearScreen();
                hostMenu(con, hostID);
            case 5:
                Bookings.readBookings(con, true);
                App.clearScreen();
                hostMenu(con, hostID);
            case 6:
                Bookings.readBookings(con, false);
                App.clearScreen();
                hostMenu(con, hostID);
            case 7:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                hostMenu(con, hostID);
        }
    }

    public static void renterMenu(Connection con, int renterID) {
        App.clearScreen();
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
            	Listings.readListings(con);
                searchMenu(con);
                renterMenu(con, renterID);
            case 2:
                System.out.println("sorting is currently being updated");
                break;
            case 3:
                Bookings.createBooking(con,-1,0);
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
            case 8:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                renterMenu(con, renterID);
        }
    }

    public static void searchMenu(Connection con){
        System.out.println("Would you like to search listings? (Y/N)");
        sc.nextLine();
        String res = sc.nextLine();

        if (res.equals("N")){
            System.out.println("Press Enter to Exit...");
            sc.nextLine();
            return; 
        } else if (res.equals("Y")) {
            System.out.println("1. Search by distance");
            System.out.println("2. Search by postal code");
            System.out.println("3. Search by address");
            System.out.println("4. Rank prices in ascending order");
            System.out.println("5. Rank prices in descending order");
            System.out.println("6. Advance search (search by multiple filters at once)");
            System.out.println("7. Exit search");
        }

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                App.clearScreen();
                Search.distance(con);
                break;
            case 2:
                App.clearScreen();
                Search.postalCode(con);
                break;
            case 3:
                App.clearScreen();
                Search.addressSerch(con);
                break;
            case 4:
                App.clearScreen();
            	Search.ascendingPrices(con);
                break;
            case 5:
                App.clearScreen();
            	Search.decendingPrices(con);
                break;
            case 7:
                App.clearScreen();
                break;
            default:
                System.out.println("Invalid choice or Feature not yet implemented. Please try again.");
                return;
        }
    }
}
