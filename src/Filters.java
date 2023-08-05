import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Filters {
    private static final Scanner sc = new Scanner(System.in);

    public static void priceRange(String query, Connection con){
        try {
            // get price range from user
            System.out.println("Enter minimum price:");
            Float min = sc.nextFloat();
            System.out.println("Enter maximum price:");
            Float max = sc.nextFloat();
            String updateQuery = query + " AND WHERE price BETWEEN "+ min + " AND " + max;
            System.out.println(updateQuery);
            // Wait for user input (pressing Enter) before continuing
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(updateQuery);

            // App.clearScreen();
            System.out.println("All Listings (Range Range Tester):");
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
}
