package com.revature.day09;

import com.revature.databases.DatabaseConnect;

import java.sql.*;
import java.util.ArrayList;

public class StudentCRUDImpl implements StudentCRUD
{
    private PreparedStatement addStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement readStmt;

    public StudentCRUDImpl()
    {
        Connection conn = DatabaseConnect.connect();
        try
        {
            addStmt = conn.prepareStatement("INSERT INTO student(first_name, last_name, age) VALUES (?, ?, ?);");
            updateStmt = conn.prepareStatement("UPDATE student SET first_name = ? WHERE id = ?;");
            deleteStmt = conn.prepareStatement("DELETE FROM student WHERE id = ?;");
            readStmt = conn.prepareStatement("SELECT * FROM student;");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void add(String firstName, String lastName, int age)
    {
        try
        {
            addStmt.setString(1, firstName);
            addStmt.setString(2, lastName);
            addStmt.setInt(3, age);
            addStmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void update(Student student, String firstName, String lastName, int age)
    {
        try
        {
            updateStmt.setString(1, firstName);
            updateStmt.setInt(2, student.getId());
            updateStmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(Student student)
    {
        try
        {
            deleteStmt.setInt(1, student.getId());
            deleteStmt.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Student[] read()
    {
        ArrayList<Student> students = new ArrayList<>();
        
        try(ResultSet results = readStmt.executeQuery())
        {
            while(results.next())
            {
                students.add(new Student(results.getInt(1), results.getString(2), results.getString(3), results.getInt(4)));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return students.toArray(new Student[students.size()]);
    }
}
