import java.sql.*;
import java.util.Scanner;

public class Project1 {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/MyBnB";
    private static final String DB_USERNAME = "root";
    
    private static final String DB_PASSWORD = "cscc43p1";
    private static final Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            // Create a connection to the database
        	Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Start the airbnb app
            startApp(con);

            // Close the database connection
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void startApp(Connection con) {
    	
        // Provide a basic text interface to interact with app
        System.out.println("Welcome to our Airbnb App!");
        System.out.println("1. Show Listings");
        System.out.println("2. Create Listing");
        System.out.println("3. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
            	readListings(con);
                break;
            case 2:
                // create listings
            	createListing(con);
                break;
            case 3:
                System.out.println("Exiting the Note Taking App. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                startApp(con);
        }
    }
	
    public static void readListings(Connection con) {
    	try {
            String query = "SELECT * FROM Listing";
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

            startApp(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public static void createListing(Connection con) {
		try {
            System.out.println("Enter the Host: ");
            int host = sc.nextInt();

            System.out.println("Enter the Renter: ");
            int renter = sc.nextInt();
            
            System.out.println("Enter the listing type: ");
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
                System.out.println("Added successfully!");
            } else {
                System.out.println("Failed");
            }

            startApp(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public void removeListing(int listing, int user) {
		
	}
	
	public void changePrice(int listing, float newPrice) {
		
	}
	
	public void addAnemity(int listing, String anemity) {
		
	}
	
	public void createBooking(int listing, int Renter, String startDate, String endDate, float cost, String status) {
		
	}
	
	public void cancelBooking(int bookingId, int user) {
		
	}
}