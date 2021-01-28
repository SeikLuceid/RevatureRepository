package com.revature.assignments.project0.states;

import com.revature.assignments.project0.dataObjects.*;
import com.revature.assignments.project0.singletons.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CustomerMenu implements State
{
    private final boolean shouldQuit = false;
    private final Customer customer;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private final ArrayList<Transfer> transfers;
    private final boolean hasAccount;
    private boolean hasPendingTransfers;


    public CustomerMenu(Customer customer)
    {
        this.customer = customer;
        System.out.println("\n\nWelcome " + this.customer.toString());
        transfers = DatabaseConnect.getPendingTransfersTo(customer);
        hasAccount = this.customer.getAccounts().size() > 0;
        hasPendingTransfers = transfers.size() > 0;
    }

    public void processInputs()
    {
        String[] options = getOptions();

        for(Account account : customer.getAccounts())
            System.out.println(account.toString());

        char selection = Input.DisplayMultipleChoice(options);

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

    private String[] getOptions()
    {
        int size = 2;
        if(hasAccount) size++;
        if(hasPendingTransfers) size++;

        String[] options = new String[size];

        int counter = 0;

        if(hasAccount)
        {
            options[counter] = "Check Existing Account";
            counter++;
        }

        options[counter] = "New Account";
        counter++;

        if(hasPendingTransfers)
        {
            options[counter] = "Pending Transfers Awaiting Approval";
            counter++;
        }
        options[counter] = "Logout";

        return options;
    }

    private void displayPendingTransfers()
    {
        System.out.println();
        System.out.println();

        for(Transfer transfer : transfers)
        {
            System.out.println(transfer.toString());
        }

        char selection = Input.DisplayMultipleChoice("Approve Transfer", "Reject Transfer", "Back");
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
        }
    }

    private void approveTransfer()
    {
        System.out.println();
        System.out.println();

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
                int selection = Input.getInt("Transfer ID: ");
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

        System.out.println();
        System.out.println();

        System.out.println("Are you sure you wish to approve the following transfers?");
        System.out.println(selectedTransfer.toString());

        char selection = Input.DisplayMultipleChoice("Yes", "No");
        if(selection == 'Y')
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

        hasPendingTransfers = transfers.size() > 0;
        if(hasPendingTransfers)
            displayPendingTransfers();
    }

    private void rejectTransfer()
    {
        System.out.println();
        System.out.println();

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
                int selection = Input.getInt("Transfer ID: ");
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

        System.out.println();
        System.out.println();

        System.out.println("Are you sure you wish to reject the following transfers?");
        System.out.println(selectedTransfer.toString());

        char selection = Input.DisplayMultipleChoice("Yes", "No");
        if(selection == 'Y')
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

        hasPendingTransfers = transfers.size() > 0;
        if(hasPendingTransfers)
            displayPendingTransfers();
    }

    private void createNewAccount()
    {
        boolean invalid = true;
        while(invalid) {
            System.out.println("An initial deposit of at least " + formatter.format(100) + " is required.");
            System.out.print("Initial Deposit: ");
            double deposit = Input.getDouble("Amount: ");
            if(deposit < 0)
            {
                System.out.println("Unable to deposit less than " + formatter.format(0) + ". Please try again.");
                continue;
            }
            if (deposit < 100) {
                System.out.println("Initial Deposit below " + formatter.format(100) + ". Please try again.");
                continue;
            }
            if(deposit > 99999999999.9999)
            {
                System.out.println("Please come into a physical location for transactions of " + formatter.format(100000000000.00) + " or greater.");
                continue;
            }
            BigDecimal bd = new BigDecimal(deposit).setScale(2, RoundingMode.DOWN);
            deposit = bd.doubleValue();

            System.out.println("Confirmation: You would like to apply for a new account with a starting balance of: " + formatter.format(deposit) + ".");

            char selection = Input.DisplayMultipleChoice("Confirm", "Back");
            switch(selection)
            {
                case 'C':
                    Account account = DatabaseConnect.createBankAccount(customer, deposit);
                    if(account != null)
                        System.out.println("Bank Account Pending.  Awaiting employee review.");
                    else
                        System.out.println("Account Creation Failed, please try again later.");
                    invalid = false;
                    break;
                case'B':
                    invalid = false;
                    break;
            }
        }
    }

    private void checkExistingAccount()
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.println();
            System.out.println();
            for(Account account : customer.getAccounts())
                System.out.println(account.toString());
            System.out.println("\nPlease choose an account number. (0) to go back.");
            int selection = Input.getInt("Account Number: ");
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
            System.out.println("Account Not Found");
        }
    }

    public boolean shouldQuitApplication() {
        return shouldQuit;
    }
}
