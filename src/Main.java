
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_URL = "jdbc:sqlite:database/booktracker.db";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Booktracker");

        while (running) {
            printMenu();
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" ->
                    addUser(scanner);
                case "2" ->
                    getReadingHabitsForUser(scanner);
                case "3" ->
                    changeBookTitle(scanner);
                case "4" ->
                    deleteReadingHabit(scanner);
                case "5" ->
                    getMeanAge();
                case "6" ->
                    getUsersWhoReadBook(scanner);
                case "7" ->
                    getTotalPagesRead();
                case "8" ->
                    getUsersWithMoreThanOneBook();
                case "9" ->
                    addNameColumn();
                case "0" ->
                    running = false;
                default ->
                    System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println( """
             1. Add a user
             2. View reading habits for a user
             3. Change a book title
             4. Delete a reading habit record
             5. Mean age of all users
             6. Number of users who read a specific book
             7. Total pages read by all users
             8. Number of users who read more than one book
             9. Add 'Name' column to User table
             0. Exit """);
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void addUser(Scanner scanner) {
        System.out.print("Enter userID (integer): ");
        int userID = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter gender (m/f/other): ");
        String gender = scanner.nextLine().trim();

        // Parameterised query – the ? placeholders prevent SQL injection.
        String sql = "INSERT INTO \"User\" (userID, age, gender) VALUES (?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.executeUpdate();

            System.out.println("User " + userID + " added successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void getReadingHabitsForUser(Scanner scanner) {
        System.out.print("Enter userID: ");
        int userID = Integer.parseInt(scanner.nextLine().trim());

        String sql = "SELECT * FROM ReadingHabit WHERE userID = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            System.out.printf("%-10s %-10s %-12s %-25s %-20s%n",
                    "habitID", "userID", "pagesRead", "book", "submissionMoment");
            System.out.println("-".repeat(80));

            while (rs.next()) {
                found = true;
                System.out.printf("%-10d %-10d %-12d %-25s %-20s%n",
                        rs.getInt("habitID"),
                        rs.getInt("userID"),
                        rs.getInt("pagesRead"),
                        rs.getString("book"),
                        rs.getString("submissionMoment"));
            }

            if (!found) {
                System.out.println("No reading habits found for user " + userID + ".");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }
    }

    private static void changeBookTitle(Scanner scanner) {
        System.out.print("Enter the current book title: ");
        String oldTitle = scanner.nextLine().trim();

        System.out.print("Enter the new book title: ");
        String newTitle = scanner.nextLine().trim();

        String sql = "UPDATE ReadingHabit SET book = ? WHERE book = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTitle);
            pstmt.setString(2, oldTitle);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Updated " + rows + " record(s) with the new title.");
            } else {
                System.out.println("No book found with that title.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating title: " + e.getMessage());
        }
    }

    private static void deleteReadingHabit(Scanner scanner) {
        System.out.print("Enter the habitID to delete: ");
        int habitID = Integer.parseInt(scanner.nextLine().trim());

        String sql = "DELETE FROM ReadingHabit WHERE habitID = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, habitID);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Record " + habitID + " deleted successfully.");
            } else {
                System.out.println("No record found with habitID " + habitID + ".");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    private static void getMeanAge() {
        String sql = "SELECT AVG(age) AS meanAge FROM \"User\"";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.printf("Mean age of all users: %.2f%n", rs.getDouble("meanAge"));
            }

        } catch (SQLException e) {
            System.out.println("Error calculating mean age: " + e.getMessage());
        }
    }

    private static void getUsersWhoReadBook(Scanner scanner) {
        System.out.print("Enter the book title: ");
        String book = scanner.nextLine().trim();

        String sql = "SELECT COUNT(DISTINCT userID) AS total FROM ReadingHabit WHERE book = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Users who read \"" + book + "\": " + rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getTotalPagesRead() {
        String sql = "SELECT SUM(pagesRead) AS totalPages FROM ReadingHabit";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Total pages read by all users: " + rs.getInt("totalPages"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getUsersWithMoreThanOneBook() {
        String sql =  """
            SELECT COUNT(*) AS total
            FROM (
                SELECT userID
                FROM ReadingHabit
                GROUP BY userID
                HAVING COUNT(DISTINCT book) > 1
            )
            """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Users who read more than one book: " + rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addNameColumn() {
        String sql = "ALTER TABLE \"User\" ADD COLUMN Name TEXT";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Column 'Name' added to the User table.");

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate column name")) {
                System.out.println("Column 'Name' already exists.");
            } else {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
