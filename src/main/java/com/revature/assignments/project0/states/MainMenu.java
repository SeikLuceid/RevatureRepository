package com.revature.assignments.project0.states;

import com.revature.assignments.project0.singletons.Input;


class MainMenu implements State
{
    private boolean shouldQuit = false;

    public void processInputs()
    {
        char selection = Input.DisplayMultipleChoice("Login", "Register", "Quit");
        if(selection == 'Q')
            shouldQuit = true;
        else if(selection == 'L')
            MenuStateMachine.getInstance().setState(new LoginMenu());
        else if(selection == 'R')
            MenuStateMachine.getInstance().setState(new RegisterMenu());
    }

    public boolean shouldQuitApplication() {
        return shouldQuit;
    }
}