package com.revature.databases;

import com.revature.assignments.assignment0.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnect
{
    private static final String DATABASE_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/bankapp";
    private static final String DATABASE_USER_NAME = "postgres";
    private static final String DATABASE_USER_PASSWORD = "db-admin";

    private static Connection conn;

    private DatabaseConnect() { }

    public static Connection connect()
    {
        if(conn != null)
            return conn;
        try
        {
            //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DATABASE_CONNECTION_STRING, DATABASE_USER_NAME, DATABASE_USER_PASSWORD );
            return conn;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean usernameExists(String username)
    {
        try
        {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, username);
            call.execute();
            boolean exists = call.getBoolean(1);
            call.close();
            return exists;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }

    public static User createUser(String username, String password, String firstName, String lastName, String ssn)
    {
        User newUser = null;

        try
        {
            verifyConnection();

            CallableStatement call = conn.prepareCall("CALL register_user( ?, ?, ?, ?, ? );");
            call.setString(1, username);
            call.setString(2, password);
            call.setString(3, firstName);
            call.setString(4, lastName);
            call.setString(5, ssn);
            call.executeUpdate();

            newUser = attemptLogin(username, password);
            call.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newUser;
    }

    public static boolean ssnExists(String ssn_text)
    {
        try
        {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call ssn_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, ssn_text);
            call.execute();
            boolean exists = call.getBoolean(1);
            call.close();
            return exists;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }

    private static void verifyConnection()
    {
        if(conn == null)
            connect();
    }

    public static User attemptLogin(String username, String password)
    {
        User newUser = null;

        try
        {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, username);
            call.execute();
            boolean exists = call.getBoolean(1);
            if(!exists)
            {
                System.out.println("\nUsername not found.\n");
                call.close();
            }

            call = conn.prepareCall("{? = call check_password(?, ?)}");
            call.registerOutParameter(1, Types.INTEGER);
            call.setString(2, username);
            call.setString(3, password);
            call.execute();
            int id = call.getInt(1);
            if(id <= 0)
            {
                System.out.println("\nPassword Incorrect.\n");
                call.close();
            }

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customers WHERE user_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                newUser = new Customer(rs.getString(1), rs.getString(2), rs.getInt(4));
            }
            call.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newUser;
    }

    public static ArrayList<Account> getAccountsByID(int customer_id)
    {
        ArrayList<Account> accounts = new ArrayList<>();

        try
        {
            verifyConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM accounts WHERE customer_id = ?");
            statement.setInt(1, customer_id);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                accounts.add(new Account(rs.getInt(1), rs.getBigDecimal(2).doubleValue()));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return accounts;
    }

    public static Account requestAccountByNumber(int accountNumber, int customer_id)
    {
        Account account = null;
        try
        {
            verifyConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND customer_id = ?");
            statement.setInt(1, accountNumber);
            statement.setInt(2, customer_id);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
               account = new Account(rs.getInt(1), rs.getBigDecimal(2).doubleValue());
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return account;
    }

    public static Account adjustBalanceToAccount(double amount, int accountNumber, int customer_id)
    {
        Account account = null;
        try
        {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?");
            stmt.setDouble(1, amount);
            stmt.setInt(2, accountNumber);
            stmt.executeUpdate();

            account = requestAccountByNumber(accountNumber, customer_id);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return account;
    }

    public static boolean initiateTransfer(int origin, int destination, double amount)
    {
        try{
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO transfers (origin_account, destination_account, amount) VALUES(?,?,?)");
            stmt.setInt(1, origin);
            stmt.setInt(2, destination);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Transfer> getPendingTransfersFrom(Account account)
    {
        return getPendingTransfers(account, true);
    }

    public static ArrayList<Transfer> getPendingTransfersTo(Customer customer)
    {
        ArrayList<Transfer> transferList = new ArrayList<>();

        for(Account account : getAccountsByID(customer.getId()))
        {
            for(Transfer transfer : getPendingTransfers(account, false))
            {
                boolean exists = false;
                for(Transfer existing : transferList)
                {
                    if (transfer.getTransferID() == existing.getTransferID()) {
                        exists = true;
                        break;
                    }
                }
                if(!exists)
                    transferList.add(transfer);
            }
        }

        return transferList;
    }

    public static ArrayList<Transfer> getPendingTransfers(Account account, boolean isOrigin)
    {
        ArrayList<Transfer> transfers = new ArrayList<>();
        try{
            verifyConnection();
            String value = isOrigin ? "origin_account" : "destination_account";
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transfers WHERE "+ value +" = ?");
            stmt.setInt(1, account.getAccountNumber());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                transfers.add(new Transfer(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transfers;
    }

    public static boolean cancelTransfer(Transfer selectedTransfer)
    {
        try{
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM transfers WHERE transfer_id = ?");
            stmt.setInt(1, selectedTransfer.getTransferID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createBankAccount(Customer customer, double deposit)
    {
        try
        {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO pending_accounts (balance, customer_id) VALUES (?, ?)");
            stmt.setDouble(1, deposit);
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean applyTransfer(Transfer selectedTransfer) {
        try {
            verifyConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?");
            stmt.setInt(1, selectedTransfer.getOrigin());
            ResultSet rs = stmt.executeQuery();
            double origin_balance = 0;
            while(rs.next())
            {
                origin_balance = rs.getDouble(1);
            }
            if(origin_balance < selectedTransfer.getAmount())
                throw new SQLException("Origin invalid, or insufficient funds.");

            stmt.setInt(1, selectedTransfer.getDestination());
            rs = stmt.executeQuery();
            double destination_balance = 0;
            while(rs.next())
            {
                destination_balance = rs.getDouble(1);
            }

            stmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?");
            stmt.setDouble(1, origin_balance - selectedTransfer.getAmount());
            stmt.setInt(2, selectedTransfer.getOrigin());
            stmt.executeUpdate();

            stmt.setDouble(1, destination_balance + selectedTransfer.getAmount());
            stmt.setInt(2, selectedTransfer.getDestination());
            stmt.executeUpdate();

            cancelTransfer(selectedTransfer);

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }
    }

    @Override
    public void finalize()
    {
        try
        {
            conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
