package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.singletons.*;
import com.revature.assignments.assignment0.dataObjects.User;

class RegisterMenu implements State
{
    public void processInputs()
    {
        String username = getUsername();
        String password = getPassword();
        String firstName = getName(PROMPT_FIRSTNAME);
        String lastName = getName(PROMPT_LASTNAME);
        String ssn = getSSN();
        createCustomerAccount(username, password, firstName, lastName, ssn);
    }

    private String getUsername()
    {
        String userName = Input.getAlphanumericStringInUpperCase(PROMPT_USERNAME, ERROR_USERNAME_INVALID);
        if(DatabaseConnect.usernameExists(userName))
        {
            System.out.println(ERROR_USERNAME_EXISTS);
            return getUsername();
        }
        return userName;
    }

    private String getPassword()
    { return Input.getStringOfGreaterThanGivenLength(6, PROMPT_PASSWORD, ERROR_PASSWORD); }

    private String getName(String prompt)
    { return Input.getStringWithNamingConvention(prompt, ERROR_NAME); }

    private String getSSN() {
        String ssnString = Input.getSSNAsString(PROMPT_SSN, ERROR_SSN_INVALID);
        if (DatabaseConnect.ssnExists(ssnString)) {
            System.out.println(ERROR_SSN_EXISTS);
            MenuStateMachine.getInstance().setState(new MainMenu());
            shouldQuit = true;
        }
        return ssnString;
    }

    private void createCustomerAccount(String username, String password, String firstName, String lastName, String ssn)
    {
        if(shouldQuit)
            return;
        User user = DatabaseConnect.createUser(username, password, firstName, lastName, ssn);
        if(user == null)
        {
            System.out.println(ERROR_USER);
            shouldQuit = true;
        }
        else
        {
            System.out.println(SUCCESS_USER);
            user.loadMenu();
        }
    }

    public boolean shouldQuitApplication()
    { return shouldQuit; }

    private boolean shouldQuit = false;
    private static final String PROMPT_FIRSTNAME = "First Name: ";
    private static final String PROMPT_LASTNAME = "Last Name: ";
    private static final String PROMPT_USERNAME = "Username: ";
    private static final String PROMPT_PASSWORD = "Password: ";
    private static final String PROMPT_SSN = "SSN: ";
    private static final String ERROR_USERNAME_INVALID = "Invalid Characters, please use alphanumeric characters only.  Please try again.";
    private static final String ERROR_USERNAME_EXISTS = "That username already exists.  Please try again.";
    private static final String ERROR_PASSWORD = "Password must be 6 or more characters.  Please try again.";
    private static final String ERROR_NAME = "Alphabetic only except ' character.  Please try again.";
    private static final String ERROR_SSN_INVALID = "Invalid SSN! Please try again.";
    private static final String ERROR_SSN_EXISTS = "That SSN already exists as a user.  Please contact customer service.";
    private static final String ERROR_USER = "Unable to Create User at this time.  Please try again later.";
    private static final String SUCCESS_USER = "Account Created!";
}
