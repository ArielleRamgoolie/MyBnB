import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Listings {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void readListings(Connection con, int hostId) {
    	try {
            String query = "";

            // if renter is requesting to see all listings, hostID = -1
            if (hostId == -1){
                query = "SELECT * FROM Listing";
            } else {
                query = "SELECT * FROM Listing WHERE Host = " + hostId;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("All Listings:");
            System.out.println("-----------------------------------");
            while (rs.next()) {
                int host = rs.getInt("Host");
                String type = rs.getString("Type");
                String address = rs.getString("Address");

                System.out.println("Host ID: " + host);
                System.out.println("Title: " + type);
                System.out.println("Content: " + address);
                System.out.println("-----------------------------------");
            }

            rs.close();
            stmt.close();
            return;

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public static void createListing(Connection con, int hostID) {
		try {
            int host = hostID;

            //System.out.println("Enter the Renter: ");
            int renter = 1; // listing schema might change 
            
            System.out.println("Enter the listing type: 'house', 'apartment', 'guesthouse' or 'hotel'");
            sc.nextLine();
            String type = sc.nextLine();
            
            System.out.println("Enter the address: ");
            String address = sc.nextLine();
            
            System.out.println("Enter the postal code: ");
            String post = sc.nextLine();
            
            System.out.println("Enter the city: ");
            String city = sc.nextLine();
            
            System.out.println("Enter the country: ");
            String country = sc.nextLine();
            
            System.out.println("Enter the longitude: ");
            double longitude = sc.nextDouble();
            
            System.out.println("Enter the latitude: ");
            double latitude = sc.nextDouble();
            
            
            String query = "INSERT INTO Listing (`Host`, `Renter`, `Type`, `Address`, `PostalCode`, `Longitude`, `latitude`, `City`, `Country`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setInt(1, host);
            stmt.setInt(2, renter);
            stmt.setString(3, type);
            stmt.setString(4, address);
            stmt.setString(5, post);
            stmt.setDouble(6, longitude);
            stmt.setDouble(7, latitude);
            stmt.setString(8, city);
            stmt.setString(9, country);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Added your listing successfully!");
            } else {
                System.out.println("Failed, please try again");
            }

            App.startApp(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
