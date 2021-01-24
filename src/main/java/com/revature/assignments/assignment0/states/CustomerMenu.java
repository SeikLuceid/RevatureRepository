package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.dataObjects.*;
import com.revature.assignments.assignment0.singletons.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CustomerMenu implements State
{
    private boolean shouldQuit = false;
    private final Customer customer;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private ArrayList<Transfer> transfers;


    public CustomerMenu(Customer customer)
    {
        this.customer = customer;
        System.out.println("\n\nWelcome " + this.customer.toString());
        transfers = DatabaseConnect.getPendingTransfersTo(customer);
    }

    public void processInputs()
    {
        for(Account account : customer.getAccounts())
            System.out.println(account.toString());

        System.out.println("\nWhat would you like to do?");
        System.out.println("(C)heck Existing Account.");
        System.out.println("(N)ew Account.");
        if(transfers.size() > 0)
            System.out.println("(P)ending Transfers Awaiting Approval.");
        System.out.println("(L)ogout.");

        char selection = Input.getChar();
        switch(selection)
        {
            case 'C':
                checkExistingAccount();
                break;
            case 'N':
                createNewAccount();
                break;
            case 'P':
                displayPendingTransfers();
                break;
            case 'L':
                MenuStateMachine.getInstance().setState(new MainMenu());
                break;
        }
    }

    private void displayPendingTransfers()
    {
        boolean invalid = true;
        while(invalid)
        {
            for(Transfer transfer : transfers)
            {
                System.out.println(transfer.toString());
            }
            System.out.println("What would you like to do?");
            System.out.println("(A)pprove Transfer.");
            System.out.println("(R)eject Transfer.");
            System.out.println("(B)ack.");
            char selection = Input.getChar();
            invalid = false;
            switch(selection)
            {
                case 'A':
                    approveTransfer();
                    break;
                case 'R':
                    rejectTransfer();
                    break;
                case 'B':
                    return;
                default:
                    invalid = true;
                    System.out.println("Please try again.");
            }
        }
    }

    private void approveTransfer()
    {
        Transfer selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            System.out.println(transfer.toString());
        }
        if(transfers.size() > 1)
        {
            while(selectedTransfer == null)
            {
                System.out.println("Which transfer would you like to approve? (0) to go back.");
                System.out.print("Transfer ID: ");
                int selection = Input.getInt();
                if(selection == 0)
                    return;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferID() == selection) {
                        selectedTransfer = transfer;
                        break;
                    }
                }
                if (selectedTransfer == null) {
                    System.out.println("No transfer found by that ID.  Please try again.");
                }
            }
        }
        else
        {
            selectedTransfer = transfers.get(0);
        }

        System.out.println("Are you sure you wish to approve the following transfers?");
        System.out.println(selectedTransfer.toString());
        System.out.println("(Y)es.");
        System.out.println("(N)o.");
        char select = Input.getChar();
        if(select == 'Y')
        {
            if(DatabaseConnect.applyTransfer(selectedTransfer))
            {
                transfers.remove(selectedTransfer);
                System.out.println("Transfer Completed.");
            }
            else{
                System.out.println("Transfer was not Completed.  Please try again later.");
                return;
            }
        }
        customer.forceAccountUpdate();

        if(transfers.size() > 0)
            displayPendingTransfers();
    }

    private void rejectTransfer()
    {
        Transfer selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            System.out.println(transfer.toString());
        }
        if(transfers.size() > 1)
        {
            while(selectedTransfer == null)
            {
                System.out.println("Which transfer would you like to cancel? (0) to go back.");
                System.out.print("Transfer ID: ");
                int selection = Input.getInt();
                if(selection == 0)
                    return;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferID() == selection) {
                        selectedTransfer = transfer;
                        break;
                    }
                }
                if (selectedTransfer == null) {
                    System.out.println("No transfer found by that ID.  Please try again.");
                }
            }
        }
        else if(transfers.size() == 1)
        {
            selectedTransfer = transfers.get(0);
        }
        else {
            return;
        }

        System.out.println("Are you sure you wish to reject the following transfers?");
        System.out.println(selectedTransfer.toString());
        System.out.println("(Y)es.");
        System.out.println("(N)o.");
        char select = Input.getChar();
        if(select == 'Y')
        {
            if(DatabaseConnect.cancelTransfer(selectedTransfer))
            {
                transfers.remove(selectedTransfer);
                System.out.println("Transfer Cancelled.");
            }
            else{
                System.out.println("Transfer was not Cancelled.  Please try again later.");
                return;
            }
        }
        customer.forceAccountUpdate();

        if(transfers.size() > 0)
            displayPendingTransfers();
    }

    private void createNewAccount()
    {
        boolean invalid = true;
        while(invalid) {
            System.out.println("An initial deposit of at least " + formatter.format(100) + " is required.");
            System.out.print("Initial Deposit: ");
            double deposit = Input.getDouble();
            if(deposit < 0)
            {
                System.out.println("Unable to deposit less than " + formatter.format(0) + ". Please try again.");
            }
            if (deposit < 100) {
                System.out.println("Initial Deposit below " + formatter.format(100) + ". Please try again.");
                continue;
            }
            BigDecimal bd = new BigDecimal(deposit).setScale(2, RoundingMode.DOWN);
            deposit = bd.doubleValue();

            System.out.println("Confirmation: You would like to apply for a new account with a starting balance of: " + formatter.format(deposit) + ".");
            System.out.println("(C)onfirm.");
            System.out.println("(B)ack.");

            char selection = Input.getChar();
            switch(selection)
            {
                case 'C':
                    if(DatabaseConnect.createBankAccount(customer, deposit))
                        System.out.println("Bank Account Pending.  Awaiting employee review.");
                    else
                        System.out.println("Account Creation Failed, please try again later.");
                    invalid = false;
                    break;
                case'B':
                    invalid = false;
                    break;
                default:
                    System.out.println("Invalid Entry.  Please try again.\n");
                    break;
            }
        }
    }

    private void checkExistingAccount()
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.println("\nPlease enter your account number. (0) to go back.");
            int selection = Input.getInt();
            if(selection == 0)
                return;
            for(Account account : customer.getAccounts())
            {
                if(account.getAccountNumber() == selection)
                {
                    MenuStateMachine.getInstance().setState(new AccountMenu(account, customer));
                    invalid = false;
                    return;
                }
            }

            customer.addAccount(selection);

            for(Account account : customer.getAccounts())
            {
                if(account.getAccountNumber() == selection)
                {
                    MenuStateMachine.getInstance().setState(new AccountMenu(account, customer));
                    invalid = false;
                    return;
                }
            }
            errorOptions("Account Not Found.");
        }
    }

    private void errorOptions(String errorMessage)
    {
        boolean invalid = true;
        while (invalid) {
            System.out.println(errorMessage);
            System.out.println("What would you like to do?");
            System.out.println("(T)ry again.");
            System.out.println("(R)eturn to Main Menu.");
            System.out.println("(Q)uit the application.");

            char selection = Input.getChar();
            System.out.println(selection);

            invalid = false;
            switch (selection) {
                case 'T':
                    break;
                case 'R':
                    MenuStateMachine.getInstance().setState(new MainMenu());
                    break;
                case 'Q':
                    shouldQuit = true;
                    break;
                default:
                    System.out.println("Invalid Entry, please try again.");
                    invalid = true;
                    break;
            }
        }
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
