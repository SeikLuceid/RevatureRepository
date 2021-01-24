package com.revature.day10;

import com.revature.assignments.assignment0.singletons.DatabaseConnect;

import java.sql.*;

public class PLSQLMainClass
{
    public static void main(String[] args)
    {
        try(Connection conn = DatabaseConnect.connect())
        {
            CallableStatement stmt = conn.prepareCall("CALL create_user(?, ?, ?, ?, ?)");
            stmt.setString(1, "postgres3");
            stmt.setString(2, "dbadmin");
            stmt.setString(3, "Matthew");
            stmt.setString(4, "Suttles");
            stmt.setInt(5, 123456);
            stmt.execute();

            PreparedStatement query = conn.prepareStatement("SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM accounts WHERE ssn = ?;");
            query.setInt(1, 12345);
            ResultSet results = query.executeQuery();

            while(results.next())
            {
                System.out.println(results.getString(1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
