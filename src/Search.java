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
            System.out.println("All Listings in Ascending Prices:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-5s %-10s %-10s %-11s %-20s %-10s %-10s %-11s %-11s %-11s\n", "ListingID", "Host", "Type", "Price", "HouseNumber", "StreetName", "City", "Country", "PostalCode", "Latitude", "Longitude");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                float price = rs.getFloat("price");
                int housenumber = rs.getInt("house_number");
                String streetname = rs.getString("street_name");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalcode = rs.getString("postal_code");

                System.out.printf("%-10d %-5d %-10s %-10.2f %-11d %-20s %-10s %-10s %-11s %-11.3f %-11.3f\n", listing, host, type, price, housenumber, streetname, city, country, postalcode, latitude, longitude);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

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
            System.out.println("All Listings in Descending Prices:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-5s %-10s %-10s %-11s %-20s %-10s %-10s %-11s %-11s %-11s\n", "ListingID", "Host", "Type", "Price", "HouseNumber", "StreetName", "City", "Country", "PostalCode", "Latitude", "Longitude");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                float price = rs.getFloat("price");
                int housenumber = rs.getInt("house_number");
                String streetname = rs.getString("street_name");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalcode = rs.getString("postal_code");

                System.out.printf("%-10d %-5d %-10s %-10.2f %-11d %-20s %-10s %-10s %-11s %-11.3f %-11.3f\n", listing, host, type, price, housenumber, streetname, city, country, postalcode, latitude, longitude);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

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
            // TO DO: we must sort the results too
            // print the results to user 
            App.clearScreen();
            System.out.printf("All Listings");
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-20s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price","Address", "Latitude", "Longitude");
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

        double lat = ((userLat - listingLat) * ( Math.PI / 180.0));
        double lon = ((userLong - listingLong) * ( Math.PI / 180.0));

        double first = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(listingLat * ( Math.PI / 180.0)) * Math.cos(userLat * ( Math.PI / 180.0))* Math.sin(lon / 2) * Math.sin(listingLong / 2);

        double second = 2 * Math.atan2(Math.sqrt(first), Math.sqrt(1 - first));

        return radiusEarth * second;
    }

    public static void addressSerch(Connection con){
        try{
            System.out.println("Please enter the exact house number of the listing you would like to search for:");
            int housenumber = sc.nextInt();
            sc.nextLine();
            System.out.println("Please enter the exact street name of the listing you would like to search for:");
            String streetname = sc.nextLine();
            System.out.println("Please enter the exact city name of the listing you would like to search for:");
            String city = sc.nextLine();
            System.out.println("Please enter the exact country name of the listing you would like to search for:");
            String country = sc.nextLine();

            // query listings 
            String query = "SELECT * FROM Listings WHERE house_number = '" + housenumber + "' AND street_name = '" + streetname + "' AND city = '" + city + "' AND country = '" + country + "'"  ; 
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings in Descending Prices:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-5s %-10s %-10s %-11s %-20s %-10s %-10s %-11s %-11s %-11s\n", "ListingID", "Host", "Type", "Price", "HouseNumber", "StreetName", "City", "Country", "PostalCode", "Latitude", "Longitude");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                float price = rs.getFloat("price");
                housenumber = rs.getInt("house_number");
                streetname = rs.getString("street_name");
                city = rs.getString("city");
                country = rs.getString("country");
                String postalcode = rs.getString("postal_code");

                System.out.printf("%-10d %-5d %-10s %-10.2f %-11d %-20s %-10s %-10s %-11s %-11.3f %-11.3f\n", listing, host, type, price, housenumber, streetname, city, country, postalcode, latitude, longitude);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
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

    public static void postalCode(Connection con){
        try{
            System.out.println("Please enter the postal code you would like to search for:");
            String pc = sc.nextLine();

            // query listings
            String query = "SELECT * FROM Listings WHERE postal_code = '" + pc + "' OR LEFT(postal_code, 2) = LEFT('" + pc +"', 2)"; 
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            App.clearScreen();
            System.out.println("All Listings in Descending Prices:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-5s %-10s %-10s %-11s %-20s %-10s %-10s %-11s %-11s %-11s\n", "ListingID", "Host", "Type", "Price", "HouseNumber", "StreetName", "City", "Country", "PostalCode", "Latitude", "Longitude");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                float price = rs.getFloat("price");
                int housenumber = rs.getInt("house_number");
                String streetname = rs.getString("street_name");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalcode = rs.getString("postal_code");

                System.out.printf("%-10d %-5d %-10s %-10.2f %-11d %-20s %-10s %-10s %-11s %-11.3f %-11.3f\n", listing, host, type, price, housenumber, streetname, city, country, postalcode, latitude, longitude);
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

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
    
}
