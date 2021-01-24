package com.revature.assignments.assignment0.states;

import com.revature.assignments.assignment0.*;
import com.revature.databases.DatabaseConnect;

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
        System.out.println("Transfers: " + transfers.size());
        displayOptions();
        char selection = Input.getChar();
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
                MenuStateMachine.getInstance().SetState(new CustomerMenu(customer));
                return;
            default:
                System.out.println("Invalid Entry!  Please try again.");
                break;
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
            System.out.println("What would you like to do?");
            System.out.println("(C)ancel Transfer.");
            System.out.println("(B)ack.");
            char selection = Input.getChar();
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

        if(transfers.size() > 0)
            reviewTransfers();
    }

    private void displayOptions()
    {
        System.out.println("\n" + account.toString());
        System.out.println("Please Enter One of the Following Options:");
        System.out.println("(D)eposit");
        System.out.println("(W)ithdrawal.");
        System.out.println("(T)ransfer");
        if(transfers.size() > 0)
            System.out.println("(P)ending Transfers.");
        System.out.println("(B)ack.");
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

            BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            amount = bd.doubleValue();

            System.out.println(formatter.format(amount) + " - Is this amount correct?");
            System.out.println("(Y)es");
            System.out.println("(N)o");
            char selection = Input.getChar();
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

    private void withdrawFromAccount()
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
            char selection = Input.getChar();
            if(selection == 'Y')
            {
                if (!adjustBalanceOfAccount(account.getBalance() - amount, "Withdrawal Successful."))
                    System.out.println("Withdrawal Failed.  Please try again later.");
                break;

            }
            else
            {
                System.out.println("Please try again.");
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
            System.out.println("What is the destination account number?");
            int destination = Input.getInt();
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
            if(amount > account.getBalance())
            {
                System.out.println("You can not transfer an amount greater than your balance.");
                continue;
            }

            BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            amount = bd.doubleValue();

            System.out.println(formatter.format(amount) + " - Is this amount correct?");
            System.out.println("(Y)es");
            System.out.println("(N)o");
            char selection = Input.getChar();
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
            else
                System.out.println("Please try again.");
        }
    }

    private void errorOptions(String errorMessage) {
        boolean invalid = true;
        while (invalid) {
            System.out.println(errorMessage);
            System.out.println("\nWhat would you like to do?");
            System.out.println("(T)ry again.");
            System.out.println("(R)eturn to Main Menu.");
            System.out.println("(Q)uit the application.");

            char selection = Input.getChar();

            invalid = true;
            switch (selection) {
                case 'T':
                    break;
                case 'R':
                    MenuStateMachine.getInstance().SetState(new MainMenu());
                    break;
                case 'Q':
                    shouldQuit = true;
                    break;
                default:
                    System.out.println("Invalid Entry, please try again.");
                    invalid = false;
                    break;
            }
        }
    }

    public boolean quitProgram() {
        return shouldQuit;
    }
}
