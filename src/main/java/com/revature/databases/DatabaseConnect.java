package com.revature.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect
{
    private static final String DATABASE_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/bankapp";
    private static final String DATABASE_USER_NAME = "bankapp";
    private static final String DATABASE_USER_PASSWORD = "sl-admin";

    private static Connection conn;

    private DatabaseConnect() { }

    public static Connection connect()
    {
        if(conn != null)
            return conn;
        try
        {
            conn = DriverManager.getConnection(DATABASE_CONNECTION_STRING, DATABASE_USER_NAME, DATABASE_USER_PASSWORD );
            System.out.println("You are successfully connected to the PostgreSQL database server.");

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            conn = null;
        }
        return conn;
    }
}
