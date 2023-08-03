import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
