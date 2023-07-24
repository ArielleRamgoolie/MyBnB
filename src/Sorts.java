import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
public class Sorts {

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
            Scanner sc = new Scanner(System.in);
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
            Scanner sc = new Scanner(System.in);
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
            Scanner sc = new Scanner(System.in);
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
            Scanner sc = new Scanner(System.in);
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
            Scanner sc = new Scanner(System.in);
            sc.nextLine();

            return;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
