package com.JDBC_TEST;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
	static final String URL = "jdbc:postgresql://localhost:5432/user_management";
	static final String USER = "postgres";
	static final String PASS = "root";

	private static Connection connect() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}

	private static void addBook() {
		try (Connection conn = connect(); Scanner sc = new Scanner(System.in)) {

			System.out.print("Enter book title: ");
			String title = sc.nextLine();

			System.out.print("Enter author: ");
			String author = sc.nextLine();

			System.out.print("Enter price: ");
			double price = sc.nextDouble();

			System.out.print("Enter available copies: ");
			int copies = sc.nextInt();

			String sql = "INSERT INTO books (title, author, price, available_copies) VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, title);
				pstmt.setString(2, author);
				pstmt.setDouble(3, price);
				pstmt.setInt(4, copies);
				pstmt.executeUpdate();
				System.out.println("Book added successfully!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void viewAllBooks() {
		String sql = "SELECT * FROM books ORDER BY id";
		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			System.out.printf("%-5s %-30s %-20s %-10s %-10s%n", "ID", "Title", "Author", "Price", "Copies");
			while (rs.next()) {
				System.out.printf("%-5d %-30s %-20s %-10.2f %-10d%n", rs.getInt("id"), rs.getString("title"),
						rs.getString("author"), rs.getDouble("price"), rs.getInt("available_copies"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void searchBook() {
		try (Connection conn = connect(); Scanner sc = new Scanner(System.in)) {

			System.out.print("Enter keyword (title/author): ");
			String keyword = sc.nextLine();

			String sql = "SELECT * FROM books WHERE title ILIKE ? OR author ILIKE ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");

				try (ResultSet rs = pstmt.executeQuery()) {
					boolean found = false;
					while (rs.next()) {
						found = true;
						System.out.printf("%d | %s | %s | %.2f | %d%n", rs.getInt("id"), rs.getString("title"),
								rs.getString("author"), rs.getDouble("price"), rs.getInt("available_copies"));
					}
					if (!found) {
						System.out.println(" No books found with keyword: " + keyword);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void issueBook() {
		try (Connection conn = connect(); Scanner sc = new Scanner(System.in)) {

			System.out.print("Enter book ID to issue: ");
			int id = sc.nextInt();

			String checkSql = "SELECT available_copies FROM books WHERE id=?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setInt(1, id);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next()) {
						int copies = rs.getInt("available_copies");
						if (copies > 0) {
							String updateSql = "UPDATE books SET available_copies=available_copies-1 WHERE id=?";
							try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
								updateStmt.setInt(1, id);
								updateStmt.executeUpdate();
								System.out.println(" Book issued successfully!");
							}
						} else {
							System.out.println("No copies available!");
						}
					} else {
						System.out.println("Book not found!");
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void returnBook() {
		try (Connection conn = connect(); Scanner sc = new Scanner(System.in)) {

			System.out.print("Enter book ID to return: ");
			int id = sc.nextInt();

			String updateSql = "UPDATE books SET available_copies=available_copies+1 WHERE id=?";
			try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
				pstmt.setInt(1, id);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println("Book returned successfully!");
				} else {
					System.out.println("Book not found!");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteBook() {
		try (Connection conn = connect(); Scanner sc = new Scanner(System.in)) {

			System.out.print("Enter book ID to delete: ");
			int id = sc.nextInt();

			String sql = "DELETE FROM books WHERE id=?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, id);
				int rows = pstmt.executeUpdate();
				if (rows > 0) {
					System.out.println("Book deleted successfully!");
				} else {
					System.out.println("Book not found!");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n========= Library Management System =========");
			System.out.println("1. Add Book");
			System.out.println("2. View All Books");
			System.out.println("3. Search Book");
			System.out.println("4. Issue Book");
			System.out.println("5. Return Book");
			System.out.println("6. Delete Book");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");
			choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				viewAllBooks();
				break;
			case 3:
				searchBook();
				break;
			case 4:
				issueBook();
				break;
			case 5:
				returnBook();
				break;
			case 6:
				deleteBook();
				break;
			case 7:
				System.out.println(" Exiting... Goodbye!");
				break;
			default:
				System.out.println(" Invalid choice!");
			}

		} while (choice != 7);

		sc.close();
	}
}
