package com.revature.assignments.assignment0.dataObjects;

import java.sql.Date;

public class Transaction {

    private char transactionType;
    private int source;
    private int destination;
    private double amount;
    private int transaction_id;
    private Date date;

    public Transaction(char transactionType, int source, int destination, double amount, int transaction_id, Date date) {
        this.transactionType = transactionType;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.transaction_id = transaction_id;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction:[" +
                "Type: " + transactionType +
                ", Source: " + (source == 0 ? "CASH" : source) +
                ", Destination: " + (destination == 0 ? "CASH" : destination) +
                ", Amount: " + amount +
                ", ID: " + transaction_id +
                ", Date: " + date +
                " ]";
    }
}
