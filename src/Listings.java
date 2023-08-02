import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price","Address", "Latitude", "Longitude");
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                price = rs.getFloat("price");
                String address = rs.getString("address");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");

                System.out.printf("%-10d %-10d %-10s %-10.2f %-20s %-10.2f %-10.2f\n", listing, host, type, price, address, latitude, longitude);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
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

    public static void createBooking(Connection con) {
        System.out.println("Enter the listing id: ");
        int id = sc.nextInt();

        float pricing = viewListing(con, id);
        getAvailability(con, id);
        sc.nextLine();

        System.out.println("Enter the start date (YYYY-MM-DD): ");
        String start = sc.nextLine();
        System.out.println("Enter the end date (YYY-MM-DD): ");
        String end = sc.nextLine();
        
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate;
        java.util.Date endDate;
        try {
            startDate = parser.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        try {
            endDate = parser.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        if(isUnavailable(con, start, end)) {
            System.out.println("Sorry, the days are not available. Please try again.");
            return;
        }
        
        int nights = (int)TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);


        System.out.println("\n\n" + "SUMMARY\n");
        System.out.println(nights + " nights");
        System.out.println("$" + pricing*nights);

        System.out.println("\nConfirm booking (y/n):");
        String confirm = sc.nextLine();
        System.out.println(confirm + " " + confirm == "y");
        if(confirm == "n\n" || confirm == "N\n" ) return;

        try {
            
            String query = "INSERT INTO Bookings (`listing_id`, `renter_id`, `start_date`, `end_date`, `price_per_night`, `num_nights`, `total_cost`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setInt(1, id);
            stmt.setInt(2, Users.userId);
            stmt.setDate(3, new Date(startDate.getTime()));
            stmt.setDate(4, new Date(endDate.getTime()));
            stmt.setDouble(5, pricing);
            stmt.setInt(6, nights);
            stmt.setDouble(7, nights*pricing);
            stmt.setInt(8, 1);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Added your booking successfully!");
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

    public static boolean isUnavailable(Connection con, String startDate, String endDate) {
        return false;
    }

    public static void getAvailability(Connection con, int listingId) {
        String query = "SELECT * FROM Bookings WHERE listing_id = ? AND CURDATE() < end_date ORDER BY start_date;";
		try {
            
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setInt(1, listingId);
            
            ResultSet rs = stmt.executeQuery();
            java.sql.Date start = new java.sql.Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Available Days \n");
            while (rs.next()) {
                if(start.after(rs.getDate("start_date", null)) || start.equals(rs.getDate("start_date", null))){
                    start = rs.getDate("end_date", null);
                    rs.next();
                }
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
