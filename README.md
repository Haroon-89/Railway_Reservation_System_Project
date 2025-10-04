## 🚂 Railway Reservation System Project

This is a **Command-Line Interface (CLI)** based Railway Reservation and Ticketing System built in **Java** using **JDBC** for persistence. The system allows for two distinct roles: **Admin** and **User**, to manage trains, book tickets, and view/cancel reservations.

-----

## ✨ Features

### Main Menu

  * **Admin Access:** Secure access to administrative functions with a hardcoded password.
  * **User Access:** Access to user functions like signup, login, and ticketing.

### Admin Features (Password: `admin123`)

  * **Add Train:** Register a new train with details like number, name, source, destination, and initial seat capacity.
  * **View Trains:** Display a list of all available trains.
  * **Cancel Train:** Remove a train from the system, which automatically cancels all associated tickets.

### User Features

  * **Sign Up:** Create a new user account.
  * **Login:** Authenticate an existing user to access booking features.
  * **Book Ticket:** Search for trains, select a train, and book an available seat.
  * **View Tickets:** Display all tickets booked by the logged-in user.
  * **Cancel Ticket:** Cancel a specific ticket, which automatically increases the train's available seat count.

-----

## 🛠️ Requirements and Setup

### 1\. Technology Stack

  * **Language:** Java
  * **Database:** MySQL
  * **Connectivity:** JDBC (Java Database Connectivity)

### 2\. Database Configuration

The application assumes a MySQL database named **`Railway`** is running on `localhost:3306`. You must set up the database and update the connection details if your configuration is different.

**Connection Details (found in `Admin.java`, `User.java`, and `DBMS_Connect.java`):**

| Parameter | Value |
| :--- | :--- |
| **Driver** | `com.mysql.cj.jdbc.Driver` |
| **URL** | `jdbc:mysql://localhost:3306/Railway` |
| **Username** | `root` |
| **Password** | `1234` |

**You must create the following tables in your `Railway` database:**

| Table Name | Purpose |
| :--- | :--- |
| **`user`** | Stores user credentials (username, password, name). |
| **`train`** | Stores train details (train number, name, source, destination, seats). |
| **`ticket`** | Stores reservation details (ticket ID, username, train number, seat number). |

### 3\. Compilation and Execution

1.  **Dependencies:** Ensure you have the **MySQL Connector/J** (`.jar` file) in your classpath.
2.  **Compile:** Compile the Java files (`Main.java`, `Admin.java`, `User.java`, `DBMS_Connect.java`).
    ```bash
    javac Main.java Admin.java User.java DBMS_Connect.java
    ```
3.  **Run:** Execute the main class.
    ```bash
    java Main
    ```

-----

## 📄 File Structure

```
Railway_Reservation_System_Project-main/
├── Main.java           # Entry point and main menu loop (Admin/User selection).
├── Admin.java          # Handles all Admin menu operations (Add/View/Cancel Train).
├── User.java           # Handles all User menu operations (Signup/Login/Book/View/Cancel Ticket).
├── DBMS_Connect.java   # Utility class to test the initial database connection.
└── README.md           # This documentation file.
```
