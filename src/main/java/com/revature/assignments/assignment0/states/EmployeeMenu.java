package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.dataObjects.Account;
import com.revature.assignments.assignment0.dataObjects.Employee;
import com.revature.assignments.assignment0.dataObjects.Transaction;
import com.revature.assignments.assignment0.singletons.*;


import java.util.ArrayList;

public class EmployeeMenu implements State
{
    private boolean shouldQuit = false;
    private Employee employee;
    private ArrayList<Account> pendingAccounts;

    public EmployeeMenu(Employee employee)
    {
        this.employee = employee;
        System.out.println("\n\nWelcome " + this.employee.toString());
        pendingAccounts = DatabaseConnect.getAllPendingAccounts(employee);
    }

    public void processInputs()
    {
        System.out.println("What would you like to view?");
        if(pendingAccounts.size() > 0)
            System.out.println("(P)ending Bank Accounts.");
        System.out.println("(A)ccounts by Customer ID.");
        System.out.println("(T)ransaction Log.");
        System.out.println("(Q)uit.");

        char selection = Input.getChar();

        switch(selection)
        {
            case 'A':
                viewCustomerAccountByID();
                break;
            case 'P':
                viewPendingAccounts();
                break;
            case 'T':
                viewTransactionLog();
                break;
            case 'Q':
                shouldQuit = true;
                break;
        }
    }

    private void viewCustomerAccountByID()
    {
        int id = -1;
        while(id != 0)
        {
            System.out.println("Please enter the ID of the customer. (0) for back.");
            System.out.print("Customer ID: ");
            id = Input.getInt();
            System.out.println(id);
            if(id < 0)
            {
                System.out.println("ID is greater than 0. Please try again.");
                continue;
            }
            if(id >= 1)
            {
                ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(id);
                if(accounts.size() <= 0)
                {
                    System.out.println("No accounts for ID: " + id);
                    return;
                }
                System.out.println("Accounts ( Customer: " + id + " )");
                for(Account account : accounts)
                    System.out.println(account.toString());
                char selection = 'Z';
                while(selection != 'B' && selection != 'V')
                {
                    System.out.println("What would you like to do?");
                    System.out.println("(V)iew different customer's accounts.");
                    System.out.println("(B)ack.");

                    selection = Input.getChar();
                    if(selection == 'B')
                        id = 0;
                }
            }
        }

    }

    private void viewPendingAccounts()
    {
        char selection = 'Z';
        while(selection != 'B')
        {
            System.out.println("PENDING TRANSFERS:");
            for(Account account : pendingAccounts)
            {
                System.out.println(account.toString());
            }

            System.out.println("What would you like to do?");
            System.out.println("(A)pprove specific account.");
            System.out.println("(R)eject specific account.");
            System.out.println("(B)ack.");
            selection = Input.getChar();
            switch(selection)
            {
                case 'A':
                    approveAccount();
                    if(pendingAccounts.size() <= 0)
                        selection = 'B';
                    break;
                case 'R':
                    rejectAccount();
                    if(pendingAccounts.size() <= 0)
                        selection = 'B';
                    break;
            }
        }
    }

    private void rejectAccount()
    {
        int id = -1;
        while(id != 0)
        {
            for(Account account : pendingAccounts)
                System.out.println(account.toString());
            System.out.println("What account would you like to reject? (0) for back.");
            System.out.print("Account Number: ");
            id = Input.getInt();
            if(id == 0)
                continue;
            else if(id < 0)
            {
                System.out.println("Invalid Account Number.  Please try again.");
                continue;
            }
            else {
                Account account = null;
                for(Account targetAccount : pendingAccounts)
                {
                    if(targetAccount.getAccountNumber() == id)
                        account = targetAccount;
                }
                if(account == null)
                {
                    System.out.println("Account Number not found.");
                }

                char selection = 'Z';
                while(selection != 'N')
                {
                    System.out.println(account.toString());
                    System.out.println("Reject this account?");
                    System.out.println("(Y)es.");
                    System.out.println("(N)o.");
                    System.out.println("(B)ack.");
                    selection = Input.getChar();
                    switch(selection) {
                        case 'Y':
                            if(DatabaseConnect.removePendingAccount(account))
                                pendingAccounts.remove(account);
                            else
                                System.out.println("Unable to reject account at this time. Please try again later.");
                            break;
                        case 'N':
                            id = -1;
                            break;
                        case 'B':
                            selection = 'N';
                            id = 0;
                            break;
                    }
                }
            }
        }
    }

    private void approveAccount()
    {

        int id = -1;
        while(id != 0)
        {
            for(Account account : pendingAccounts)
                System.out.println(account.toString());
            System.out.println("What account would you like to approve? (0) for back.");
            System.out.print("Account Number: ");
            id = Input.getInt();
            if(id == 0)
                continue;
            else if(id < 0)
            {
                System.out.println("Invalid Account Number.  Please try again.");
                continue;
            }
            else {
                Account account = null;
                for(Account targetAccount : pendingAccounts)
                {
                    if(targetAccount.getAccountNumber() == id)
                        account = targetAccount;
                }
                if(account == null)
                {
                    System.out.println("Account Number not found.");
                }

                char selection = 'Z';
                while(selection != 'N')
                {
                    System.out.println(account.toString());
                    System.out.println("Approve this account?");
                    System.out.println("(Y)es.");
                    System.out.println("(N)o.");
                    System.out.println("(B)ack.");
                    selection = Input.getChar();
                    switch(selection) {
                        case 'Y':
                            if(DatabaseConnect.approvePendingAccount(account))
                                pendingAccounts.remove(account);
                            else
                                System.out.println("Unable to approve account at this time. Please try again later.");
                            selection = 'N';
                            if(pendingAccounts.size() <= 0)
                                return;
                            break;
                        case 'N':
                            id = -1;
                            break;
                        case 'B':
                            return;
                    }
                }
            }
        }
    }

    private void viewTransactionLog()
    {
        ArrayList<Transaction> transactions = DatabaseConnect.getTransactions();
        System.out.println("\nTransaction Log:");
        for(Transaction transaction : transactions)
            System.out.println(transaction.toString());
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
