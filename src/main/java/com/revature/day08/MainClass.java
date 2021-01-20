package com.revature.day08;

import com.revature.databases.DatabaseConnect;
import java.sql.Connection;

public class MainClass
{
    public static void main(String[] args)
    {
        Connection conn = DatabaseConnect.connect();

        if(conn != null)
        {
            System.out.println("Work with Database!");
        }
        else
        {
            System.out.println("You're offline!");
        }
    }
}
