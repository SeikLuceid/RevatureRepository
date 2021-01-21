package com.revature.databases;

import java.sql.*;

public class DatabaseConnect
{
    private static final String DATABASE_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mattdb";
    private static final String DATABASE_USER_NAME = "postgres";
    private static final String DATABASE_USER_PASSWORD = "db-admin";

    private static Connection conn;

    private DatabaseConnect() { }

    public static Connection connect()
    {
        if(conn != null)
            return conn;
        try
        {
            //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DATABASE_CONNECTION_STRING, DATABASE_USER_NAME, DATABASE_USER_PASSWORD );
            System.out.println("You are successfully connected to the PostgreSQL database server.");

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            conn = null;
        }

        return conn;
    }
}
