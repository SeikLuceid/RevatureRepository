package com.revature.day09;

import java.sql.*;
import java.util.Scanner;

public class PreparedExample {
    public static void main(String[] args) {
        try (Connection connection =
                     DriverManager.getConnection("jdbc:postgresql://localhost:5432/dvdrental", "postgres", "db-admin")) {
            // When this class first attempts to establish a connection, it automatically loads any JDBC 4.0 drivers found within
            // the class path. Note that your application must manually load any JDBC drivers prior to version 4.0.
//	      Class.forName("org.postgresql.Driver");
            System.out.println("Connected to PostgreSQL database!");
            PreparedStatement statement = connection.prepareStatement("select * from city where city_id=?");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter City :");
            int city = scanner.nextInt();
            statement.setInt(1, city);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.print(resultSet.getInt(1) + " - ");
                System.out.println(resultSet.getString("city"));
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
