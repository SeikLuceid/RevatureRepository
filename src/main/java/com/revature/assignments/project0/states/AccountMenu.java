package com.revature.assignments.project0.states;

import com.revature.assignments.project0.dataObjects.*;
import com.revature.assignments.project0.singletons.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

public class AccountMenu implements State
{
    private boolean shouldQuit = false;
    private final Account account;
    private final Customer customer;
    private ArrayList<Transfer> transfers;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public AccountMenu(Account account, Customer customer)
    {
        this.account = account;
        this.customer = customer;
        transfers = DatabaseConnect.getPendingTransfersFrom(account);
    }

    public void processInputs()
    {
        char selection = 'Z';
        if(transfers.size() > 0)
            selection = Input.DisplayMultipleChoice("Deposit", "Withdrawal", "Transfer", "Pending Transfers", "Back");
        else
            selection = Input.DisplayMultipleChoice("Deposit", "Withdrawal", "Transfer", "Back");
        switch(selection)
        {
            case'D':
                depositIntoAccount();
                break;
            case'W':
                withdrawFromAccount();
                break;
            case'T':
                transferFromAccount();
                break;
            case'P':
                if(transfers.size() > 0)
                    reviewTransfers();
                break;
            case 'B':
                MenuStateMachine.getInstance().setState(new CustomerMenu(customer));
                return;
        }
    }

    private void reviewTransfers()
    {
        boolean invalid = true;
        while(invalid)
        {
            for(Transfer transfer : transfers)
            {
                System.out.println(transfer.toString());
            }

            char selection = Input.DisplayMultipleChoice("Cancel Transfer", "Back");
            switch(selection)
            {
                case 'C':
                    deinitializeTransfer();
                    invalid = false;
                case 'B':
                    invalid = false;
                    return;
                default:
                    System.out.println("Please try again.");
            }
        }
    }

    private void deinitializeTransfer()
    {
        Transfer selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            System.out.println(transfer.toString());
        }
        if(transfers.size() > 1) {
            while (selectedTransfer == null) {
                System.out.println("Which transfer would you like to cancel? (0) to go back.");
                System.out.print("Transfer ID: ");
                int selection = Input.getInt();
                if (selection == 0)
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

        System.out.println("Are you sure you wish to cancel the following transfers?");
        System.out.println(selectedTransfer.toString());
        System.out.println("(Y)es.");
        System.out.println("(N)o.");
        char select = Input.getValidChar("Selection: ", "Invalid Entry!  Please try again.", 'Y', 'N');
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

        if(transfers.size() > 0)
            reviewTransfers();
    }

    private void displayOptions()
    {
        System.out.println(account.toString());
        System.out.println("Please Enter One of the Following Options:");
        System.out.println("(D)eposit");
        System.out.println("(W)ithdrawal.");
        System.out.println("(T)ransfer");
        if(transfers.size() > 0)
            System.out.println("(P)ending Transfers.");
        System.out.println("(B)ack.");
        char selection = Input.DisplayMultipleChoice("Deposit", "Withdrawal", "Transfer", "Back");
    }

    private void depositIntoAccount()
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.println("How much did you wish to deposit? (0) for back.");
            double amount = Input.getDouble();
            if(amount == 0)
            {
                break;
            }
            if(amount < 0)
            {
                System.out.println("You can not deposit an amount less than 0.");
                continue;
            }
            if(amount > 99999999999.9999)
            {
                System.out.println("Please come into a physical location for transactions of 100,000,000,000.00 or greater.");
                continue;
            }

            BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            amount = bd.doubleValue();

            System.out.println(formatter.format(amount) + " - Is this amount correct?");
            System.out.println("(Y)es");
            System.out.println("(N)o");
            char selection = Input.getValidChar("Selection: ", "Invalid Entry!  Please try again.", 'Y', 'N');
            if(selection == 'Y')
            {
                if (!adjustBalanceOfAccount(account.getBalance() + amount, "Deposit Successful."))
                    System.out.println("Withdrawal Failed.  Please try again later.");
                break;
            }
            else
            {
                System.out.println("Please try again.");
            }
        }
    }

    public void withdrawFromAccount()
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.println("How much did you wish to withdraw? (0) for back.");
            double amount = Input.getDouble();
            if(amount == 0)
            {
                break;
            }
            if(amount <= 0)
            {
                System.out.println("You can not withdraw an amount less than 0.");
                continue;
            }
            if(amount > 99999999999.9999)
            {
                System.out.println("Please come into a physical location for transactions of " + formatter.format(100000000000.00) + " or greater.");
                continue;
            }

            BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            amount = bd.doubleValue();

            if(amount > account.getBalance())
            {
                System.out.println("You can not withdraw an amount greater than your balance.");
                continue;
            }
            System.out.println(formatter.format(amount) + " - Is this amount correct?");
            System.out.println("(Y)es");
            System.out.println("(N)o");
            char selection = Input.getValidChar("Selection: ", "Invalid Entry!  Please try again.", 'Y', 'N');
            if(selection == 'Y')
            {
                if (!adjustBalanceOfAccount(account.getBalance() - amount, "Withdrawal Successful."))
                    System.out.println("Withdrawal Failed.  Please try again later.");
                break;
            }
        }
    }

    private boolean adjustBalanceOfAccount(double balance, String s) {
        Account updatedAccount = DatabaseConnect.adjustBalanceToAccount(balance, account.getAccountNumber(), customer.getId());
        if (updatedAccount != null) {
            account.UpdateAccount(updatedAccount);
            System.out.println(s);
            System.out.println("Your new account balance is : " + account.getBalanceAsString());
            return true;
        }
        return false;
    }

    private void transferFromAccount()
    {
        boolean invalid = true;
        while(invalid)
        {
            System.out.println("What is the destination account number? (0) for back.");
            int destination = Input.getInt();

            if(destination == 0)
                return;
            if(destination == account.getAccountNumber())
            {
                System.out.println("Can't transfer to and from the same account.");
                return;
            }
            if(!DatabaseConnect.checkValidAccount(destination))
            {
                System.out.println("Account Doesn't Exist, or Network Connection Error");
                System.out.println("Check Account Number, or try again later.");
                continue;
            }

            System.out.println();
            System.out.println();

            System.out.println("How much did you want to transfer? (0) for back.");
            double amount = Input.getDouble();
            if(amount == 0)
            {
                break;
            }
            if(amount < 0)
            {
                System.out.println("You can not deposit an amount less than 0.");
                continue;
            }
            if(amount > 99999999999.9999)
            {
                System.out.println("Please come into a physical location for transactions of " + formatter.format(100000000000.00) + " or greater.");
                continue;
            }
            if(amount > account.getBalance())
            {
                System.out.println("You can not transfer an amount greater than your balance.");
                continue;
            }

            BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            amount = bd.doubleValue();

            System.out.println();
            System.out.println();

            System.out.println(formatter.format(amount) + " - Is this amount correct?");
            System.out.println("[Y]es");
            System.out.println("[N]o");
            char selection = Input.getValidChar("Selection: ", "Invalid Entry!  Please try again.", 'Y', 'N');
            if(selection == 'Y')
            {
                if(DatabaseConnect.initiateTransfer(account.getAccountNumber(), destination, amount))
                {
                    transfers = DatabaseConnect.getPendingTransfersFrom(account);
                    System.out.println("Transfer Initiated - Awaiting Confirmation");
                }
                else
                    System.out.println("Transfer Failed - Please try again later.");
                break;
            }
        }
    }

    private void errorOptions(String errorMessage)
    {
            System.out.println(errorMessage);
            System.out.println("\nWhat would you like to do?");
            System.out.println("(T)ry again.");
            System.out.println("(R)eturn to Main Menu.");
            System.out.println("(Q)uit the application.");

            char selection = Input.getValidChar("Selection: ", "Invalid Entry!  Please try again.", 'T', 'R', 'Q');

            switch (selection)
            {
                case 'T':
                    break;
                case 'R':
                    MenuStateMachine.getInstance().setState(new MainMenu());
                    break;
                case 'Q':
                    shouldQuit = true;
                    break;
            }
    }

    public boolean shouldQuitApplication() {
        return shouldQuit;
    }
}
