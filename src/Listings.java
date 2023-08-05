import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Listings {
    private static final Scanner sc = new Scanner(System.in);

    public static void readListings(Connection con) {
        try {
            String query = "SELECT * FROM Listings";

            // if renter is requesting to see all listings, hostID = -1
            if (Users.isHost) query += " WHERE host_id = " + Users.userId;

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings:");
            System.out
                    .println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price",
                    "Address", "Latitude", "Longitude");
            System.out
                    .println("--------------------------------------------------------------------------------------");
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float price = rs.getFloat("price");
                String address = rs.getString("address");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");

                System.out.printf("%-10d %-10d %-10s %-10.2f %-20s %-10.2f %-10.2f\n", listing, host, type, price,
                        address, latitude, longitude);
            }
            System.out
                    .println("--------------------------------------------------------------------------------------");

            rs.close();
            stmt.close();

            // Wait for user input (pressing Enter) before continuing
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();

            return;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewListing(Connection con, int listingId) {
        float price = -1;
        try {

            String query = "SELECT * FROM Listings WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, listingId);
            System.out.print(query + listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price",
                    "Address", "Latitude", "Longitude");
            System.out.println("--------------------------------------------------------------------------------------");
            rs.next();
            int host = rs.getInt("host_id");
            int listing = rs.getInt("id");
            String type = rs.getString("type");
            price = rs.getFloat("price");
            String address = rs.getString("address");
            float longitude = rs.getFloat("longitude");
            float latitude = rs.getFloat("latitude");

            System.out.printf("%-10d %-10d %-10s %-10.2f %-20s %-10.2f %-10.2f\n", listing, host, type, price, address,
                    latitude, longitude);

            rs.close();
            stmt.close();

            System.out.println("\n1. See reviews");
            System.out.println("2. See amenities");
            System.out.println("3. See availability");
            System.out.println("4. Make booking");
            System.out.println("5. Return to menu");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                // get listings
                Listings.viewReviews(con, 1);
                break;
            case 2:
                // create listings
                Listings.viewAmenities(con, 1);
                break;
            case 3:
                Bookings.getAvailability(con, 1);
                break;
            case 4:
                Bookings.createBooking(con, listingId, price);
            case 5:
                return;
            default:
                break;
        }
        Listings.viewListing(con, listingId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void viewReviews(Connection con, int listingId) {
        try {

            String query = "SELECT CONCAT(Users.first_name, ' ', Users.last_name) as renter_name, Rating, comment, time\n" + //
                    "FROM ListingComment\n" + //
                    "JOIN Users on Users.id = ListingComment.FromUser\n" + //
                    "WHERE ListingID = ?;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, listingId);
            System.out.print(query + listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Reviews");
            System.out.println("--------------------------------------------------------------------------------------");
            while(rs.next()) {
                String name = rs.getString("renter_name");
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                java.sql.Timestamp time = rs.getTimestamp("time");
                
                System.out.printf("%-60s %-40s\n", name, time);
                System.out.println(rating + "/5");
                System.out.println(comment);

                System.out.println("--------------------------------------------------------------------------------------");
            }

            rs.close();
            stmt.close();

            System.out.println("\nPress Enter to return...");
            sc.nextLine();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewAmenities(Connection con, int listingId) {
        try {

            String query = "Select distinct Amenities.*\n" + //
                    "from Amenities\n" + //
                    "join ListingAmenities on amenity_id = Amenities.id\n" + //
                    "WHERE listing_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, listingId);
            System.out.print(query + listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.printf("%-30s %-30s", "Amenity", "Type");
            System.out.println("\n------------------------------------------");
            while(rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                
                System.out.printf("%-30s %-30s\n", name, type);
            }

            rs.close();
            stmt.close();

            System.out.println("\nPress Enter to return...");
            sc.nextLine();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateListing(Connection con) {
        App.clearScreen();
        Listings.readListings(con);
        System.out.println("Enter listing id: ");
        int id = sc.nextInt();
        sc.nextLine();
        viewListing(con, id);
        System.out.println("\n1. Edit price");
        System.out.println("2. Edit availability");
        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {
            case 1:
                System.out.println("Enter new price: ");
                double newPrice = sc.nextInt();
                updateListingPrice(con, id, newPrice);
            case 2:
                Bookings.getAvailability(con, id);
                System.out.println("\n1. Book off date range");
                System.out.println("2. Cancel bookings");
                choice = sc.nextInt();
                if(choice == 1) Bookings.createBooking(con, id, 0);
                if(choice == 2) Bookings.cancelBooking(con);
                return;
            default:
                return;
        }
    }

    public static void updateListingPrice(Connection con, int listingId, double newPrice) {
        try {
            String query = "UPDATE Listings SET price = ? WHERE id = ?;";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, listingId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Updated pricing successfully!");
            } else {
                System.out.println("Failed, please try again");
            }

            System.out.println("\nEnter to continue...");
            sc.nextLine();

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createListing(Connection con, int hostID) {
        try {
            int host = hostID;

            System.out.println("Enter the listing type: 'house', 'apartment', 'guesthouse' or 'hotel'");
            String type = sc.nextLine();

            System.out.println("Enter the address: ");
            String address = sc.nextLine();

            System.out.println("Enter the longitude: ");
            double longitude = sc.nextDouble();

            System.out.println("Enter the latitude: ");
            double latitude = sc.nextDouble();

            System.out.println("Enter the price per night: ");
            double price = sc.nextDouble();

            String query = "INSERT INTO Listings (`host_id`, `type`, `address`, `longitude`, `latitude`, `price`) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, host);
            stmt.setString(2, type);
            stmt.setString(3, address);
            stmt.setDouble(4, longitude);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, price);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Added your listing successfully!");
            } else {
                System.out.println("Failed, please try again");
            }

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
