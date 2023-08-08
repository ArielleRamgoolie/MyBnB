import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Bookings {
    private static final Scanner sc = new Scanner(System.in);

    private static final int ACTIVE = 1;
    private static final int CANCELLED_BY_HOST = 2;
    private static final int CANCELLED_BY_RENTER = 3;
    private static final int BLOCKED_BY_HOST = 4;

    public static void createBooking(Connection con, int id, float pricing) {
        getAvailability(con, id);
        sc.nextLine();

        System.out.println("Enter the start date (YYYY-MM-DD): ");
        String start = sc.nextLine();
        System.out.println("Enter the end date (YYY-MM-DD): ");
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

        if (startDate.after(endDate) || startDate.equals(endDate) || isUnavailable(con, id, new Date(startDate.getTime()), new Date(endDate.getTime()))) {
            System.out.println("Sorry, the days are not available. Please try again.");
            System.out.println("\nEnter to return...");
            sc.nextLine();
            return;
        }

        int nights = (int) TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);

        System.out.println("\n\n" + "SUMMARY\n");
        System.out.println(nights + " nights");
        if (!Users.isHost)
            System.out.println("$" + pricing * nights);

        System.out.println("\nConfirm booking (y/n):");
        String confirm = sc.nextLine();
        if (confirm == "n\n" || confirm == "N\n")
            return;

        try {

            String query = "INSERT INTO Bookings (`listing_id`, `renter_id`, `start_date`, `end_date`, `price_per_night`, `num_nights`, `total_cost`, `status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, id);
            stmt.setInt(2, Users.userId);
            stmt.setDate(3, new Date(startDate.getTime()));
            stmt.setDate(4, new Date(endDate.getTime()));
            stmt.setDouble(5, pricing);
            stmt.setInt(6, nights);
            stmt.setDouble(7, nights * pricing);
            stmt.setInt(8, 1);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                if (!Users.isHost)
                    System.out.println("Successfully booked off the days!");
                else
                    System.out.println("Added your booking successfully!");
            } else {
                System.out.println("Failed, please try again");
                if (!Users.isHost)
                    System.out.println("You may have to cancel some bookings");
            }
            System.out.println("\nEnter to continue...");
            sc.nextLine();

            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAvailability(Connection con, int listingId) {
        String query = "SELECT * FROM Bookings WHERE listing_id = ? AND CURDATE() < end_date AND (status = "
                + ACTIVE + " OR status = "
                + BLOCKED_BY_HOST + ") ORDER BY start_date;";
        try {
            App.clearScreen();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, listingId);

            ResultSet rs = stmt.executeQuery();
            java.sql.Date start = new java.sql.Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

            System.out
                    .println("--------------------------------------------------------------------------------------");
            System.out.println("Available Days \n");
            while (rs.next()) {
                if (start.after(rs.getDate("start_date", null)) || start.equals(rs.getDate("start_date", null))) {
                    start = rs.getDate("end_date", null);
                } else {
                    System.out.printf("From %-10s to %-10s\n", formatter.format(start),
                            formatter.format(rs.getDate("start_date", null)));
                    start = rs.getDate("end_date", null);
                }
            }
            try {
                if (!start.equals(formatter.parse("31 December 9999")))
                    System.out.println("From " + formatter.format(start) + " onwards");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out
                    .println("--------------------------------------------------------------------------------------");
            rs.close();
            stmt.close();

            System.out.println("\nPress Enter to return...");
            sc.nextLine();

            return;
        } catch (SQLException e) {
            e.printStackTrace();
            sc.nextLine();
        }
    }

    public static boolean isUnavailable(Connection con, int listingId, Date startDate, Date endDate) {
        String query = "SELECT COUNT(*) as result\n" + //
                "FROM bookings\n" + //
                "WHERE listing_id = ?\n" + //
                "AND GREATEST(start_date, ?) < LEAST(end_date, ?)\n" + //
                "AND (status = " + ACTIVE + " or status = " + BLOCKED_BY_HOST + ")\n" + //
                "ORDER BY start_date;";
        try {
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, listingId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);

            ResultSet rs = stmt.executeQuery();

            rs.next();
            
            int result = rs.getInt("result");
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            sc.nextLine();
        }
        return false;
    }

    public static void cancelBooking(Connection con) {
        readBookings(con, true);
        System.out.println("Select booking id from above: ");
        int id = sc.nextInt();

        try {

            String query = "UPDATE Bookings SET status = ? WHERE id = ?;";
            PreparedStatement stmt = con.prepareStatement(query);

            if (Users.isHost)
                stmt.setInt(1, CANCELLED_BY_HOST);
            else
                stmt.setInt(1, CANCELLED_BY_RENTER);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Cancelled your booking successfully!");
            } else {
                System.out.println("Failed, please try again");
            }
            sc.nextLine();
            System.out.println("\nEnter to continue...");
            sc.nextLine();
            return;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readBookings(Connection con, boolean future) {
        try {

            String query = "SELECT Bookings.*, Listings.*, CONCAT(Users.first_name, ' ', Users.last_name) as renter_name FROM Bookings "
                    + "JOIN Listings on Bookings.listing_id = Listings.id "
                    + "JOIN Users on Users.id = " + (Users.isHost ? "Bookings.renter_id" : "Listings.host_id")
                    + " where start_date "
                    + (future ? ">" : "<") + " curdate() AND " + (Users.isHost ? "host_id" : "renter_id")
                    + " = ? AND status = " + ACTIVE + " ORDER BY start_date;";

            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, Users.userId);

            ResultSet rs = stmt.executeQuery();

            App.clearScreen();
            System.out.println("Bookings:");
            SimpleDateFormat parser = new SimpleDateFormat("dd MMMM YYYY");

            System.out.printf("%-10s %-20s %-10s %-20s $ %-10s %-10s %-10s %-20s %-20s\n", "ID",
                    (Users.isHost ? "Renter" : "Host"), "Type", "Address", "Cost",
                    "Nights", "Status", "Check-in", "Check-out");
            System.out.println(
                    "--------------------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {

                System.out.printf("%-10s %-20s %-10s %-20s $ %-10s %-10s %-10s %-20s %-20s\n",
                        rs.getInt("id"),
                        rs.getString("renter_name"),
                        rs.getString("type").toUpperCase(),
                        rs.getString("address"),
                        rs.getDouble("total_cost"),
                        rs.getInt("num_nights"),
                        rs.getString("status"),
                        parser.format(rs.getDate("start_date")),
                        parser.format(rs.getDate("end_date")));

            }

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
