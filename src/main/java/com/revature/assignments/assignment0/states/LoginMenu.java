package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.singletons.*;
import com.revature.assignments.assignment0.dataObjects.User;

class LoginMenu implements State
{
    private String username;
    private String password;

    private boolean shouldQuit = false;
    private boolean exitMenu = false;

    public LoginMenu() { System.out.println("Beginning User Login."); }

    public void processInputs()
    {
        getUsername();
        getPassword();
        User user = DatabaseConnect.attemptLogin(username, password);
        if(user != null)
            user.loadMenu();
    }

    private void getUsername()
    {
        boolean invalid = true;
        while (invalid && !exitMenu) {
            System.out.print("Username: ");
            username = Input.getString().toUpperCase();

            if(username.length() <= 0)
            {
                errorOptions("\nInvalid username.\n");
            }
            else
                invalid = false;

            for(int i = 0; i < username.length(); i++)
            {
                if(username.charAt(i) > 90 || username.charAt(i) < 65)
                {
                    errorOptions("\nInvalid username.\n");
                    invalid = true;
                    break;
                }
                else
                    invalid = false;

            }
        }
    }

    private void getPassword() {
        boolean invalid = true;
        while(invalid && !exitMenu)
        {
            System.out.print("\nPassword: ");
            password = Input.getString();
            if (password.length() < 4)
            {
                errorOptions("Your password is too short!  Must be 4 or more characters.");
                invalid = true;
            }
            else
            {
                invalid = false;
            }
        }
    }

    private void errorOptions(String errorMessage) {
        boolean invalid = true;
        while (invalid && !exitMenu)
        {
            System.out.println(errorMessage);
            System.out.println("\nWhat would you like to do?");
            System.out.println("(T)ry again.");
            System.out.println("(R)eturn to Main Menu.");
            System.out.println("(Q)uit the application.");

            char selection = Input.getChar();

            invalid = false;
            switch (selection)
            {
                case 'T':
                    break;
                case 'R':
                    MenuStateMachine.getInstance().setState(new MainMenu());
                    exitMenu = true;
                    break;
                case 'Q':
                    shouldQuit = true;
                    exitMenu = true;
                    break;
                default:
                    invalid = true;
            }
        }
    }
    public boolean quitProgram() {
        return shouldQuit;
    }
}
