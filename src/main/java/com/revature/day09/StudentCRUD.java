package com.revature.day09;

public interface StudentCRUD
{
    void add(String firstName, String lastName, int age);
    void update(Student student, String firstName, String lastName, int age);
    void delete(Student student);
    Student[] read();
}

