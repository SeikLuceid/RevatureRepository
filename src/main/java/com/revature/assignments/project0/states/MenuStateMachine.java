package com.revature.assignments.project0.states;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class MenuStateMachine
{
    private static final MenuStateMachine instance = new MenuStateMachine();
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
        currentState.processInputs();
    }

    public void setState(State newState)
    {
        if(newState == currentState)
            return;
        currentState = newState;

        System.out.println();
        System.out.println();
    }
}

