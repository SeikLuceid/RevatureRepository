package com.revature.assignments.assignment0.states;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class MenuStateMachine
{
    private static MenuStateMachine instance = new MenuStateMachine();
    private static State currentState = new MainMenu();
    static final Logger logger = LogManager.getLogger(MenuStateMachine.class.getName());

    private MenuStateMachine() { }
    public static MenuStateMachine getInstance() { return instance; }


    public boolean quitProgram()
    {
        return currentState.shouldQuitApplication();
    }

    public void processInputs()
    {
        System.out.println();
        try{
            Runtime.getRuntime().exec("cls");
        } catch (IOException e) { }
        System.out.flush();

        currentState.processInputs();
    }

    public void setState(State newState)
    {
        if(newState == currentState)
            return;
        currentState = newState;
    }
}

