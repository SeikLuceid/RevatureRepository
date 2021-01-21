package com.revature.assignments.assignment0;

import com.revature.databases.DatabaseConnect;
import java.sql.Connection;
import java.util.Scanner;

public class BankApp
{
    public static boolean quit = false;
    public static ConsoleMenu displayMenu = new MainMenu();

    public static void main(String[] args)
    {
        System.out.println("Welcome to Bank BS");
        do
        {
            displayMenu.Tick();
        }
        while(!quit);
    }
}
