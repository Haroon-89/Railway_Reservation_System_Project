import java.sql.Connection;
import java.sql.DriverManager;

public class DBMS_Connect {
    public static void main(String[] args) {
    	String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/Railway";
		String uname = "root";
		String pwd = "1234";
		
		try {
			Class.forName(driver);
			System.out.println("Driver is Ready");
			Connection con = DriverManager.getConnection(url, uname, pwd);
			System.out.println("Connection is ready");
			con.close();
			System.out.println("Connection closed");
		}
		catch(Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
    }
}
