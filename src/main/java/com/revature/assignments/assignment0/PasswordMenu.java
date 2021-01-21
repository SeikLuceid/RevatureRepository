package com.revature.assignments.assignment0;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Scanner;

public class PasswordMenu extends ConsoleMenu
{
    void Tick()
    {
        System.out.println("Please enter your login password:");
        Scanner input = new Scanner(System.in);
        String selection = input.next();
        HandleSelection(selection);
    }

    void HandleSelection(char selection) { }

    void HandleSelection(String selection)
    {
        //CHANGE TO DATABASE
        if(selection.equals("dbadmin"))
            throw new NotImplementedException();
        else
            BankApp.displayMenu = new ErrorMenu(BankApp.displayMenu);
    }
}
