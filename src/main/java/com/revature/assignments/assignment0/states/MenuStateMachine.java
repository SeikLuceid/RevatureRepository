package com.revature.assignments.assignment0.states;

public class MenuStateMachine
{
    private static MenuStateMachine instance = new MenuStateMachine();
    private static State currentState = new MainMenu();

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

    public void SetState(State newState)
    {
        if(newState == currentState)
            return;
        currentState = newState;
    }
}

