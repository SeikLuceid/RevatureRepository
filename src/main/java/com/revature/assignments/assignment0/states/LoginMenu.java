package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.singletons.*;
import com.revature.assignments.assignment0.dataObjects.User;

class LoginMenu implements State
{
    public void processInputs() {
        String username = getUsername();
        String password = getPassword();
        attemptLogin(username, password);
    }

    private void attemptLogin(String username, String password) {
        User user = DatabaseConnect.attemptLogin(username, password);
        if(user == null) {
            System.out.println(ERROR_LOGIN);
            shouldQuit = true;
        }
        else {
            System.out.println(SUCCESSFUL_LOGIN);
            user.loadMenu();
        }
    }

    private String getUsername()
    { return Input.getAlphanumericStringInUpperCase(PROMPT_USERNAME, ERROR_USERNAME); }

    private String getPassword()
    { return Input.getStringOfGreaterThanGivenLength(PASSWORD_MIN_LENGTH, PROMPT_PASSWORD, ERROR_PASSWORD); }

    public boolean shouldQuitApplication()
    { return shouldQuit; }

    private boolean shouldQuit = false;

    private static final int PASSWORD_MIN_LENGTH = 6;;
    private static final String SUCCESSFUL_LOGIN = "Account Created!";
    private static final String PROMPT_PASSWORD = "Password: ";
    private static final String PROMPT_USERNAME = "Username: ";
    private static final String ERROR_USERNAME = "Invalid Username.";
    private static final String ERROR_PASSWORD = "Password must be 6 or more characters.  Please try again.";
    private static final String ERROR_LOGIN = "Unable to Login at this time.  Please try again later.";
}
