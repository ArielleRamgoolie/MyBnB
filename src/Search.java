import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Search {
    private static final Scanner sc = new Scanner(System.in);

    public static void ascendingPrices(Connection con) {

        String query = "SELECT * FROM Listings ORDER BY price ASC";

    	try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings (Price: Low to High):");
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

    public static void decendingPrices(Connection con) {

        String query = "SELECT * FROM Listings ORDER BY price DESC";

    	try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings (Price: High to Low):");
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

    public static void maxListing(int max, Connection con) {
       
        String query = "SELECT * FROM Listings WHERE price >=" + max;

    	try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.printf("All Listings with prices greater than $%.2f:", (float) max);
            System.out.println();
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

    public static void minListing(int min, Connection con) {
               
        String query = "SELECT * FROM Listings WHERE price <=" + min;

    	try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.printf("All Listings with prices less than $%.2f:", (float) min);
            System.out.println();
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

    public static void typeSort(String type, Connection con) {
                       
        String query = "SELECT * FROM Listings WHERE type = '" + type + "'";

    	try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.printf("All %s Listings", type);
            System.out.println();
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price","Address", "Latitude", "Longitude");
            System.out.println("--------------------------------------------------------------------------------------");
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String types = rs.getString("type");
                float price = rs.getFloat("price");
                String address = rs.getString("address");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");

                System.out.printf("%-10d %-10d %-10s %-10.2f %-20s %-10.2f %-10.2f\n", listing, host, types, price, address, latitude, longitude);
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

    public static void distance(Connection con){
        try{
            // We want to collect all listings 
            List<String> results = new ArrayList<>();

            // Use Haversine Formula

            // Collect user Longitude and Latitude 
            System.out.println("Enter your longitude:");
            double userLong = sc.nextDouble();
            System.out.println("Enter your latitude:");
            double userLat = sc.nextDouble();

            sc.nextLine();

            // Collect range; Default value is 10 km
            App.clearScreen();
            System.out.println("This will, by default, find the listings within 10km of your longitude and latitude.");
            System.out.println("Would you like to enter a new range for the distance? (Y/N)");
            String res = sc.nextLine();

            double range = 10.0;
            if (res.equals("Y")){
                System.out.println("Please enter the new range in kilometers");
                range = sc.nextDouble();
            }

            // query listings 
            Statement stmt = con.createStatement();
            String query = "SELECT id, host_id, type, price, address, latitude, longitude FROM Listings";
            ResultSet rs = stmt.executeQuery(query);

            // search through all the results 
            while (rs.next()){
                double currListingLat = rs.getDouble("latitude");
                double currListingLong = rs.getDouble("longitude");

                double dist = findDistance(currListingLat, currListingLong, userLat, userLong);

                if (dist <= range) {
                    String id = rs.getString("id");
                    String hostId = rs.getString("host_id");
                    String type = rs.getString("type");
                    double price = rs.getDouble("price");
                    String address = rs.getString("address");
                    String listingProperties = String.format("%-10s %-10s %-10s %-10.2f %-20s %-10s %-10s", id, hostId, type, price, address, currListingLat, currListingLong);
                    results.add(listingProperties);
                }
            }

            // print the results to user 
            App.clearScreen();
            System.out.printf("All Listings");
            System.out.println();
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingxxID", "HostID", "Type", "Price","Address", "Latitude", "Longitude");
            System.out.println("--------------------------------------------------------------------------------------");
            for (String listing : results) {
                System.out.println(listing);
            }

            // Wait for user input (pressing Enter) before continuing
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public static double findDistance(double listingLat, double listingLong, double userLat, double userLong){
        // Use Haversine Formula
        double radiusEarth = 6371.0;
        double convDegToRad = Math.PI / 180.0; 

        double lat = ((userLat - listingLat) * convDegToRad);
        double lon = ((userLong - listingLong) * convDegToRad);

        double first = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(listingLat * convDegToRad) * Math.cos(userLat * convDegToRad)* Math.sin(lon / 2) * Math.sin(listingLong / 2);

        double second = 2 * Math.atan2(Math.sqrt(first), Math.sqrt(1 - first));

        return radiusEarth * second;
    }
    
}
