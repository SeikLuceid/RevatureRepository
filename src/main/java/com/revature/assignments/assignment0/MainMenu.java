package com.revature.assignments.assignment0;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Scanner;

public class MainMenu extends ConsoleMenu
{
    public void Tick()
    {
        System.out.println("You may (L)ogin, or (R)egister for an account here, or (Q)uit.");
        Scanner input = new Scanner(System.in);
        char selection = input.next().toUpperCase().charAt(0);
        HandleSelection(selection);
    }

    public void HandleSelection(char selection)
    {
        switch (selection)
        {
            case 'L':
                BankApp.displayMenu = new UserNameMenu();
                break;
            case 'R':
                throw new NotImplementedException();
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


