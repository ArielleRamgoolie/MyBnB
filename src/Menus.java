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
        System.out.println("7. Comment on renters");
        System.out.println("8. Delete account");
        System.out.println("9. Exit");

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                // get listings
                App.clearScreen();
                Listings.readListings(con);
                sc.nextLine();
                hostMenu(con, hostID);
            case 2:
                // create listings
                App.clearScreen();
            	Listings.createListing(con, hostID);
                hostMenu(con, hostID);
            case 3:
                // create listings
                App.clearScreen();
                Listings.readListings(con);
                System.out.println("Select Listing ID: ");
                int listing_id = sc.nextInt();
            	Listings.viewListing(con, listing_id);
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
                Bookings.readBookings(con, false);
                System.out.println("Enter ID:");
                int renter = sc.nextInt();
                sc.nextLine();
                Comments.createRenterComment(con, renter);
                hostMenu(con, hostID);
            case 8:
                Delete.host(con);
                break;
            case 9:
                System.out.println("Exiting MyBnB. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
                hostMenu(con, hostID);
        }
    }

    public static void renterMenu(Connection con, int renterID) {
        App.clearScreen();
        System.out.println("Welcome Renter! What would you like to do today?");
        System.out.println("1. View all listings");
        System.out.println("2. Create a booking");
        System.out.println("3. Cancel a booking");
        System.out.println("4. See my booking future bookings");
        System.out.println("5. See my booking past bookings");
        System.out.println("6. Write a review");
        System.out.println("7. See comments from hosts");
        System.out.println("8. Delete account");
        System.out.println("9. Exit");

        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                App.clearScreen();
            	Listings.readListings(con);
                searchMenu(con);
                renterMenu(con, renterID);
            case 2:
                System.out.println("Enter ID:");
                int listingId = sc.nextInt();
                Listings.viewListing(con, listingId);
                renterMenu(con, renterID);
            case 3:
                Bookings.cancelBooking(con);
                App.clearScreen();
                renterMenu(con, renterID);
            case 4:
                Bookings.readBookings(con, true);
                App.clearScreen();
                renterMenu(con, renterID);
            case 5:
                Bookings.readBookings(con, false);
                App.clearScreen();
                renterMenu(con, renterID);
            case 6:
                Bookings.readBookings(con, false);
                System.out.println("Enter ID:");
                int listing = sc.nextInt();
                sc.nextLine();
                Comments.createListingComment(con, listing);
                renterMenu(con, renterID);
            case 7:
                Comments.viewComments(con);
                renterMenu(con, renterID);
            case 8:
                Delete.user(con);
                break;
            case 9:
                System.out.println("Exiting MyBnB. Goodbye!");
                System.exit(0);
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
            System.out.println("1. Regular search (distance, postal code, address)");
            System.out.println("2. Advance search (search by multiple filters at once)");
            System.out.println("3. Select Listing");
            System.out.println("4. Exit search");
        
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: 
                    System.out.println("Enter the type of search you'd like to conduct: (eg. distance, postal code, address)");
                    String search = sc.nextLine();
                    if (search.equals("distance")){
                        Search.distance(con);
                    }
                    if (search.equals("postal code")){
                        Search.postalCode(con);
                    }
                    if (search.equals("address")){
                        Search.addressSerch(con);
                    }
                    break; 
                case 2:
                    App.clearScreen();
                    Search.advanceSearch(con);
                case 3:
                    System.out.println("Enter the listing id:");
                    int listing_id = sc.nextInt();
                    sc.nextLine();
                    Listings.viewListing(con, listing_id);
                    break;
                case 4:
                    App.clearScreen();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return;
            }
        }
    }
}
