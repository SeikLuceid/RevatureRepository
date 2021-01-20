package com.revature.assignments.assignment0;

import com.revature.databases.DatabaseConnect;
import java.sql.Connection;

public class BankApp
{
    private static Connection conn;

    public static void main(String[] args)
    {
        Connection conn = DatabaseConnect.connect();
    }

    public static Connection TestConnection()
    {
        if(conn == null)
            conn = DatabaseConnect.connect();
        return conn;
    }
}
