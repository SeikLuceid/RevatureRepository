package com.revature.assignments.project0.dataObjects;

import java.sql.Date;
import java.text.NumberFormat;

public class Transaction {

    private final char transactionType;
    private final int source;
    private final int destination;
    private final double amount;
    private final int transaction_id;
    private final Date date;

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
                ", Amount: " + NumberFormat.getCurrencyInstance().format(amount) +
                ", ID: " + transaction_id +
                ", Date: " + date +
                " ]";
    }
}
