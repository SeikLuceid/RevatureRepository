package com.revature.assignments.assignment0;

import com.revature.assignments.assignment0.states.MenuStateMachine;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class BankApp
{
    public static final Logger logger = LogManager.getLogger(BankApp.class.getName());

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        MenuStateMachine menu = MenuStateMachine.getInstance();
        System.out.println("Hello!  Welcome to BS Bank.");

        do
        {
            menu.processInputs();
        }
        while(!menu.quitProgram());

        System.out.println("\n\nThank you for banking with us.  Goodbye!");
    }
}

