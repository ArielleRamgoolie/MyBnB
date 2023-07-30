import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Listings {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void readListings(Connection con, int hostId) {
    	try {
            String query = "";

            // if renter is requesting to see all listings, hostID = -1
            if (hostId == -1){
                query = "SELECT * FROM Listings";
            } else {
                query = "SELECT * FROM Listings WHERE host_id = " + hostId;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings:");
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price","Address", "Latitude", "Longitude");
            System.out.println("--------------------------------------------------------------------------------------");
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float price = rs.getFloat("price");
                String address = rs.getString("address");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");

                System.out.printf("%-10d %-10d %-10s %-10.2f %-20s %-10.2f %-10.2f\n", listing, host, type, price, address, latitude, longitude);
            }
            System.out.println("--------------------------------------------------------------------------------------");

            rs.close();
            stmt.close();

            // Wait for user input (pressing Enter) before continuing
            System.out.println("\nPress Enter to continue...");
            Scanner sc = new Scanner(System.in);
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

    public static void getAvailability(Connection con, int listingId) {
        String query = "SELECT * FROM Bookings WHERE listing_id = ?;";
		try {
            
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setInt(1, listingId);
            
            ResultSet rs = stmt.executeQuery();
            java.sql.Date start = new java.sql.Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Available Days \n");


            while (rs.next()) {
                System.out.printf("From %-10s to %-10s\n", formatter.format(start), formatter.format(rs.getDate("start_date", null)));
                start = rs.getDate("end_date", null);
            }
            System.out.println("\nAny day after "+ start);
            System.out.println("--------------------------------------------------------------------------------------");
            rs.close();
            stmt.close();

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
