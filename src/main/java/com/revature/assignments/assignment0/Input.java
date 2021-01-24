package com.revature.assignments.assignment0;

import java.util.Scanner;

public class Input
{
    private static final Scanner scanner = new Scanner(System.in);

    private Input() { }

    private static String getScannerInput()
    {
        scanner.hasNextLine();
        return scanner.nextLine();
    }

    public static char getChar()
    {
        String input = getScannerInput();
        if(input.length() > 0)
            return input.toUpperCase().charAt(0);
        else
            return getChar();
    }

    public static String getString()
    {
        String input = getScannerInput();
        if(input.length() > 0)
            return input;
        else
            return getString();
    }

    public static int getInt()
    {
        String input = getScannerInput();
        if(input.length() > 0)
        {
            try {
                return Integer.parseInt(input);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid Entry.  Please try again.");
                return getInt();
            }
        }
        else
            return getInt();
    }

    public static double getDouble()
    {
        String input = getScannerInput();
        if(input.length() > 0)
        {
            try {
                return Double.parseDouble(input);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid Entry.  Please try again.");
                return getDouble();
            }
        }
        else
            return getDouble();
    }
}
