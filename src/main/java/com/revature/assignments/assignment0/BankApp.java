package com.revature.assignments.assignment0;

import com.revature.assignments.assignment0.states.MenuStateMachine;
import java.util.Scanner;

public class BankApp
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        MenuStateMachine menu = MenuStateMachine.getInstance();
        System.out.println("Hello!  Welcome to BS Bank.");

        do
        {
            menu.processInputs(input);
        }
        while(!menu.quitProgram());

        System.out.println("Thank you for banking with us.  Goodbye!");
    }
}

