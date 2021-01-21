package com.revature.day09;

import java.sql.*;

public class PreparedCRUDExample {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mattdb", "postgres", "db-admin"))
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO student(first_name, last_name, age) VALUES (?, ?, ?), (?, ?, ?);");
            stmt.setString(1, "Bill");
            stmt.setString(2, "Knight");
            stmt.setInt(3, 99);
            stmt.setString(4, "Alex");
            stmt.setString(5, "Knight");
            stmt.setInt(6, 96);
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE student SET first_name = ? WHERE id > ?;");
            stmt.setString(1, "SomeName");
            stmt.setInt(2, 5);
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT * FROM student;");
            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                System.out.println(results.getString(2) + " " + results.getString(3));
            }

            stmt = conn.prepareStatement("DELETE FROM student WHERE id = ?;");
            stmt.setInt(1, 5);
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT * FROM student;");
            results = stmt.executeQuery();
            while (results.next()) {
                System.out.println(results.getString(2) + " " + results.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
