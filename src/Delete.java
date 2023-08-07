import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Delete {
    private static final Scanner sc = new Scanner(System.in);

    public static void user(Connection con){
        try{
            System.out.println("Are you sure you would like to delete your account? (Y/N)");
            System.out.println("NOTE: If you have any bookings, you must first cancel these before deleting your account");

            String res = sc.nextLine();
            if (res.equals("Y")){

                // Check that user has no bookings
                String querycount = "SELECT COUNT(*) AS bookings_count FROM Bookings WHERE renter_id = ?";
                PreparedStatement stmt = con.prepareStatement(querycount);
                stmt.setInt(1, Users.userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    int bookings = rs.getInt("bookings_count");
                    if (bookings > 0){
                        System.out.println("Unable  to delete account. Please cancel all bookings first");
                    }else {
                        String deleteUserQuery = "DELETE FROM Users WHERE id = ?";
                        PreparedStatement newstmt = con.prepareStatement(deleteUserQuery);
                        newstmt.setInt(1, Users.userId);
                        int newrowsAffected = newstmt.executeUpdate(); // Use newstmt here
                        newstmt.close();
                        
                        if (newrowsAffected > 0) {
                            System.out.println("User account deleted successfully.");
                        } else {
                            System.out.println("Failed to delete user account.");
                        }
                    }
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void host(Connection con){
        try{
            System.out.println("Are you sure you would like to delete your account? (Y/N)");
            System.out.println("NOTE: If you have any listings and or bookings, you must first cancel all before deleting your account");

            String res = sc.nextLine();
            if (res.equals("Y")){

                // Check that host has no listings
                String querycount = "SELECT COUNT(*) AS listings_count FROM Listings WHERE host_id = ?";
                PreparedStatement stmt = con.prepareStatement(querycount);
                stmt.setInt(1, Users.userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    int listings = rs.getInt("listings_count");
                    if (listings > 0){
                        System.out.println("Unable  to delete account. Please cancel all listings first");
                    }else {
                        String deleteUserQuery = "DELETE FROM Users WHERE id = ?";
                        PreparedStatement newstmt = con.prepareStatement(deleteUserQuery);
                        newstmt.setInt(1, Users.userId);
                        int newrowsAffected = newstmt.executeUpdate(); // Use newstmt here
                        newstmt.close();
                        
                        if (newrowsAffected > 0) {
                            System.out.println("Host account deleted successfully.");
                        } else {
                            System.out.println("Failed to delete host account.");
                        }
                    }
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
