package com.bridgelabz;

import java.time.LocalDate;

public class EmployeePayrollData {

    public int id;
    public String name;
    public double salary;
    public LocalDate start;
    //Creating Constructor
    public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.start = start;
    }
}