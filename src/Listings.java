import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Listings {
    private static final Scanner sc = new Scanner(System.in);

    public static void readListings(Connection con) {
        String query = "SELECT * FROM Listings";

        // if renter is requesting to see all listings, hostID = -1
        if (Users.isHost) query += " WHERE host_id = " + Users.userId;


        Search.printListings(con, query);

        return;
    }

    public static void viewListing(Connection con, int listingId) {
        float price = -1;

            String query = "SELECT * FROM Listings WHERE id = " + listingId + ";";

            Search.printListings(con, query);

            System.out.println("\n1. See reviews");
            System.out.println("2. See amenities");
            System.out.println("3. See availability");
            System.out.println("4. " + (Users.isHost ? "Cancel a booking" : "Make a booking"));
            if(Users.isHost) {
                System.out.println("5. Edit price");
                System.out.println("6. Book days off");
                System.out.println("7. Add amenity");
                System.out.println("8. Remove amenity");
            } 
            System.out.println((Users.isHost ? 9 : 5) + ". Return to menu");
            

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                // get listings
                viewReviews(con, listingId);
                break;
            case 2:
                // create listings
                viewAmenities(con, listingId);
                break;
            case 3:
                Bookings.getAvailability(con, listingId);
                break;
            case 4:
                if(Users.isHost)
                    Bookings.cancelBooking(con);   
                else
                    Bookings.createBooking(con, listingId, price);
                break;
            case 5:
                if(Users.isHost){
                    System.out.println("Enter new price: ");
                    double newPrice = sc.nextInt();
                    updateListingPrice(con, listingId, newPrice);
                    break;
                } else return;
            case 6:
                if(Users.isHost)
                    Bookings.createBooking(con, listingId, 0);
                break;
            case 7:
                if(Users.isHost)
                    addAmenity(con, listingId, true);
                break;
            case 8:
                if(Users.isHost)
                    addAmenity(con, listingId, false);
                break;
            case 9:
                return;
        }
        Listings.viewListing(con, listingId);
        return;

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

            String query = "Select Amenities.*\n" + //
                    "from Amenities\n" + //
                    "join ListingAmenities on amenity_id = Amenities.id\n" + //
                    "WHERE listing_id = ?\n" + 
                    "ORDER BY Amenities.id";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, listingId);
            System.out.print(query + listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.printf("%-10s %-30s %-30s", "ID", "Amenity", "Type");
            System.out.println("\n------------------------------------------------------");
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                
                System.out.printf("%-10s %-30s %-30s\n", id, name, type);
            }

            rs.close();
            stmt.close();

            System.out.println("\nPress Enter to return...");
            sc.nextLine();

        } catch (SQLException e) {
            e.printStackTrace();
            sc.nextLine();
        }
    }

        public static void addAmenity(Connection con, int listingId, boolean add) {
        try {

            String addQuery = "select *\n" + //
                    "from Amenities\n" + //
                    "where (select count(*) from ListingAmenities where listing_id = ? and amenity_id = id) = 0\n" + 
                    "ORDER BY Amenities.id";

            String deleteQuery = "Select Amenities.*\n" + //
                    "from Amenities\n" + //
                    "join ListingAmenities on amenity_id = Amenities.id\n" + //
                    "WHERE listing_id = ?\n" + 
                    "ORDER BY Amenities.id";

            PreparedStatement stmt = con.prepareStatement(add ? addQuery : deleteQuery);
            stmt.setInt(1, listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.printf("%-10s %-30s %-30s", "ID", "Amenity", "Type");
            System.out.println("\n------------------------------------------------------");
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                
                System.out.printf("%-10s %-30s %-30s\n", id, name, type);
            }

            rs.close();
            stmt.close();

            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nSelect amenity ID:");
        int amenityId = sc.nextInt();
        updateAmenity(con, listingId, amenityId, add);

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

    public static void updateAmenity(Connection con, int listingId, int amenityId, boolean add) {
        try {
            String deleteQuery = "DELETE from ListingAmenities\n" + //
                    "where listing_id = ? and amenity_id = ?;";

            String addQuery = "Insert into ListingAmenities (listing_id, amenity_id) values (?, ?);";

            PreparedStatement stmt = con.prepareStatement(add ? addQuery: deleteQuery);

            stmt.setInt(1, listingId);
            stmt.setInt(2, amenityId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println((add ? "Added":"Removed") + " amenity successfully!");
            } else {
                System.out.println("Failed, please try again");
            }
            sc.nextLine();
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

            System.out.println("Enter house number (eg. 1) ");
            String housenumber = sc.nextLine();

            System.out.println("Enter the street name (eg. Cool Street)");
            String streetname = sc.nextLine();

            System.out.println("Enter city (eg. Vaughan) ");
            String city = sc.nextLine();

            System.out.println("Enter the country (eg. Canada)");
            String country = sc.nextLine();

            System.out.println("Enter the postal code (eg. LNL NLN) ");
            String pc = sc.nextLine();

            System.out.println("Enter the longitude (eg. 52.555)");
            double longitude = sc.nextDouble();

            System.out.println("Enter the latitude (eg. 54.555) ");
            double latitude = sc.nextDouble();

            System.out.println("Enter the price per night (eg. 52) ");
            double price = sc.nextDouble();

            sc.nextLine();

            String query = "INSERT INTO Listings (`host_id`, `type`, `longitude`, `latitude`, `price`, `address`, `city`, `country`, `postal_code`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            // PreparedStatement stmt = con.prepareStatement(query);
            PreparedStatement stmt = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            String address = housenumber + " " + streetname;

            stmt.setInt(1, host);
            stmt.setString(2, type);
            stmt.setDouble(3, longitude);
            stmt.setDouble(4, latitude);
            stmt.setDouble(5, price);
            stmt.setString(6, address);
            stmt.setString(7, city);
            stmt.setString(8, country);
            stmt.setString(9, pc);

            int rowsAffected = stmt.executeUpdate();
            // stmt.close();
            // preparedStatement.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            int key = -1;
            if (rs.next()) {
                key = rs.getInt(1);
            }

            if (rowsAffected > 0) {
                System.out.println("Added your listing successfully! Please move onto adding Amenities for your Listing by selecting 3 in menu");
                System.out.println("Press Enter to continue..");
                System.out.println(key);
                sc.nextLine();
                Listings.viewListing(con, key);
                return;
                
            } else {
                System.out.println("Failed, please try again");
            }

            stmt.close();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
