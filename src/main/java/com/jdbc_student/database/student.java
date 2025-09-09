package com.jdbc_student.database;

import java.sql.*;
import java.util.Scanner;

public class student {
	public static void main(String[] args) {
		String URL = "jdbc:postgresql://localhost:5432/user_management";
		String USERNAME = "postgres";
		String PASSWORD = "root";

		Scanner scanner = new Scanner(System.in);

		try {
			// Load driver
			Class.forName("org.postgresql.Driver");

			// Establish the connection
			Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

			int choice;
			do {
				System.out.println("\n====== Student Management System ======");
				System.out.println("1. Add Student");
				System.out.println("2. View All Students");
				System.out.println("3. Update Student");
				System.out.println("4. Delete Student");
				System.out.println("5. Search Student by ID");
				System.out.println("6. Exit");
				System.out.print("Enter your choice: ");
				choice = scanner.nextInt();

				switch (choice) {
				case 1:
					// Add Student
					System.out.print("Enter name: ");
					String name = scanner.next();
					System.out.print("Enter age: ");
					int age = scanner.nextInt();
					System.out.print("Enter cource: ");
					String cource = scanner.next();

					String insertStudent = "INSERT INTO student.database(name, age, cource) VALUES (?, ?, ?)";
					PreparedStatement pstmt = con.prepareStatement(insertStudent);
					pstmt.setString(1, name);
					pstmt.setInt(2, age);
					pstmt.setString(3, cource);
					pstmt.executeUpdate();
					System.out.println(" Student added successfully.");
					break;

				case 2:
					// View All Students
					String selectStudent = "SELECT * FROM student.database";
					PreparedStatement selectStmt = con.prepareStatement(selectStudent);
					ResultSet rs = selectStmt.executeQuery();
					while (rs.next()) {
						System.out.printf("%d | %s | %d | %s%n", rs.getInt("id"), rs.getString("name"),
								rs.getInt("age"), rs.getString("cource"));
					}
					break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   

				case 3:
					// Update Student
					System.out.print("Enter student ID to update: ");
					int updateId = scanner.nextInt();
					System.out.print("Enter new name: ");
					String newName = scanner.next();
					
					System.out.print("Enter new cource: ");
					String newcource = scanner.next();

					String updateStudent = "UPDATE student.database SET name = ?,cource = ? WHERE id = ?";
					PreparedStatement updateStmt = con.prepareStatement(updateStudent);
					updateStmt.setString(1, newName);
					updateStmt.setString(2, newcource);
					
					int rowsUpdated = updateStmt.executeUpdate();
					if (rowsUpdated > 0)
						System.out.println(" Student updated successfully.");
					else
						System.out.println(" No student found with ID " + updateId);
					break;

				case 4:
					// Delete Student
					System.out.print("Enter student ID to delete: ");
					int deleteId = scanner.nextInt();

					String deleteSQL = "DELETE FROM student.database WHERE id = ?";
					PreparedStatement deleteStmt = con.prepareStatement(deleteSQL);
					deleteStmt.setInt(1, deleteId);
					int rowsDeleted = deleteStmt.executeUpdate();
					if (rowsDeleted > 0)
						System.out.println(" Student deleted successfully.");
					else
						System.out.println(" No student found with ID " + deleteId);
					break;

				case 5:
					// Search by ID
					System.out.print("Enter student ID to search: ");
					int searchId = scanner.nextInt();

					String searchStudent = "SELECT * FROM student.database WHERE id = ?";
					PreparedStatement searchStmt = con.prepareStatement(searchStudent);
					searchStmt.setInt(1, searchId);
					ResultSet result = searchStmt.executeQuery();

					if (result.next()) {
						System.out.println("ID: " + result.getInt("id"));
						System.out.println("Name: " + result.getString("name"));
						System.out.println("Age: " + result.getInt("age"));
						System.out.println("cource: " + result.getString("cource"));
					} else {
						System.out.println("No student found with ID " + searchId);
					}
					break;

				case 6:
					System.out.println(" Exiting...");
					break;

				default:
					System.out.println(" Invalid choice! Please try again.");
				}

			} while (choice != 6);

			con.close();
			scanner.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
