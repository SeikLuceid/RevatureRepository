package com.revature.assignments.assignment0.dataObjects;

import com.revature.assignments.assignment0.states.CustomerMenu;
import com.revature.assignments.assignment0.states.MenuStateMachine;
import com.revature.assignments.assignment0.singletons.DatabaseConnect;

import java.util.ArrayList;

public class Customer implements User {
    private final String firstName;
    private final String lastName;
    private final int customer_id;
    private ArrayList<Account> accounts;

    public Customer(String firstName, String lastName, int customer_id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customer_id = customer_id;

        accounts = DatabaseConnect.getAccountsByID(customer_id);
    }

    public void addAccount(int accountNumber)
    {
        for(Account account : accounts)
        {
            if(account.getAccountNumber() == accountNumber)
            {
                return;
            }
        }
        Account account = DatabaseConnect.requestAccountByNumber(accountNumber, customer_id);
        if(account != null)
        {
            accounts.add(account);
        }
    }

    public String getFirstName() { return firstName; }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return customer_id;
    }

    public void loadMenu() {
        MenuStateMachine.getInstance().setState(new CustomerMenu(this));
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public String toString()
    {
        return firstName + " " + lastName;
    }

    public void forceAccountUpdate()
    {
        accounts = DatabaseConnect.getAccountsByID(customer_id);
    }
}

