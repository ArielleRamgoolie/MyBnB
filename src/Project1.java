import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // get listings
            	// readListings(con);
                login(con);
                break;
            case 2:
                // create listings
            	// createListing(con);
                createUser(con);
                break;
            case 3:
                System.out.println("Exiting MyBnB. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                startApp(con);
        }
    }

    public static void createUser(Connection con){
        try {
            // Assume that user will always enter all of their data (:
            System.out.println("Enter your Username: ");
            String username = sc.next();
            
            System.out.println("Enter your Password: ");
            String password = sc.next();

            System.out.println("Enter your Name: ");
            String name = sc.next();
            
            System.out.println("Enter your Address: ");
            String address = sc.next();

            String dob;
            do {
                System.out.println("Enter your Date of Birth (yyyy-mm-dd): ");
                dob = sc.next();
            } while (!isValidDateFormat(dob));

            System.out.println("Enter your SIN I am the bank >;) : ");
            int SIN = sc.nextInt();
            sc.nextLine();
            
            System.out.println("Enter your Occupation: ");
            String occupation = sc.next();
            
            System.out.println("Enter which type of user you are (h - host or r - renter): ");
            String type = sc.next();
            
            // only collect a renter's payment info
            String payment;
            if (type.equals("r")){
                System.out.println("Enter your payment info hehehehe >:) : ");
                payment = sc.next();
            } else {
                payment = "null";
            }
            
            String query = "INSERT INTO User (`Username`, `Password`, `Name`, `Address`, `DOB`, `Occupation`, `SIN`, `Type`, `PaymentInfo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, address);
            stmt.setString(5, dob);
            stmt.setString(6, occupation);
            stmt.setInt(7, SIN);
            stmt.setString(8, type);
            stmt.setString(9, payment);
            
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

    // Regular expression to check the date format (yyyy-mm-dd)
    private static final Pattern DATE_FORMAT_REGEX = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    // Method to validate the date format (yyyy-mm-dd)
    private static boolean isValidDateFormat(String date) {
        Matcher matcher = DATE_FORMAT_REGEX.matcher(date);
        return matcher.matches();
    }

    public static void login(Connection con) {
        System.out.println("Enter your username: ");
        String username = sc.next();
        System.out.println("Enter your password: ");
        String password = sc.next();

        try {
            // Prepare the SQL query to check if the credentials are valid in the database.
            String query = "SELECT * FROM User WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if the result set has any rows (i.e., if the credentials match any user in the database)
            boolean isValid = rs.next();

            // Close the resources
            rs.close();
            stmt.close();
            if (isValid){
                System.out.println("User Logged in");
            }else {
                System.out.println("Unsuccessful Login");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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