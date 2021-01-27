package com.revature.assignments.project0.dataObjects;

import com.revature.assignments.project0.states.EmployeeMenu;
import com.revature.assignments.project0.states.MenuStateMachine;

public class Employee implements User {

    private int emp_id;
    private String emp_name;

    public Employee(int id, String name)
    {
        emp_id = id;
        emp_name = name;
    }

    @Override
    public String getLastName() { return emp_name; }

    @Override
    public int getId() { return emp_id; }

    @Override
    public void loadMenu()
    {
        MenuStateMachine.getInstance().setState(new EmployeeMenu(this));
    }

    @Override
    public String toString() {
        return "Employee[ " + emp_id +
                " : " + emp_name +
                " ]";
    }
}
