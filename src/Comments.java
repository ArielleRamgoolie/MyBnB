import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Comments {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void createRenterComment(Connection con, int renter_id) {
        try {

            System.out.println("Add your comment");
            String comment = sc.nextLine();

            System.out.println("Add your rating (0-5)");
            int rating = sc.nextInt();
            sc.nextLine();

            String query = "INSERT INTO RenterComment (`FromUser`, `ToUser`, `Comment`, `Rating`) VALUES (?, ?, ?, ?);";
            // PreparedStatement stmt = con.prepareStatement(query);
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, Users.userId);
            stmt.setInt(2, renter_id);
            stmt.setString(3, comment);
            stmt.setInt(4, rating);
            
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Added your comment successfully!");
                return;
            } else {
                System.out.println("Failed, please try again");
            }

            stmt.close();
            sc.nextLine();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            sc.nextLine();
        }
    }

    public static void createListingComment(Connection con, int listingId) {
        try {

            System.out.println("Add your comment");
            String comment = sc.nextLine();

            System.out.println("Add your rating");
            int rating = sc.nextInt();

            String query = "INSERT INTO ListingComment (`FromUser`, `ListingID`, `Rating`, `Comment`) VALUES (?, ?, ?, ?);";

            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, Users.userId);
            stmt.setInt(2, listingId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Added your comment successfully!");
                return;
            } else {
                System.out.println("Failed, please try again");
            }
            stmt.close();
            sc.nextLine();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            sc.nextLine();
        }
    }
    
    public static void viewComments(Connection con) {
        try {

            String query = "SELECT CONCAT(Users.first_name, ' ', Users.last_name) as host_name, rating, comment\n"
                    + //
                    "FROM RenterComment\n" + //
                    "JOIN Users on Users.id = FromUser\n" + //
                    "WHERE ToUser = ?;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, Users.userId);
            //System.out.print(query + listingId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Comments");
            System.out.printf("%-40s %-10s %-70s\n", "Host", "Rating", "Comment");
            System.out
                    .println("--------------------------------------------------------------------------------------");
            while (rs.next()) {
                String name = rs.getString("host_name");
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");

                System.out.printf("%-40s %-10s %-70s\n", name, rating+"/5", comment);
            }
            System.out.println("--------------------------------------------------------------------------------------");

            rs.close();
            stmt.close();

            System.out.println("\nPress Enter to return...");
            sc.nextLine();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
