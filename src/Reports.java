import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Reports {
    private static final Scanner sc = new Scanner(System.in);

    public static void BookingsPer(Connection con, boolean city) {
        System.out.println("Enter the start date (YYYY-MM-DD): ");
        String start = sc.nextLine();
        System.out.println("Enter the end date (YYYY-MM-DD): ");
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

        try {

            String query = "Select " + (city ? "city": "postal_code") + ", COUNT(*) as numBookings " + //
                    "from bookings " + //
                    "JOIN Listings on Listings.id = bookings.listing_id " + //
                    "WHERE start_date > ? AND end_date < ? " + //
                    "GROUP BY " + (city ? "city": "postal_code") + ";";

            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setDate(1, new Date(startDate.getTime()));
            stmt.setDate(2, new Date(endDate.getTime()));

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Bookings per city from " + new Date(endDate.getTime()) + " to " + endDate + ":");
            System.out
                    .println("------------------------------------------------------------");
            System.out.printf("%-20s %-30s\n", (city? "City": "Postal code"), "# of Bookings");
            System.out
                    .println("------------------------------------------------------------");
            while (rs.next()) {
                String cty = rs.getString((city ? "city": "postal_code"));
                int bookings = rs.getInt("numBookings");

                System.out.printf("%-20s %-30s\n", cty, bookings);
            }
            System.out
                    .println("------------------------------------------------------------");

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

    public static void ListingsPer(Connection con, int columns) {

        try {

            String query = "Select country" + (columns == 3 ? ", city, postal_code": columns == 2 ? ", city": "") + ", COUNT(*) as numListings " + //
                    "from Listings " +
                    "GROUP BY country" + (columns == 3 ? ", city, postal_code": columns == 2 ? ", city": "") + ";";

            
            PreparedStatement stmt = con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Listings per country" + (columns == 3 ? ", city, postal_code": columns == 2 ? ", city": "") + ":");
            System.out
                    .println("------------------------------------------------------------");
            if(columns == 1)
                System.out.printf("%-20s %-20s\n", "Country", "# of Bookings");
            else if(columns == 2)
                System.out.printf("%-20s %-20s %-20s\n", "Country", "City", "# of Bookings");
            else if(columns == 3)
                System.out.printf("%-20s %-20s %-20s %-20s\n", "Country", "City", "Postal Code", "# of Bookings");
            System.out
                    .println("------------------------------------------------------------");
            while (rs.next()) {
                String country = rs.getString("country");
                String city = columns > 1 ? rs.getString("city") : "";
                String postal_code = columns > 2 ? rs.getString("postal_code") : "";
                int listings = rs.getInt("numListings");

                if(columns == 1)
                    System.out.printf("%-20s %-20s\n", country, listings);
                else if(columns == 2)
                    System.out.printf("%-20s %-20s %-20s\n", country, city, listings);
                else if(columns == 3)
                    System.out.printf("%-20s %-20s %-20s %-20s\n", country, city, postal_code, listings);
            }
            System.out
                    .println("------------------------------------------------------------");

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

    public static void RankHosts(Connection con) {

        try {

            String query = "Select country, CONCAT(first_name, ' ', last_name) as host, COUNT(*) as numListings from Listings \n" + //
                    "JOIN Users on Users.id = Listings.host_id \n" + //
                    "GROUP BY country, Users.id\n" + //
                    "ORDER BY country, numListings desc";
            
            PreparedStatement stmt = con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Host Rankings per country:");
            System.out
                    .println("------------------------------------------------------------");
            
            
            int rank = 1;
            String country = "";
            while (rs.next()) {
                if(!country.equals(rs.getString("country"))) {
                    country = rs.getString("country");
                    rank = 1;
                    System.out.println("\n" + country.toUpperCase());
                    System.out.printf("%-10s %-30s %-20s\n", "Rank", "Host", "# of Listings");
                    System.out.println("------------------------------------------------------------");
                }
                String host = rs.getString("host");
                int listings = rs.getInt("numListings");

                System.out.printf("%-10s %-30s %-20s\n", rank, host, listings);
                rank++;
            }
            System.out
                    .println("------------------------------------------------------------");

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

    public static void findPossibleCommercialHosts(Connection con) {

        try {

            String query = "SELECT * \n" + //
                    "FROM\n" + //
                    "(SELECT \n" + //
                    "host_id, \n" + //
                    "CONCAT(first_name,' ', last_name) as host, \n" + //
                    "country, \n" + //
                    "city, \n" + //
                    "COUNT(*) as numListings,\n" + //
                    "(COUNT(*) / (SELECT COUNT(*) from Listings as l1 where l1.city = l2.city) * 100) as coverage\n" + //
                    "from Listings as l2\n" + //
                    "join Users on host_id = Users.id\n" + //
                    "group by host_id, country, city\n" + //
                    "order by city, country, coverage desc) as t1\n" + //
                    "WHERE t1.coverage > 10";
            
            PreparedStatement stmt = con.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.printf("%-10s %-30s %-20s %-20s %-20s %-10s\n", "Host ID", "Host", "Country", "City", "# of Listings", "Coverage");
            System.out
                    .println("---------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String country = rs.getString("country");
                String city = rs.getString("city");
                String host = rs.getString("host");
                int listings = rs.getInt("numListings");
                int hostId = rs.getInt("host_id");
                float coverage = rs.getFloat("coverage");

                System.out.printf("%-10s %-30s %-20s %-20s %-20s %.2f %%\n", hostId, host, country, city, listings, coverage);
            }
            System.out
                    .println("---------------------------------------------------------------------------------------------------------------------");

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

    public static void BookingsPerRenter(Connection con, boolean city) {
        System.out.println("Enter the start date (YYYY-MM-DD): ");
        String start = sc.nextLine();
        System.out.println("Enter the end date (YYYY-MM-DD): ");
        String end = sc.nextLine();

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate;
        java.util.Date endDate;

        try {
            startDate = parser.parse(start);
            endDate = parser.parse(end);

            String query = "SELECT CONCAT(first_name, ' ', last_name) as renter" + (city ? ", city": "") + ", renter_id, COUNT(*) as ct\n" + //
                            " FROM bookings\n" + //
                            " JOIN Users on Users.id = renter_id\n" + //
                            (city ? "\n  JOIN Listings on Listings.id = listing_id\n" : " ") + //
                            " WHERE start_date >= ? and end_date <= ? \n" + //
                            " GROUP BY renter_id" + (city ? ", city": "") + 
                            "\n ORDER BY " + (city ? "city, ct desc": "ct desc");

            
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setDate(1, new Date(startDate.getTime()));
            stmt.setDate(2, new Date(endDate.getTime()));


            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Bookings per user from " + new Date(startDate.getTime()) + " to " + new Date(endDate.getTime()) + ":");
            System.out
                    .println("--------------------------------------------------------------------------" + (city? "-------------------------": ""));
            if(city)
                System.out.printf("%-20s %-10s %-30s %-10s %20s\n", "City", "Rank", "Renter", "User ID", "# of Bookings");
            else 
                System.out.printf("%-10s %-30s %-10s %20s\n", "Rank", "Renter", "User ID", "# of Bookings");
            System.out
                    .println("--------------------------------------------------------------------------" + (city? "-------------------------": ""));
            int rank = 1;
            String cty = "";
            while (rs.next()) {
                if(city && !cty.equals(rs.getString("city"))) {
                    cty = rs.getString("city");
                    rank = 1;
                    System.out.println("\n" + cty.toUpperCase());
                }
                int renterId = rs.getInt("renter_id");
                String renter = rs.getString("renter");
                int count = rs.getInt("ct");

                if(city)
                    System.out.printf("%-20s %-10s %-30s %-10s %20s\n", cty, rank, renter, renterId, count);
                else 
                    System.out.printf("%-10s %-30s %-10s %20s\n", rank, renter, renterId, count);
                rank++;
            }
            System.out
                    .println("------------------------------------------------------------------------" + (city? "-------------------------": ""));

            rs.close();
            stmt.close();

            // Wait for user input (pressing Enter) before continuing
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void BookingsPerRenterInYear(Connection con) {

        try {

            String query = "SELECT CONCAT(first_name, ' ', last_name) as renter, renter_id, COUNT(*) as ct\n" + //
                            " FROM bookings\n" + //
                            " JOIN Users on Users.id = renter_id\n" + //
                            " where start_date >  DATE_SUB(NOW(),INTERVAL 1 YEAR) and cnt >= 2 \n" + //
                            " GROUP BY renter_id \n"+ 
                            "\n ORDER BY ct desc";

            
            PreparedStatement stmt = con.prepareStatement(query);


            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Bookings per user from the past year:");
            System.out
                    .println("--------------------------------------------------------------------------");

                System.out.printf("%-10s %-30s %-10s %20s\n", "Rank", "Renter", "User ID", "# of Bookings");
            System.out
                    .println("--------------------------------------------------------------------------");
            int rank = 1;
            while (rs.next()) {
                int renterId = rs.getInt("renter_id");
                String renter = rs.getString("renter");
                int count = rs.getInt("ct");
                System.out.printf("%-10s %-30s %-10s %20s\n", rank, renter, renterId, count);
                rank++;
            }
            System.out
                    .println("------------------------------------------------------------------------");

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

    public static void RenterCancelsPerYear(Connection con) {

        try {

            String query = "SELECT renter_id, YEAR(start_date) as yr,\n" + //
                    "CONCAT(first_name, ' ', last_name) as renter,\n" + //
                    "COUNT(*) as numCancels\n" + //
                    "from bookings\n" + //
                    "JOIN Users on Users.id = renter_id\n" + //
                    "where status = 2\n" + //
                    "group by renter_id, yr\n" + //
                    "ORDER by yr, numCancels desc";

            PreparedStatement stmt = con.prepareStatement(query);


            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Renter cancels per year:");
            System.out
                    .println("------------------------------------------------------------------------------------------------");

                System.out.printf("%-20s %-10s %-30s %-10s %20s\n", "Year", "Rank", "Renter", "User ID", "# of Cancels");
            System.out
                    .println("------------------------------------------------------------------------------------------------");
            int rank = 1;
            int year = 0;
            while (rs.next()) {
                if(year != rs.getInt("yr")){
                    year = rs.getInt("yr");
                    rank = 1;
                }
                int renterId = rs.getInt("renter_id");
                String renter = rs.getString("renter");
                int count = rs.getInt("numCancels");
                System.out.printf("%-20s %-10s %-30s %-10s %20s\n", year, rank, renter, renterId, count);
                rank++;
            }
            System.out
                    .println("------------------------------------------------------------------------------------------------");

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

    public static void HostCancelsPerYear(Connection con) {

        try {

            String query = "SELECT host_id, YEAR(start_date) as yr,\n" + //
                    "CONCAT(first_name, ' ', last_name) as host,\n" + //
                    "COUNT(*) as numCancels\n" + //
                    "from bookings\n" + //
                    "JOIN Listings on Listings.id = listing_id\n" + //
                    "JOIN Users on Users.id = Listings.host_id\n" + //
                    "where status = 4\n" + //
                    "group by host_id, yr\n" + //
                    "ORDER by yr, numCancels desc";

            PreparedStatement stmt = con.prepareStatement(query);


            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Host cancels per year:");
            System.out
                    .println("------------------------------------------------------------------------------------------------");

                System.out.printf("%-20s %-10s %-30s %-10s %20s\n", "Year", "Rank", "Host", "User ID", "# of Cancels");
            System.out
                    .println("------------------------------------------------------------------------------------------------");
            int rank = 1;
            int year = 0;
            while (rs.next()) {
                if(year != rs.getInt("yr")){
                    year = rs.getInt("yr");
                    rank = 1;
                }
                int hostId = rs.getInt("host_id");
                String host = rs.getString("host");
                int count = rs.getInt("numCancels");
                System.out.printf("%-20s %-10s %-30s %-10s %20s\n", year, rank, host, hostId, count);
                rank++;
            }
            System.out
                    .println("------------------------------------------------------------------------------------------------");

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
