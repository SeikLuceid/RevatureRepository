package com.revature.assignments.assignment0.singletons;

import java.util.Scanner;

public class Input {
    private static final int Z_INT = Character.getNumericValue('Z');
    private static final int A_INT = Character.getNumericValue('a');
    private static final int APOST_INT = Character.getNumericValue('\'');

    private static Scanner scanner = new Scanner(System.in);

    private Input() { }

    public static void SetScanner(Scanner newScanner) {
        scanner = newScanner;
    }

    private static String getScannerInput() { return scanner.nextLine(); }

    private static char getChar()
    {
        String input = getScannerInput();
        if (input.length() > 0)
            return input.toUpperCase().charAt(0);
        else
            return getChar();
    }

    public static char getValidChar(String prompt, String error, char... acceptableValues)
    {
        System.out.println();
        System.out.print(prompt);
        char validChar = getChar();
        if(acceptableValues.length == 0)
            return validChar;
        else {
            for(char valid : acceptableValues)
            {
                if(validChar == valid)
                    return validChar;
            }
            System.out.println(error);
            return getValidChar(prompt, error, acceptableValues);
        }
    }

    public static String getString()
    {
        String input = getScannerInput();
        if(input.length() > 0)
            return input;
        return getString();
    }

    public static String getStringOfGreaterThanGivenLength(int length, String prompt, String error)
    {
        System.out.println();
        System.out.print(prompt);
        String string = getString();
        if(string.length() >= length)
            return string;
        System.out.println(error);
        return getStringOfGreaterThanGivenLength(length, prompt, error);
    }

    public static String getAlphanumericStringInUpperCase(String prompt, String error)
    {
        System.out.println();
        System.out.println(prompt);
        String string = getString().toUpperCase();
        boolean valid = true;
        for(int i = 0; i < string.length(); i++)
        {
            char c = string.charAt(i);
            int v = Character.getNumericValue(c);
            if(v > Z_INT || v < A_INT)
            {
                valid = false;
                break;
            }
        }
        if(valid)
            return string;
        System.out.println(error);
        return getAlphanumericStringInUpperCase(prompt, error);
    }

    public static String getStringWithNamingConvention(String prompt, String error)
    {
        System.out.println();
        System.out.println(prompt);
        String string = getString().toUpperCase();
        boolean valid = true;
        for(int i = 0; i < string.length(); i++)
        {
            char c = string.charAt(i);
            int v = Character.getNumericValue(c);

            if(v > Z_INT || v < A_INT)
            {
                if(v != APOST_INT)
                {
                    valid = false;
                    break;
                }
            }
        }
        if(valid)
            return string;
        System.out.println(error);
        return getStringWithNamingConvention(prompt, error);
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

    public static String getSSNAsString(String s, String s1)
    {
        System.out.print(s);
        String ssn = getString().toUpperCase();

        if(ssn.length() != 9 || ssn.length() != 11)
        {
            boolean valid = true;
            for(int i = 0; i < ssn.length(); i++)
            {
                if(ssn.length() == 11)
                {
                    if(i == 3 || i == 6)
                    {
                        if(ssn.charAt(i) != '-')
                        {
                            valid = false;
                            break;
                        }
                        continue;
                    }
                }

                if(ssn.charAt(i) < 48 || ssn.charAt(i) > 57)
                {
                    valid = false;
                    break;
                }
            }
            if(valid)
                return ssn;
        }
        System.out.println(s1);
        return getSSNAsString(s, s1);
    }
}
