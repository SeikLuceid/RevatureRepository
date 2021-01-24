package com.revature.assignments.assignment0.dataObjects;

import java.text.NumberFormat;

public class Account
{
    private int accountNumber = -1;
    private double balance;
    private int userId;

    public Account(int accountNumber, double balance, int userId)
    {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public int getUser_id()
    {
        return userId;
    }

    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return "Account [ Account Number:" + accountNumber +
                " | Balance:" + getBalanceAsString() +
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