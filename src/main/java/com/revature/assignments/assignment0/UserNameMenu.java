package com.revature.assignments.assignment0;

import java.util.Scanner;

public class UserNameMenu extends ConsoleMenu {
    public void Tick() {
        System.out.println("Please enter your login email:");
        Scanner input = new Scanner(System.in);
        String selection = input.next().toUpperCase();
        HandleSelection(selection);
    }

    public void HandleSelection(char selection)
    { }

    void HandleSelection(String selection)
    {
        //CHANGE TO DATABASE
        if(selection.equals("MATTHEW.SUTTLES@REVATURE.NET"))
            BankApp.displayMenu = new PasswordMenu();
        else
            BankApp.displayMenu = new ErrorMenu(BankApp.displayMenu);
    }
}
