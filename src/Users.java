import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Users {
    private static final Scanner sc = new Scanner(System.in);
    public static int userId = -1;
    public static boolean isHost = false;
        
    public static void createUser(Connection con){
        try {
            // Assume that user will always enter all of their data (:
            System.out.println("Enter your username: ");
            String username = sc.next();
        
            System.out.println("Enter your password: ");
            String password = sc.next();

            System.out.println("Enter your first name: ");
            String first_name = sc.next();

            System.out.println("Enter your last name: ");
            String last_name = sc.next();
            
            System.out.println("Enter your address: ");
            String address = sc.next();

            String dob;
            do {
                System.out.println("Enter your date of birth (yyyy-mm-dd): ");
                dob = sc.next();
            } while (!isValidDateFormat(dob));

            if (!isAdult(dob)) {
                System.out.println("You must be 18 years or older to register.");
                return;
            }

            System.out.println("Enter your SIN I am the bank >;) : ");
            int SIN = sc.nextInt();
            sc.nextLine();
            
            System.out.println("Enter your occupation: ");
            String occupation = sc.next();
            
            System.out.println("Enter which type of user you are (h - host or r - renter): ");
            String type = sc.next();
            
            // only collect a renter's payment info
            String payment;
            if (type.equals("r")){
                System.out.println("Enter your payment info hehehehe >:) : ");
                payment = sc.next();
            } else {
                payment = "null";
            }
            
            String query = "INSERT INTO Users (`username`, `password`, `first_name`, `last_name`, `address`, `dob`, `occupation`, `SIN`, `type`, `payment_info`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = con.prepareStatement(query);
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, first_name);
            stmt.setString(4, last_name);
            stmt.setString(5, address);
            stmt.setString(6, dob);
            stmt.setString(7, occupation);
            stmt.setInt(8, SIN);
            stmt.setString(9, type);
            stmt.setString(10, payment);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                System.out.println("Added successfully!");
            } else {
                System.out.println("Failed");
            }
            App.startApp(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Regular expression to check the date format (yyyy-mm-dd)
    private static final Pattern DATE_FORMAT_REGEX = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    // Method to validate the date format (yyyy-mm-dd)
    private static boolean isValidDateFormat(String date) {
        Matcher matcher = DATE_FORMAT_REGEX.matcher(date);
        return matcher.matches();
    }

    // Method to check if the user is 18 years or older
    private static boolean isAdult(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday);
        LocalDate currentDate = LocalDate.now();

        boolean valid = Period.between(birthDate, currentDate).getYears() > 17;

        return valid; 
    }

    public static void loginUser(Connection con) {
        System.out.println("Enter your username: ");
        String username = sc.next();
        
        System.out.println("Enter your password: ");
        String password = sc.next();

        try {
            // Prepare the SQL query to check if the credentials are valid in the database.
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            String query2 = "SELECT * FROM Users WHERE username = ? AND password = ? AND type = 'h'";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setString(1, username);
            stmt2.setString(2, password);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();

            // Check if the result set has any rows (i.e., if the credentials match any user in the database)
            boolean isValidUser = rs.next();

            int hostID = -1; // Initialize the hostID with a default value
            int renterID = -1;

            if (isValidUser){
                isHost = rs2.next();
                if (isHost) {
                    hostID = rs2.getInt("id");
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    userId = hostID;
                    Menus.hostMenu(con, hostID);
                } else {
                    renterID = rs.getInt("id");
                    userId = renterID;
                    Menus.renterMenu(con, renterID); 
                }
            }else {
                System.out.println("Unsuccessful Login");
            }
            
            // Close the resources
            rs.close();
            stmt.close();
            rs2.close();
            stmt2.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
