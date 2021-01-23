package com.revature.assignments.assignment0.states;

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

    public void processInputs(Scanner input)
    {
        getUsername(input);
        getPassword(input);
        getFirstName(input);
        getLastName(input);
        getSSN(input);
        if(DatabaseConnect.createUser(username, password, firstName, lastName, ssn) == false)
        {
            System.out.println("Unable to Create User at this time.  Please try again later.");
            MenuStateMachine.getInstance().SetState(new MainMenu());
        }
    }

    private void getUsername(Scanner input)
    {
        boolean invalid = true;
        while (invalid) {
            System.out.print("Username: ");
            username = input.nextLine().toUpperCase(Locale.ROOT);
            if(DatabaseConnect.usernameExists(username))
            {
                System.out.println("That username already exists.  Please try again.");
            }
            else
                invalid = false;
        }
    }

    private void getPassword(Scanner input) {
        boolean invalid = true;
        while(invalid)
        {
            System.out.print("\nPassword: ");
            password = input.nextLine();
            if (password.length() < 4)
            {
                System.out.println("Your password is too short!  Must be 4 or more characters.");
            }
            else
            {
                invalid = false;
            }
        }
    }

    private void getFirstName(Scanner input)
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.print("First Name: ");
            firstName = input.nextLine().toUpperCase(Locale.ROOT);
            if (firstName.length() < 1)
            {
                System.out.println("You must enter a valid name.  Please try again.");
            }
            else
                invalid = false;
        }
    }

    private void getLastName(Scanner input)
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.print("First Name: ");
            lastName = input.nextLine().toUpperCase(Locale.ROOT);
            if (lastName.length() < 1)
            {
                System.out.println("You must enter a valid name.  Please try again.");
            }
            else
                invalid = false;
        }
    }

    private void getSSN(Scanner input)
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.print("SSN (###-##-####): ");
            ssn = input.nextLine().toUpperCase(Locale.ROOT);
            int count = 0;
            for(int i = 0; i < ssn.length(); i++)
            {
                count++;
                if(i == 3 || i == 6)
                {
                    if(ssn.charAt(i) == '-')
                        invalid = false;
                    else
                    {
                        System.out.println("Incorrect Format!  ###-##-#### is the correct format. Please try again.");
                        invalid = true;
                        break;
                    }
                }
                else if(ssn.charAt(i) < 48 || ssn.charAt(i) > 57)
                {
                    System.out.println("Incorrect Format!  ###-##-#### is the correct format. Please try again.");
                    invalid = true;
                    break;
                }
                else
                {
                    invalid = false;
                }
            }
            if(count != 11)
                invalid = true;
        }
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
