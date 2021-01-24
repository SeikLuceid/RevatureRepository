package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.Input;
import com.revature.assignments.assignment0.User;
import com.revature.databases.DatabaseConnect;

import java.util.Locale;
import java.util.Scanner;

class RegisterState implements State
{
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String ssn;

    private boolean shouldQuit = false;
    private boolean exitMenu = false;

    public RegisterState() { System.out.println("Beginning User Registration.");}

    public void processInputs()
    {
        exitMenu = false;
        getUsername();
        getPassword();
        getFirstName();
        getLastName();
        getSSN();
        createCustomerAccount();
    }

    private void createCustomerAccount()
    {
        if(exitMenu)
            return;
        User user = DatabaseConnect.createUser(username, password, firstName, lastName, ssn);
        if(user == null)
        {
            System.out.println("Unable to Create User at this time.  Please try again later.");
            MenuStateMachine.getInstance().SetState(new MainMenu());
            exitMenu = true;
        }
        else
        {
            System.out.println("Account Created!");
            user.loadMenu();
        }
    }

    private void getUsername()
    {
        boolean invalid = true;
        while (invalid && !exitMenu) {
            System.out.print("Username: ");
            username = Input.getString().toUpperCase();
            if(DatabaseConnect.usernameExists(username))
            {
                errorOptions("That username already exists.  Please try again.");
            }
            else
                invalid = false;
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

    private void getFirstName()
    {
        boolean invalid = true;
        while(invalid && !exitMenu)
        {
            System.out.print("\nFirst Name: ");
            firstName = Input.getString().toUpperCase();
            if (firstName.length() < 1)
            {
                errorOptions("You must enter a valid name.  Please try again.");
            }
            else
                invalid = false;
        }
    }

    private void getLastName()
    {
        boolean invalid = true;
        while(invalid && !exitMenu)
        {
            System.out.print("\nLast Name: ");
            lastName = Input.getString().toUpperCase();
            if (lastName.length() < 1)
            {
                errorOptions("You must enter a valid name.  Please try again.");
            }
            else
                invalid = false;
        }
    }

    private void getSSN()
    {
        boolean invalid = true;
        while(invalid && !exitMenu)
        {
            System.out.print("\nSSN (###-##-####): ");
            ssn = Input.getString().toUpperCase();
            int count = 0;
            for(int i = 0; i < ssn.length(); i++)
            {
                if(ssn.length() != 11)
                {
                    errorOptions("Incorrect Format!  ###-##-#### is the correct format. Please try again.");
                    invalid = true;
                    break;
                }

                if(i == 3 || i == 6)
                {
                    if(ssn.charAt(i) == '-')
                        invalid = false;
                    else
                    {
                        errorOptions("Incorrect Format!  ###-##-#### is the correct format. Please try again.");
                        invalid = true;
                        break;
                    }
                }
                else if(ssn.charAt(i) < 48 || ssn.charAt(i) > 57)
                {
                    errorOptions("Incorrect Format!  ###-##-#### is the correct format. Please try again.");
                    invalid = true;
                    break;
                }
                else
                {
                    invalid = false;
                }
            }

            if(DatabaseConnect.ssnExists(ssn))
            {
                System.out.println("That SSN already exists as a user.  Please contact customer service.");
                MenuStateMachine.getInstance().SetState(new MainMenu());
                exitMenu = true;
            }
        }

    }

    private void errorOptions(String errorMessage)
    {
        boolean invalid = true;
        while(invalid && !exitMenu)
        {
            System.out.println(errorMessage);
            System.out.println("\nWhat would you like to do?");
            System.out.println("(T)ry again.");
            System.out.println("(R)eturn to Main Menu.");
            System.out.println("(Q)uit the application.");
            char selection = Input.getString().toUpperCase(Locale.ROOT).charAt(0);
            switch(selection)
            {
                case 'T':
                    invalid = false;
                    break;
                case 'R':
                    MenuStateMachine.getInstance().SetState(new MainMenu());
                    invalid = false;
                    exitMenu = true;
                    break;
                case 'Q':
                    shouldQuit = true;
                    exitMenu = true;
                    break;
            }
        }


    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
