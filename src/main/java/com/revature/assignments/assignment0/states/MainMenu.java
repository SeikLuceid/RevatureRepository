package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.singletons.Input;

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
            MenuStateMachine.getInstance().setState(new LoginMenu());
        else if(selection == 'R')
            MenuStateMachine.getInstance().setState(new RegisterMenu());
        else if(selection == 'Q')
            shouldQuit = true;
        else
            System.out.println("Invalid Input. Please try again.");
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}

