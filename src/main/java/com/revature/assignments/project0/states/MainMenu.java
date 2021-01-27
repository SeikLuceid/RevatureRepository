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


    public char DisplayMultipleChoice(String... options)
    {
        char[] validSelections = new char[options.length];
        String[] displayOptions = new String[options.length];
        for(int i = 0; i < options.length; i++)
        {
            char c = options[i].charAt(0);

            StringBuilder sb = new StringBuilder(options[i]);
            sb.insert(0, '[');
            sb.insert(2, ']');
            displayOptions[i] = sb.toString();
            validSelections[i] = c;
        }

        System.out.println("Please Enter One of the Following Options:");
        for(String option : displayOptions)
            System.out.println(option);

        return Input.getValidChar("Option: ", "Invalid Entry, please try again.", validSelections);
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