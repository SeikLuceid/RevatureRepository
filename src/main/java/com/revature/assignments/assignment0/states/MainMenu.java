package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.Input;

import java.util.Locale;
import java.util.Scanner;

class MainMenu implements State
{
    private boolean shouldQuit = false;

    public void processInputs()
    {
        System.out.println("Please Enter One of the Following Options:");
        System.out.println("(L)ogin.");
        System.out.println("(R)egister.");
        System.out.println("(Q)uit.");

        char selection = Input.getChar();
        if(selection == 'L')
            MenuStateMachine.getInstance().SetState(new LoginState());
        else if(selection == 'R')
            MenuStateMachine.getInstance().SetState(new RegisterState());
        else if(selection == 'Q')
            shouldQuit = true;
        else
            System.out.println("Invalid Input. Please try again.");
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}

class EmptyState implements State
{
    private boolean shouldQuit = false;

    public void processInputs()
    {

    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}

