package com.revature.assignments.assignment0.dataObjects;

import java.text.NumberFormat;

public class Transfer
{
    private final int transferId;
    private final int origin;
    private final int destination;
    private final double amount;

    public Transfer(int transferId, int origin, int destination, double amount)
    {
        this.transferId = transferId;
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
    }

    public String toString() {
        return "Transfer [ " +
                "Origin Account: " + origin +
                ", Destination Account: " + destination +
                ", Amount: " + getAmountAsString() +
                " ]";
    }

    public int getOrigin() {
        return origin;
    }

    public int getDestination() {
        return destination;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountAsString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    public int getTransferID() {
        return transferId;
    }
}
