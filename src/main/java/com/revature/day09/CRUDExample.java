package com.revature.day09;

import java.sql.*;
import java.util.Scanner;

public class CRUDExample
{
    public static void main(String[] args)
    {
        try(Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mattdb", "postgres", "db-admin"))
        {
            Statement query = conn.createStatement();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO student(first_name, last_name, age) VALUES (?, ?, ?);");
            stmt.setString(1, "Bill");
            stmt.setString(2, "Knight");
            stmt.setInt(3, 99);

            query.execute("INSERT INTO student(first_name, last_name, age) VALUES ('Matt', 'Blank', 3), ('Steven', 'Zero', 5);");

            query.execute("UPDATE student SET first_name = 'GreaterThan0' WHERE id > 0;");

            ResultSet results = query.executeQuery("SELECT * FROM student;");
            while(results.next())
            {
                System.out.println(results.getString(2) + " " + results.getString(3));
            }

            query.execute("DELETE FROM student WHERE id = 1;");

            results = query.executeQuery("SELECT * FROM student;");
            while(results.next())
            {
                System.out.println(results.getString(2) + " " + results.getString(3));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

