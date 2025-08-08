import java.sql.*;
import java.util.Scanner;

public class User {
    private Scanner sc;
    private String loggedUser = "";

    public User(Scanner sc) {
        this.sc = sc;
    }

    void userMenu() throws Exception {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Sign Up\n2. Login\n3. Book Ticket\n4. View Tickets\n5. Cancel Ticket\n6. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice;
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            switch (choice) {
                case 1: signUp(); break;
                case 2: login(); break;
                case 3: bookTicket(); break;
                case 4: viewTickets(); break;
                case 5: cancelTicket(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    
    void signUp() throws Exception {
        System.out.print("Enter username: ");
        String uname = sc.next();
        System.out.print("Enter password: ");
        String pass = sc.next();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        System.out.print("Enter gender: ");
        String gender = sc.next();

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String dbUname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUname, pwd);

        PreparedStatement ps = con.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?)");
        ps.setString(1, uname);
        ps.setString(2, pass);
        ps.setInt(3, age);
        ps.setString(4, gender);
        ps.executeUpdate();
        System.out.println("Sign up successful.");
    }

    void login() throws Exception {
        System.out.print("Username: ");
        String uname = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String dbUname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUname, pwd);

        PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE uname=? AND pass=?");
        ps.setString(1, uname);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            loggedUser = uname;
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    void bookTicket() throws Exception {
        if (loggedUser.equals("")) {
            System.out.println("Please login first.");
            return;
        }

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String dbUname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUname, pwd);

        // Display available trains
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM train");
        System.out.println("\nAvailable Trains:");
        while (rs.next()) {
            System.out.printf("%d | %s | Seats Left: %d\n", rs.getInt(1), rs.getString(2), rs.getInt(3));
        }

        // Get user input
        System.out.print("Enter train number to book: ");
        int tnum = sc.nextInt();
        System.out.print("Enter seat number: ");
        int seat = sc.nextInt();
        sc.nextLine(); // Clear newline

        // Check if the train exists and has available seats
        PreparedStatement checkSeatsPs = con.prepareStatement("SELECT seats FROM train WHERE tnum = ?");
        checkSeatsPs.setInt(1, tnum);
        ResultSet seatsRs = checkSeatsPs.executeQuery();
        if (seatsRs.next()) {
            int availableSeats = seatsRs.getInt("seats");
            if (availableSeats <= 0) {
                System.out.println("No seats available on this train.");
                con.close();
                return;
            }
        } else {
            System.out.println("Invalid train number.");
            con.close();
            return;
        }

        // Check if the chosen seat is already booked
        PreparedStatement checkSeatPs = con.prepareStatement("SELECT COUNT(*) FROM ticket WHERE tnum = ? AND seat_no = ?");
        checkSeatPs.setInt(1, tnum);
        checkSeatPs.setInt(2, seat);
        ResultSet seatRs = checkSeatPs.executeQuery();
        seatRs.next();
        int count = seatRs.getInt(1);
        if (count > 0) {
            System.out.println("Seat " + seat + " is already booked on this train.");
            con.close();
            return;
        }

        // Proceed with booking
        PreparedStatement ps = con.prepareStatement("INSERT INTO ticket(uname, tnum, seat_no) VALUES (?, ?, ?)");
        ps.setString(1, loggedUser);
        ps.setInt(2, tnum);
        ps.setInt(3, seat);
        ps.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement("UPDATE train SET seats = seats - 1 WHERE tnum = ?");
        ps2.setInt(1, tnum);
        ps2.executeUpdate();

        System.out.println("Ticket booked successfully!");
        con.close();
    }

    void viewTickets() throws Exception {
        if (loggedUser.equals("")) {
            System.out.println("Please login first.");
            return;
        }

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String dbUname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUname, pwd);

        PreparedStatement ps = con.prepareStatement("SELECT * FROM ticket WHERE uname = ?");
        ps.setString(1, loggedUser);
        ResultSet rs = ps.executeQuery();

        System.out.println("\nYour Tickets:");
        while (rs.next()) {
            System.out.printf("Ticket ID: %d | Train No: %d | Seat No: %d\n",
                rs.getInt(1), rs.getInt(3), rs.getInt(4));
        }
    }

    void cancelTicket() throws Exception {
        if (loggedUser.equals("")) {
            System.out.println("Please login first.");
            return;
        }

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String dbUname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, dbUname, pwd);

        // Display user's tickets
        PreparedStatement ps = con.prepareStatement("SELECT * FROM ticket WHERE uname = ?");
        ps.setString(1, loggedUser);
        ResultSet rs = ps.executeQuery();

        System.out.println("\nYour Tickets:");
        boolean hasTickets = false;
        while (rs.next()) {
            hasTickets = true;
            System.out.printf("Ticket ID: %d | Train No: %d | Seat No: %d\n",
                rs.getInt(1), rs.getInt(3), rs.getInt(4));
        }

        if (!hasTickets) {
            System.out.println("No tickets found to cancel.");
            return;
        }

        // Prompt for ticket ID to cancel
        System.out.print("Enter Ticket ID to cancel: ");
        int ticketId = sc.nextInt();

        // Verify ticket belongs to user
        PreparedStatement checkPs = con.prepareStatement("SELECT tnum FROM ticket WHERE id = ? AND uname = ?");
        checkPs.setInt(1, ticketId);
        checkPs.setString(2, loggedUser);
        ResultSet checkRs = checkPs.executeQuery();

        if (!checkRs.next()) {
            System.out.println("Invalid Ticket ID or ticket does not belong to you.");
            return;
        }

        int tnum = checkRs.getInt("tnum");

        // Delete ticket
        PreparedStatement deletePs = con.prepareStatement("DELETE FROM ticket WHERE id = ?");
        deletePs.setInt(1, ticketId);
        int deleted = deletePs.executeUpdate();

        // Increment train seats
        PreparedStatement updatePs = con.prepareStatement("UPDATE train SET seats = seats + 1 WHERE tnum = ?");
        updatePs.setInt(1, tnum);
        updatePs.executeUpdate();

        if (deleted > 0) {
            System.out.println("Ticket cancelled successfully.");
        } else {
            System.out.println("Failed to cancel ticket.");
        }

        con.close();
    }
}