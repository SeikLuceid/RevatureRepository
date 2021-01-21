package com.revature.assignments.assignment0;

import java.util.Scanner;

public class ErrorMenu extends ConsoleMenu
{
    ConsoleMenu lastMenu;

    public ErrorMenu(ConsoleMenu displayMenu)
    {
        lastMenu = displayMenu;
    }

    @Override
    void Tick()
    {
        System.out.println("Incorrect Input!");
        System.out.println("Would you like to (T)ry again?");
        System.out.println("(R)eturn to Main Menu?");
        System.out.println("Or (Q)uit the application?");
        Scanner input = new Scanner(System.in);
        char selection = input.next().toUpperCase().charAt(0);
        HandleSelection(selection);
    }

    @Override
    void HandleSelection(char selection)
    {
        switch (selection)
        {
            case 'T':
                BankApp.displayMenu = lastMenu;
                break;
            case 'R':
                BankApp.displayMenu = new MainMenu();
                break;
            case 'Q':
                BankApp.quit = true;
                break;
            default:
                System.out.println("Invalid Input, please try again!");
        }
    }

    @Override
    void HandleSelection(String selection) {
    }
}
