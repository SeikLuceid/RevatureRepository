package com.revature.databases;

import java.rmi.ServerError;
import java.sql.*;
import java.util.Locale;

public class DatabaseConnect
{
    private static final String DATABASE_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/bankapp";
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
            return conn;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean usernameExists(String username)
    {
        try
        {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, username);
            call.execute();
            return call.getBoolean(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }

    private static void verifyConnection() {
        if(conn == null)
            connect();
    }

    public static boolean createUser(String username, String password, String firstName, String lastName, String ssn)
    {
        try
        {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call }");
            return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void finalize()
    {
        try
        {
            conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
