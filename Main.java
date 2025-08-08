import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin(sc);
        User user = new User(sc);

        while (true) {
            System.out.println("\n=== Railway Booking System ===");
            System.out.println("1. Admin\n2. User\n3. Exit");
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
                case 1:
                    System.out.print("Enter Admin Password: ");
                    String pass = sc.nextLine();
                    if (pass.equals("admin123")) {
                        try {
							admin.adminMenu();
						} catch (Exception e) {
							e.printStackTrace();
						}
                    } else {
                        System.out.println("Wrong password.");
                    }
                    break;
                case 2:
				try {
					user.userMenu();
				} catch (Exception e) {
					e.printStackTrace();
				}
                    break;
                case 3:
                    System.out.println("Exiting system...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}