import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Search {
    private static final Scanner sc = new Scanner(System.in);

    public static void printListings(Connection con, String query){
        try {
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            App.clearScreen();
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-5s %-10s %-10s %-40s %-20s %-10s %-15s %-11s %-11s\n", "ListingID", "Host", "Type", "Price", "Address", "City", "Country", "PostalCode", "Latitude", "Longitude");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int host = rs.getInt("host_id");
                int listing = rs.getInt("id");
                String type = rs.getString("type");
                float longitude = rs.getFloat("longitude");
                float latitude = rs.getFloat("latitude");
                float price = rs.getFloat("price");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalcode = rs.getString("postal_code");

                System.out.printf("%-10d %-5d %-10s %-10.2f %-40s %-20s %-10s %-15s %-11.3f %-11.3f\n", listing, host, type, price, address, city, country, postalcode, latitude, longitude);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    public static void filter(Connection con, String query){
        System.out.println("Would you like to further filter this search? (Y/N)");

        if (sc.nextLine().equals("Y")){
            System.out.println("What would you like to filter / sort by? (eg. price range, amenities, availibility, ascending, descending)");
            System.out.println("Please note that ascending and descending will sort by prices");
            String res = sc.nextLine();

            if (res.equals("price range")){
                System.out.println("Enter max price");
                int max = sc.nextInt();
                System.out.println("Enter min price");
                int min = sc.nextInt();
                query = query + " AND price BETWEEN " + min + " AND " + max;
                printListings(con, query);
                sc.nextLine();
            }

            if (res.equals("ascending")){
                query = query + "ORDER BY price ASC";
                printListings(con, query);
                sc.nextLine();
            }

            if (res.equals("descending")){
                query = query + "ORDER BY price DESC";
                printListings(con, query);
                sc.nextLine();
            }
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
            String query = "SELECT * FROM Listings";
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
                    String listingProperties = String.format("%-10s %-10s %-10s %-10.2f %-20s %-10s %-10s", id, hostId, type, price, currListingLat, currListingLong);
                    results.add(listingProperties);
                }
            }
            // TO DO: we must sort the results too
            // print the results to user 
            App.clearScreen();
            System.out.printf("All Listings");
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "ListingID", "HostID", "Type", "Price", "Latitude", "Longitude");
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
        System.out.println("Please enter the exact house number of the listing you would like to search for:");
        int housenumber = sc.nextInt();
        sc.nextLine();

        System.out.println("Please enter the exact street name of the listing you would like to search for:");
        String streetname = sc.nextLine();

        String address = housenumber + " " + streetname;

        System.out.println("Please enter the exact city name of the listing you would like to search for:");
        String city = sc.nextLine();

        System.out.println("Please enter the exact country name of the listing you would like to search for:");
        String country = sc.nextLine();

        // query listings 
        String query = "SELECT * FROM Listings WHERE address = '" + address + "' AND city = '" + city + "' AND country = '" + country + "'"  ; 

        printListings(con, query);

        filter(con, query);

        // Wait for user input (pressing Enter) before continuing
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();

        return;
    }

    public static void postalCode(Connection con){
        System.out.println("Please enter the postal code you would like to search for:");
        String pc = sc.nextLine();
        
        // query listings
        String query = "SELECT * FROM Listings WHERE (postal_code = '" + pc + "' OR LEFT(postal_code, 2) = LEFT('" + pc +"', 2))"; 
        printListings(con, query);

        // ask if they would like to further filter the search
        filter(con, query);

        // Wait for user input (pressing Enter) before continuing
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();

        return;
    
    }

    public static void advanceSearch(Connection con){
        String query = "SELECT * FROM Listings WHERE "; 
        String response;

        System.out.println("Would you like to filter by HostID? (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter host ID");
            int host = sc.nextInt();
            sc.nextLine();
            query = query + "host_id = " + host + " AND ";
        }

        System.out.println("Would you like to filter by type of listing? (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter type: apartment, house, hotel, guesthouse");
            String type = sc.nextLine();
            query = query + "type = '" + type + "' AND ";
        }

        // System.out.println("Would you like to filter by distance of listing?");
        // if (sc.nextLine().equals("Y")){
        //     System.out.println("Enter type: apartment, house, hotel, guesthouse");
        //     String type = sc.nextLine();
        //     sc.nextLine();
        //     query = query + "type = " + type + " AND ";
        // }


        System.out.println("Would you like to filter by postal code of listing? (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter type postal code (eg. LNL NLN)");
            String pc = sc.nextLine();
            query = query + "postal_code = '" + pc + "' AND ";
        }

        System.out.println("Would you like to filter by city of listing? (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter type city");
            String city = sc.nextLine();
            query = query + "city = '" + city + "' AND ";
        }

        System.out.println("Would you like to filter by country of listing? (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter country");
            String country = sc.nextLine();
            query = query + "country = '" + country + "' AND ";
        }

        System.out.println("Would you like to filter by price range (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            System.out.println("Enter min price");
            int min = sc.nextInt();
            System.out.println("Enter max price");
            int max = sc.nextInt();
            query = query + "price BETWEEN " + min + " AND " + max;
            sc.nextLine();
        }
        
        System.out.println("Would you like to sort by ascending (Y/N)");
        response = sc.nextLine();
        if (response.equals("Y")){
            if (query.endsWith("AND ")){
                query = query.substring(0, query.length() - 4);
            }
            query = query + " ORDER BY price ASC";
        } else {
            System.out.println("Would you like to sort by descending (Y/N)");
            response = sc.nextLine();
            if (response.equals("Y")){
                if (query.endsWith("AND ")){
                    query = query.substring(0, query.length() - 4);
                }
                query = query + " ORDER BY price DESC";
            }
        }

        if (query.endsWith("AND ")){
            query = query.substring(0, query.length() - 4);
        }

        printListings(con, query);
        System.out.println("Press enter to continue: ");
        sc.nextLine();
    }
}
