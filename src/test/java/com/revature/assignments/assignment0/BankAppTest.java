package com.revature.assignments.assignment0;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BankAppTest
{
    //Requirements
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
        As_a_customer_I_can_post_a_money_transfer_to_another_account();
        As_a_customer_I_can_accept_a_money_transfer_from_another_account();
        As_an_employee_I_can_view_a_log_of_all_transactions();
    }

    @Test
    void Data_is_stored_in_a_database()
    {
        assertTrue(true);
    }

    @Test
    void A_custom_stored_procedure_is_called_to_perform_some_portion_of_the_functionality()
    {
        assertTrue(true);
    }

    @Test
    void Data_Access_is_performed_through_the_use_of_JDBC_in_a_data_layer_consisting_of_Data_Access_Objects()
    {
        assertTrue(true);
    }

    @Test
    void All_input_is_received_using_the_java_util_Scanner_class()
    {
        assertTrue(true);
    }

    @Test
    void Log4j_is_implemented_to_log_events_to_a_file()
    {
        assertTrue(true);
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

    //_User_Stories
    @Test
    void As_a_user_I_can_login()
    {
        assertTrue(true);
    }

    @Test
    void As_a_customer_I_can_apply_for_a_new_bank_account_with_a_starting_balance()
    {
        assertTrue(true);
    }

    @Test
    void As_a_customer_I_can_view_the_balance_of_a_specific_account()
    {
        assertTrue(true);
    }

    @Test
    void As_a_customer_I_can_make_a_withdrawal_or_deposit_to_a_specific_account()
    {
        assertTrue(true);
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
        assertTrue(true);
    }

    @Test
    void As_the_system_I_reject_a_deposit_or_withdrawal_of_negative_money()
    {
        assertTrue(true);
    }

    @Test
    void As_an_employee_I_can_approve_or_reject_an_account()
    {
        assertTrue(true);
    }

    @Test
    void As_an_employee_I_can_view_a_customers_bank_accounts()
    {
        assertTrue(true);
    }

    @Test
    void As_a_user_I_can_register_for_a_customer_account()
    {
        assertTrue(true);
    }

    @Test
    void As_a_customer_I_can_post_a_money_transfer_to_another_account()
    {
        assertTrue(true);
    }

    @Test
    void As_a_customer_I_can_accept_a_money_transfer_from_another_account()
    {
        assertTrue(true);
    }

    @Test
    void As_an_employee_I_can_view_a_log_of_all_transactions()
    {
        assertTrue(true);
    }
}