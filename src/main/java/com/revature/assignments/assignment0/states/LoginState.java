package com.revature.assignments.assignment0.states;

import java.util.Scanner;

class LoginState implements State
{
    private boolean shouldQuit = false;

    public void processInputs(Scanner input)
    {

    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
