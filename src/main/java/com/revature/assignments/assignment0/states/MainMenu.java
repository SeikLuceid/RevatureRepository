package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.singletons.Input;

class MainMenu implements State
{
    private boolean shouldQuit = false;

    public void processInputs()
    {
        DisplayOptions();
        char selection = Input.getValidChar("Selection: ", "Invalid Input. Please try again.", 'L', 'R', 'Q');
        if(selection == 'Z')
            return;
        else if(selection == 'L')
            MenuStateMachine.getInstance().setState(new LoginMenu());
        else if(selection == 'R')
            MenuStateMachine.getInstance().setState(new RegisterMenu());
        else
            System.out.println("Invalid Input. Please try again.");
    }

    private void DisplayOptions() {
        System.out.println("Please Enter One of the Following Options:");
        System.out.println("(L)ogin.");
        System.out.println("(R)egister.");
        System.out.println("(Q)uit.");
    }

    public boolean shouldQuitApplication() {
        return shouldQuit;
    }
}

class UserInteractionManager
{
    public void DisplayOptions()
    {
        DisplayMultipleChoice("Login", "Register", "Quit");
    }

    public void DisplayMultipleChoice(String... options)
    {
        char[] validSelections = new char[options.length];
        String[] displayOptions = new String[options.length];
        for(int i = 0; i < options.length; i++)
        {
            char c = options[i].charAt(0);

            StringBuilder string = new StringBuilder(options[i]);
            string.insert(0, '[');
            string.insert(2, ']');
            System.out.println(string);
            validSelections[i] = c;
        }
    }
}

