package com.revature.assignments.assignment0;

import com.revature.assignments.assignment0.states.MenuStateMachine;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class BankApp
{
    public static final Logger logger = LogManager.getLogger(BankApp.class);
    public static void main(String[] args)
    {
        logger.trace("*** Application Started ***");
        Scanner input = new Scanner(System.in);
        MenuStateMachine menu = MenuStateMachine.getInstance();
        System.out.println("Hello!  Welcome to BS Bank.");

        logger.trace("Test Log - trace");
        logger.debug("Test Log - debug");
        logger.info("Test Log - info");
        logger.warn("Test Log - warn");
        logger.error("Test Log - error");
        logger.fatal("Test Log - fatal");

        do
        {
            menu.processInputs();
        }
        while(!menu.quitProgram());

        System.out.println("\n\nThank you for banking with us.  Goodbye!");
        logger.trace("*** Application Finished ***");
    }
}

