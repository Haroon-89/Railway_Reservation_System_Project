import java.sql.*;
import java.util.Scanner;

public class Admin {
    private Scanner sc;

    public Admin(Scanner sc) {
        this.sc = sc;
    }

    void adminMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Train\n2. View Trains\n3. Cancel Train\n4. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice;
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            sc.nextLine(); // Clear newline

            switch (choice) {
                case 1: addTrain(); break;
                case 2: viewTrains(); break;
                case 3: cancelTrain(); break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    void addTrain() throws Exception {
        // ... (unchanged, use this.sc for input, no sc.close())
        System.out.print("Enter Train Number: ");
        int tnum = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Train Name: ");
        String tname = sc.nextLine();
        System.out.print("Enter Total Seats: ");
        int seats = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Boarding Point: ");
        String bp = sc.nextLine();
        System.out.print("Enter Destination Point: ");
        String dp = sc.nextLine();
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String uname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, uname, pwd);

        PreparedStatement ps = con.prepareStatement("INSERT INTO train VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, tnum);
        ps.setString(2, tname);
        ps.setInt(3, seats);
        ps.setString(4, bp);
        ps.setString(5, dp);
        ps.executeUpdate();

        System.out.println("Train added successfully!");
        con.close();
    }

    void viewTrains() throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String uname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, uname, pwd);

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM train");

        System.out.println("\nTrain List:");
        System.out.println("Number | Name | Seats | Boarding | Destination");
        while (rs.next()) {
            System.out.printf("%d | %s | %d | %s | %s\n",
                rs.getInt(1), rs.getString(2), rs.getInt(3),
                rs.getString(4), rs.getString(5));
        }
    }

    void cancelTrain() throws Exception {
        System.out.print("Enter Train Number to cancel: ");
        int tnum = sc.nextInt();

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/Railway";
        String uname = "root";
        String pwd = "1234";

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, uname, pwd);

        // Check if train exists
        PreparedStatement checkPs = con.prepareStatement("SELECT * FROM train WHERE tnum = ?");
        checkPs.setInt(1, tnum);
        ResultSet rs = checkPs.executeQuery();
        if (!rs.next()) {
            System.out.println("Train number does not exist.");
            return;
        }

        // Delete associated tickets
        PreparedStatement deleteTicketsPs = con.prepareStatement("DELETE FROM ticket WHERE tnum = ?");
        deleteTicketsPs.setInt(1, tnum);
        int ticketsDeleted = deleteTicketsPs.executeUpdate();

        // Delete the train
        PreparedStatement deleteTrainPs = con.prepareStatement("DELETE FROM train WHERE tnum = ?");
        deleteTrainPs.setInt(1, tnum);
        int trainDeleted = deleteTrainPs.executeUpdate();

        if (trainDeleted > 0) {
            System.out.println("Train cancelled successfully. " + ticketsDeleted + " ticket(s) also cancelled.");
        } else {
            System.out.println("Failed to cancel train.");
        }

        con.close();
    }
}