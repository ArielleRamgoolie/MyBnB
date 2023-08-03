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

    public static float viewListing(Connection con, int listingId) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    public static void updateListing(Connection con) {
        App.clearScreen();
        Listings.readListings(con);
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
