package com.revature.assignments.assignment0.states;

import java.util.Scanner;

interface State
{
    public void processInputs(Scanner input);
    public boolean quitProgram();
}

