package com.revature.assignments.assignment0.states;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MenuStateMachine
{
    private static MenuStateMachine instance = new MenuStateMachine();
    private static State currentState = new MainMenu();
    static final Logger logger = LogManager.getLogger(MenuStateMachine.class.getName());

    private MenuStateMachine() { }
    public static MenuStateMachine getInstance() { return instance; }


    public boolean quitProgram()
    {
        return currentState.quitProgram();
    }

    public void processInputs()
    {
        System.out.println();
        currentState.processInputs();
    }

    public void setState(State newState)
    {
        if(newState == currentState)
            return;
        currentState = newState;
    }
}

