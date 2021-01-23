package com.revature.day09;

import java.util.Scanner;

public class StudentCRUDMain
{
    public static void main(String[] args)
    {
        


        StudentCRUD scrud = new StudentCRUDImpl();

        scrud.add("Jasdhir", "Singh", 35);
        scrud.add("Matthew", "Suttles", 32);

        Student[] students = scrud.read();

        for(Student student : students)
        {
            if(student.getId() == 37)
                scrud.update(student, student.getFirstName() + "-Senpai", student.getLastName(), student.getAge());
        }

        for(Student student : students)
        {
            if(student.getId() == 35)
            {
                scrud.delete(student);
            }
        }

        students = scrud.read();
        for(Student student : students)
        {
            System.out.println(student);
        }
    }
}
