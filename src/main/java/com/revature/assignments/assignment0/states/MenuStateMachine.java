package com.revature.assignments.assignment0.states;

import java.io.IOException;
import java.util.Scanner;

public class MenuStateMachine
{
    private static MenuStateMachine instance = new MenuStateMachine();;
    private static State currentState = new MainMenu();

    private MenuStateMachine() { }
    public static MenuStateMachine getInstance() { return instance; }


    public boolean quitProgram()
    {
        return currentState.quitProgram();
    }

    public void processInputs(Scanner input)
    {
        System.out.println("\n");
        currentState.processInputs(input);
    }

    void SetState(State newState)
    {
        if(newState == currentState)
            return;
        currentState = newState;
    }
}

