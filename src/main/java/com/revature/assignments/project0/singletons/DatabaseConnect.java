package com.revature.assignments.project0.singletons;

import com.revature.assignments.project0.dataObjects.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DatabaseConnect
{
    public static final Logger logger = LogManager.getLogger();

    private static final String DATABASE_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/bankapp";
    private static final String DATABASE_USER_NAME = "postgres";
    private static final String DATABASE_USER_PASSWORD = "db-admin";

    private static Connection conn;

    private DatabaseConnect() {
    }

    public static Connection connect() {
        if (conn != null)
            return conn;
        try {
            //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DATABASE_CONNECTION_STRING, DATABASE_USER_NAME, DATABASE_USER_PASSWORD);
            return conn;
        } catch (SQLException e) {
            logger.error("SQL", e);
            return null;
        }
    }

    public static boolean usernameExists(String username) {
        try {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, username);
            call.execute();
            boolean exists = call.getBoolean(1);
            call.close();
            return exists;
        } catch (SQLException e) {
            logger.error("SQL", e);
            return true;
        }
    }

    public static User createUser(String username, String password, String firstName, String lastName, String ssn) {
        User newUser = null;

        try {
            verifyConnection();

            CallableStatement call = conn.prepareCall("CALL register_customer( ?, ?, ?, ?, ? );");
            call.setString(1, username);
            call.setString(2, password);
            call.setString(3, firstName);
            call.setString(4, lastName);
            call.setString(5, ssn);
            call.executeUpdate();

            newUser = attemptLogin(username, password);
            call.close();
        } catch (SQLException e) {
           logger.error("SQL", e);
        }

        return newUser;
    }

    public static boolean ssnExists(String ssn_text) {
        try {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call ssn_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, ssn_text);
            call.execute();
            boolean exists = call.getBoolean(1);
            call.close();
            return exists;
        } catch (SQLException e) {
            logger.error("SQL", e);
            return true;
        }
    }

    private static void verifyConnection() throws SQLException {
        try {
            if (conn == null || conn.isClosed())
                connect();
        }
        catch (SQLException e) {
           logger.error("SQL", e);
        }

        if(conn == null || conn.isClosed())
        {
            throw new SQLException("Connection Failed");
        }
    }

    public static User attemptLogin(String username, String password) {
        User newUser = null;

        try {
            verifyConnection();
            CallableStatement call = conn.prepareCall("{? = call public.username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, username);
            call.execute();
            boolean exists = call.getBoolean(1);
            if (!exists) {
                System.out.println("\nUsername not found.\n");
                call.close();
                return newUser;
            }

            call = conn.prepareCall("{? = call check_password(?, ?)}");
            call.registerOutParameter(1, Types.INTEGER);
            call.setString(2, username);
            call.setString(3, password);
            call.execute();
            int id = call.getInt(1);
            if (id <= 0) {
                System.out.println("\nPassword Incorrect.\n");
                call.close();
                return newUser;
            }

            boolean isEmployee = false;
            PreparedStatement statement = conn.prepareStatement("SELECT is_employee FROM users WHERE user_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                isEmployee = rs.getBoolean(1);
            }

            String targetTable = isEmployee ? "employees" : "customers";
            statement = conn.prepareStatement("SELECT * FROM " + targetTable + " WHERE user_id = ?");
            statement.setInt(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                if (isEmployee)
                    newUser = new Employee(rs.getInt(1), rs.getString(2));
                else
                    newUser = new Customer(rs.getString(1), rs.getString(2), rs.getInt(4));
            }

            call.close();
        } catch (SQLException e) {
           logger.error("SQL", e);
           logger.trace("Login Failure.  Please try again Later.");
        }

        return newUser;
    }

    public static ArrayList<Account> getAccountsByID(int customer_id) {
        ArrayList<Account> accounts = new ArrayList<>();

        try {
            verifyConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM accounts WHERE customer_id = ?");
            statement.setInt(1, customer_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(rs.getInt(1), rs.getBigDecimal(2).doubleValue(), customer_id));
            }
        } catch (SQLException e) {
           logger.error("SQL", e);
        }

        return accounts;
    }

    public static Account requestAccountByNumber(int accountNumber, int customer_id) {
        Account account = null;
        try {
            verifyConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND customer_id = ?");
            statement.setInt(1, accountNumber);
            statement.setInt(2, customer_id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                account = new Account(rs.getInt(1), rs.getBigDecimal(2).doubleValue(), customer_id);
            }
        } catch (SQLException e) {
           logger.error("SQL", e);
        }
        return account;
    }

    public static Account adjustBalanceToAccount(double amount, int accountNumber, int customer_id) {
        Account account = null;
        try {
            verifyConnection();

            conn.setAutoCommit(false);
            conn.setSavepoint();

            PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?");
            stmt.setDouble(1, amount);
            stmt.setInt(2, accountNumber);


            account = requestAccountByNumber(accountNumber, customer_id);
            BigDecimal bd = new BigDecimal(account.getBalance());
            double originalBalance = bd.doubleValue();

            stmt.executeUpdate();

            if(originalBalance < amount)
                recordTransaction("D", 0, accountNumber, amount - originalBalance);
            else
                recordTransaction("W", accountNumber, 0, originalBalance - amount);

            account = requestAccountByNumber(accountNumber, customer_id);
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
           logger.error("SQL", e);
            try{
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e1) {
                logger.error("SQL", e1);
            }
        }
        return account;
    }

    public static boolean initiateTransfer(int origin, int destination, double amount) {
        try {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO transfers (origin_account, destination_account, amount) VALUES(?,?,?)");
            stmt.setInt(1, origin);
            stmt.setInt(2, destination);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
           logger.error("SQL", e);
            return false;
        }
    }

    public static ArrayList<Transfer> getPendingTransfersFrom(Account account) {
        return getPendingTransfers(account, true);
    }

    public static ArrayList<Transfer> getPendingTransfersTo(Customer customer) {
        ArrayList<Transfer> transferList = new ArrayList<>();

        for (Account account : getAccountsByID(customer.getId())) {
            for (Transfer transfer : getPendingTransfers(account, false)) {
                boolean exists = false;
                for (Transfer existing : transferList) {
                    if (transfer.getTransferID() == existing.getTransferID()) {
                        exists = true;
                        break;
                    }
                }
                if (!exists)
                    transferList.add(transfer);
            }
        }

        return transferList;
    }

    public static ArrayList<Transfer> getPendingTransfers(Account account, boolean isOrigin) {
        ArrayList<Transfer> transfers = new ArrayList<>();
        try {
            verifyConnection();
            String value = isOrigin ? "origin_account" : "destination_account";
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transfers WHERE " + value + " = ?");
            stmt.setInt(1, account.getAccountNumber());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transfers.add(new Transfer(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4)));
            }
        } catch (SQLException e) {
           logger.error("SQL", e);
        }
        return transfers;
    }

    public static boolean cancelTransfer(Transfer selectedTransfer) {
        try {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM transfers WHERE transfer_id = ?");
            stmt.setInt(1, selectedTransfer.getTransferID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
           logger.error("SQL", e);
            return false;
        }
    }

    public static Account createBankAccount(Customer customer, double deposit) {
        Account account = null;
        try {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO pending_accounts (balance, customer_id) VALUES (?, ?)");
            stmt.setDouble(1, deposit);
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT * FROM pending_accounts WHERE customer_id = ? AND balance = ?");
            stmt.setInt(1, customer.getId());
            stmt.setDouble(2, deposit);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                account = new Account(rs.getInt(1), rs.getDouble(2), rs.getInt(3));
            }
        } catch (SQLException e) {
           logger.error("SQL", e);
        }
        return account;
    }

    public static boolean applyTransfer(Transfer selectedTransfer) {
        try {
            verifyConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?");
            stmt.setInt(1, selectedTransfer.getOrigin());
            ResultSet rs = stmt.executeQuery();
            double origin_balance = 0;
            while (rs.next()) {
                origin_balance = rs.getDouble(1);
            }
            if (origin_balance < selectedTransfer.getAmount())
                throw new SQLException("Origin invalid, or insufficient funds.");

            stmt.setInt(1, selectedTransfer.getDestination());
            rs = stmt.executeQuery();
            double destination_balance = 0;
            while (rs.next()) {
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
            recordTransaction("T", selectedTransfer.getOrigin(), selectedTransfer.getDestination(), selectedTransfer.getAmount());
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
           logger.error("SQL", e);
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException e1) {
                    logger.error("SQL", e1);
                }
            }
            return false;
        }
    }

    public static ArrayList<Account> getAllPendingAccounts(Employee employee) {
        ArrayList<Account> pendingAccounts = new ArrayList<>();
        try {
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE user_id = ?");
            stmt.setInt(1, employee.getId());
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
                throw new SQLException();

            stmt = conn.prepareStatement("SELECT * FROM pending_accounts");
            rs = stmt.executeQuery();
            while(rs.next())
            {
                pendingAccounts.add(
                    new Account(rs.getInt(3), rs.getDouble(1), rs.getInt(2) )
                );
            }
        } catch (SQLException e) {
           logger.error("SQL", e);
        }

        return pendingAccounts;
    }

    public static boolean removePendingAccount(Account account) {
        try{
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM pending_accounts WHERE pending_id = ?");
            stmt.setInt(1, account.getAccountNumber());
            stmt.executeUpdate();
            return true;
        }
        catch(SQLException e){
           logger.error("SQL", e);
            return false;
        }
    }

    public static ArrayList<Transaction> getTransactions()
    {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try{
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions ORDER BY date_time");
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                transactions.add(
                        new Transaction(
                                rs.getString(1).toUpperCase().charAt(0),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getDouble(4),
                                rs.getInt(5),
                                rs.getDate(6)
                        )
                );
            }
        }
        catch (SQLException e) {
           logger.error("SQL", e);
        }

        return transactions;
    }

    public static void recordTransaction(
            String transactionType,
            int source,
            int destination,
            double amount
    ) throws SQLException
    {
        verifyConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions VALUES (?, ?, ?, ?)");
        stmt.setString(1, transactionType);
        stmt.setInt(2, source);
        stmt.setInt(3, destination);
        stmt.setDouble(4, amount);
        stmt.executeUpdate();
    }

    public static boolean approvePendingAccount(Account account)
    {
        try{
            verifyConnection();
            conn.setAutoCommit(false);
            conn.setSavepoint();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts (balance, customer_id) VALUES (?,?)");
            stmt.setDouble(1, account.getBalance());
            stmt.setInt(2, account.getUser_id());
            stmt.executeUpdate();

            boolean removed = removePendingAccount(account);
            if(!removed)
                throw new SQLException();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        }
        catch(SQLException e){
           logger.error("SQL", e);
            try{
                conn.rollback();
                conn.setAutoCommit(true);
            }
            catch(SQLException e1)
            {
                logger.error("SQL", e1);
            }
            return false;
    }
    }

    public static boolean checkValidAccount(int destination)
    {
        try{
            verifyConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ?");
            stmt.setInt(1, destination);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.error("SQL", e);
            return false;
        }
    }

    @Override
    public void finalize() {
        try {
            conn.close();
        } catch (SQLException e) {
           logger.error("SQL", e);
        }
    }
}
