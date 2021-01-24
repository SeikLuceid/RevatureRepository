package com.revature.assignments.assignment0;

import java.text.NumberFormat;

public class Account
{
    private final int accountNumber;
    private double balance;

    public Account(int accountNumber, double balance)
    {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return "Account [ " + accountNumber +
                " | " + formatter.format(balance) +
                " ] ";
    }

    public void UpdateAccount(Account account)
    {
        if(account.accountNumber == accountNumber)
            this.balance = account.balance;
    }

    public int getAccountNumber() { return accountNumber; }

    public double getBalance() {return balance;}

    public String getBalanceAsString()
    {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(balance);
    }
}
