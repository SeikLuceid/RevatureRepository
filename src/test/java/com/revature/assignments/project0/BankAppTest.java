package com.revature.assignments.project0;

import com.revature.assignments.project0.dataObjects.*;
import com.revature.assignments.project0.singletons.*;
import com.revature.assignments.project0.states.*;
import org.apache.logging.log4j.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BankAppTest
{
    @Test
    void Functionality_should_reflect_the_below_user_stories()
    {
        As_a_user_I_can_login();
        As_a_customer_I_can_apply_for_a_new_bank_account_with_a_starting_balance();
        As_a_customer_I_can_view_the_balance_of_a_specific_account();
        As_a_customer_I_can_make_a_withdrawal_or_deposit_to_a_specific_account();
        As_the_system_I_reject_invalid_transactions();
        As_the_system_I_reject_a_withdrawal_that_would_result_in_a_negative_balance();
        As_the_system_I_reject_a_deposit_or_withdrawal_of_negative_money();
        As_an_employee_I_can_approve_or_reject_an_account();
        As_an_employee_I_can_view_a_customers_bank_accounts();
        As_a_user_I_can_register_for_a_customer_account();
        As_a_user_I_can_register_for_a_customer_account();
        As_a_customer_I_can_post_a_money_transfer_to_another_account();
        As_a_customer_I_can_accept_a_money_transfer_from_another_account();
        As_an_employee_I_can_view_a_log_of_all_transactions();
    }

    @Test
    void Data_is_stored_in_a_database()
    {
        try{
            Connection conn = DatabaseConnect.connect();
            assertTrue(conn.isValid(1));
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, "test", "accounts", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "customers", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "employees", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "pending_accounts", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "transactions", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "transfers", null);
            assertTrue(rs.first());
            rs = metaData.getTables(null, "test", "users", null);
            assertTrue(rs.first());

        } catch (SQLException e)
        {
            fail();
        }
    }

    @Test
    void A_custom_stored_procedure_is_called_to_perform_some_portion_of_the_functionality()
    {
        try{
            Connection conn = DatabaseConnect.connect();
            assert(conn.isValid(1));
            CallableStatement call = conn.prepareCall("{? = call test.username_exists(?)}");
            call.registerOutParameter(1, Types.BOOLEAN);
            call.setString(2, "ADMIN");
            call.execute();
            boolean exists = call.getBoolean(1);
            assertTrue(exists);
        }   catch (SQLException e)
        {
            fail();
        }
    }

    @Test
    void Data_Access_is_performed_through_the_use_of_JDBC_in_a_data_layer_consisting_of_Data_Access_Objects()
    {
        User u = DatabaseConnect.attemptLogin("ADMIN", "PASSWORD");
        Customer c = (Customer)DatabaseConnect.attemptLogin("SEIKLUCEID", "sl-admin");
        Employee e = (Employee)DatabaseConnect.attemptLogin("ADMIN", "PASSWORD");
        assertNotNull(u);
        assertNotNull(c);
        assertNotNull(e);
        assertEquals(Employee.class, u.getClass());
        assertEquals(Customer.class, c.getClass());
        assertEquals(Employee.class, e.getClass());
    }

    @Test
    void All_input_is_received_using_the_java_util_Scanner_class()
    {
        Input.SetScanner(new Scanner("Test"));
        assertTrue(Input.getString().equals("Test"));
    }

    @Test
    void Log4j_is_implemented_to_log_events_to_a_file()
    {
        Logger logger = LogManager.getLogger(DatabaseConnect.class);
        String classString = "class org.apache.logging.log4j.core.Logger";
        assertEquals(logger.getClass().toString(), classString);
    }

    @Test
    void A_minimum_of_one_JUnit_test_is_written_to_test_some_functionality()
    {
        Functionality_should_reflect_the_below_user_stories();
        Data_is_stored_in_a_database();
        A_custom_stored_procedure_is_called_to_perform_some_portion_of_the_functionality();
        Data_Access_is_performed_through_the_use_of_JDBC_in_a_data_layer_consisting_of_Data_Access_Objects();
        All_input_is_received_using_the_java_util_Scanner_class();
        Log4j_is_implemented_to_log_events_to_a_file();
        As_a_user_I_can_login();
        As_a_customer_I_can_apply_for_a_new_bank_account_with_a_starting_balance();
        As_a_customer_I_can_view_the_balance_of_a_specific_account();
        As_a_customer_I_can_make_a_withdrawal_or_deposit_to_a_specific_account();
        As_the_system_I_reject_invalid_transactions();
        As_the_system_I_reject_a_withdrawal_that_would_result_in_a_negative_balance();
        As_the_system_I_reject_a_deposit_or_withdrawal_of_negative_money();
        As_an_employee_I_can_approve_or_reject_an_account();
        As_an_employee_I_can_view_a_customers_bank_accounts();
        As_a_user_I_can_register_for_a_customer_account();
        As_a_customer_I_can_post_a_money_transfer_to_another_account();
        As_a_customer_I_can_accept_a_money_transfer_from_another_account();
        As_an_employee_I_can_view_a_log_of_all_transactions();
    }

    @Test
    void As_a_user_I_can_login()
    {
        String username = "ADMIN";
        String password = "PASSWORD";
        int user_id = 4;
        User c = DatabaseConnect.attemptLogin(username, password);
        assertEquals(c.getId(), user_id);
    }

    @Test
    void As_a_customer_I_can_apply_for_a_new_bank_account_with_a_starting_balance()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";
        double deposit = 100D;

        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        Account account = DatabaseConnect.createBankAccount(c, deposit);

        assertNotNull(c);
        assertNotNull(account);

        String empName = "ADMIN";
        String empPass = "PASSWORD";
        Employee e = (Employee)DatabaseConnect.attemptLogin(empName, empPass);


        ArrayList<Account> pendings = DatabaseConnect.getAllPendingAccounts(e);
        for(Account pending : pendings)
        {
            if(pending.getUser_id() == c.getId())
            {
                assertTrue(DatabaseConnect.removePendingAccount(pending));
            }
        }
    }

    @Test
    void As_a_customer_I_can_view_the_balance_of_a_specific_account()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";

        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(c.getId());

        assertTrue(accounts.get(0).getBalance() > 0D);
    }

    @Test
    void As_a_customer_I_can_make_a_withdrawal_or_deposit_to_a_specific_account()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";

        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(c.getId());

        Account account = accounts.get(0);
        Double balance = account.getBalance();
        account = DatabaseConnect.adjustBalanceToAccount(5D, account.getAccountNumber(), c.getId());
        assertTrue(account.getBalance() == 5D);
        account = DatabaseConnect.adjustBalanceToAccount(balance, account.getAccountNumber(), c.getId());
        assertTrue(account.getBalance() == balance);
    }

    @Test
    void As_the_system_I_reject_invalid_transactions()
    {
        As_the_system_I_reject_a_withdrawal_that_would_result_in_a_negative_balance();
        As_the_system_I_reject_a_deposit_or_withdrawal_of_negative_money();
    }

    @Test
    void As_the_system_I_reject_a_withdrawal_that_would_result_in_a_negative_balance()
    {
        String output =  "How much did you wish to withdraw? (0) for back.\n" +
                "Amount: You can not withdraw an amount greater than your balance.\n" +
                "How much did you wish to withdraw? (0) for back.\n" +
                "Amount: ";
        String username = "SEIKLUCEID";
        String password = "sl-admin";

        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(c.getId());
        Account account = accounts.get(0);

        double balance = account.getBalance();
        Input.SetScanner(new Scanner((balance + 1) + "\n0"));

        PrintStream previousConsole = System.out;
        ByteArrayOutputStream newConsole = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newConsole));

        AccountMenu menu = new AccountMenu(account, c);
        menu.withdrawFromAccount();

        System.setOut(previousConsole);
        String console = newConsole.toString();
        console = console.replaceAll("\\r\\n", "\n");
        console = console.replaceAll("\\r", "\n");

        assertEquals(output, console);
    }

    @Test
    void As_the_system_I_reject_a_deposit_or_withdrawal_of_negative_money()
    {
        String output =  "How much did you wish to withdraw? (0) for back.\n" +
                "Amount: You can not withdraw an amount less than 0.\n" +
                "How much did you wish to withdraw? (0) for back.\n" +
                "Amount: ";
        String username = "SEIKLUCEID";
        String password = "sl-admin";

        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        Account account = DatabaseConnect.getAccountsByID(c.getId()).get(0);

        Input.SetScanner(new Scanner("-1\n0"));

        PrintStream previousConsole = System.out;
        ByteArrayOutputStream newConsole = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newConsole));

        AccountMenu menu = new AccountMenu(account, c);
        menu.withdrawFromAccount();

        System.setOut(previousConsole);
        String console = newConsole.toString();
        console = console.replaceAll("\\r\\n", "\n");
        console = console.replaceAll("\\r", "\n");

        assertEquals(output, console);
    }

    @Test
    void As_an_employee_I_can_approve_or_reject_an_account()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";
        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        Account account = DatabaseConnect.createBankAccount(c, 100D);


        DatabaseConnect.removePendingAccount(account);
    }

    @Test
    void As_an_employee_I_can_view_a_customers_bank_accounts()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";
        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(c.getId());
        assertTrue(accounts.size() > 0);
    }

    @Test
    void As_a_user_I_can_register_for_a_customer_account()
    {
        String username = "TEST";
        String password = "EXAMPLE";
        String firstName = "JUnit";
        String lastName = "Test Runner";
        String ssn = "888-88-8888";

        DatabaseConnect.createUser(username, password, firstName, lastName, ssn);
        boolean success = DatabaseConnect.usernameExists(username) && DatabaseConnect.ssnExists(ssn);
        assertTrue(success);


        if(!success)
        {
            fail();
            return;
        }

        try{
            Connection conn = DatabaseConnect.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username=?");
            stmt.setString(1, username);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("DELETE FROM customers WHERE ssn=?");
            stmt.setString(1, ssn);
            stmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        success = DatabaseConnect.usernameExists(username) && DatabaseConnect.ssnExists(ssn);
        assertFalse(success);
    }

    @Test
    void As_a_customer_I_can_post_a_money_transfer_to_another_account()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";
        Customer c = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts = DatabaseConnect.getAccountsByID(c.getId());

        String username2 = "MATTHEW";
        String password2 = "suttles";
        Customer c2 = (Customer)DatabaseConnect.attemptLogin(username2, password2);
        ArrayList<Account> accounts2 = DatabaseConnect.getAccountsByID(c2.getId());

        Account acc1 = accounts.get(0);
        Account acc2 = accounts2.get(0);
        double amount = 1D;

        assertTrue(DatabaseConnect.initiateTransfer(acc1.getAccountNumber(), acc2.getAccountNumber(), amount));
        ArrayList<Transfer> transfers = DatabaseConnect.getPendingTransfersFrom(acc1);

        Transfer selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            if(
                    transfer.getOrigin() == acc1.getAccountNumber() &&
                            transfer.getDestination() == acc2.getAccountNumber() &&
                            transfer.getAmount() == amount
            )
            {
                selectedTransfer = transfer;
                break;
            }

        }
        assertTrue(DatabaseConnect.cancelTransfer(selectedTransfer));
    }

    @Test
    void As_a_customer_I_can_accept_a_money_transfer_from_another_account()
    {
        String username = "SEIKLUCEID";
        String password = "sl-admin";
        Customer c1 = (Customer)DatabaseConnect.attemptLogin(username, password);
        ArrayList<Account> accounts1 = DatabaseConnect.getAccountsByID(c1.getId());

        String username2 = "MATTHEW";
        String password2 = "suttles";
        Customer c2 = (Customer)DatabaseConnect.attemptLogin(username2, password2);
        ArrayList<Account> accounts2 = DatabaseConnect.getAccountsByID(c2.getId());

        Account acc1 = accounts1.get(0);
        Account acc2 = accounts2.get(0);

        int origin = acc1.getAccountNumber();
        int destination = acc2.getAccountNumber();
        double amount = 1D;

        Account account = DatabaseConnect.requestAccountByNumber(origin, c1.getId());

        assertTrue(DatabaseConnect.initiateTransfer(origin, destination, amount));
        ArrayList<Transfer> transfers = DatabaseConnect.getPendingTransfersFrom(account);

        Transfer selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            if(
                    transfer.getOrigin() == origin &&
                    transfer.getDestination() == destination &&
                    transfer.getAmount() == amount
            )
            {
                selectedTransfer = transfer;
                break;
            }
        }
        assertTrue(DatabaseConnect.applyTransfer(selectedTransfer));

        int targetDestination = destination;
        destination = origin;
        origin = targetDestination;

        account = DatabaseConnect.requestAccountByNumber(origin, c2.getId());

        assertTrue(DatabaseConnect.initiateTransfer(origin, destination, amount));
        transfers = DatabaseConnect.getPendingTransfersFrom(account);

        selectedTransfer = null;
        for(Transfer transfer : transfers)
        {
            if(
                    transfer.getOrigin() == origin &&
                            transfer.getDestination() == destination &&
                            transfer.getAmount() == amount
            )
            {
                selectedTransfer = transfer;
                break;
            }
        }

        assertTrue(DatabaseConnect.applyTransfer(selectedTransfer));
    }

    @Test
    void As_an_employee_I_can_view_a_log_of_all_transactions()
    {
        ArrayList<Transaction> transactions = DatabaseConnect.getTransactions();
        assertTrue(transactions.size() > 0);
    }
}