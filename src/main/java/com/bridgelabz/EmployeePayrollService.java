package com.bridgelabz;

import java.util.List;

public class EmployeePayrollService {
    EmployeePayrollDBService dbService = new EmployeePayrollDBService();

    public enum IOService{
        DB_IO;
    }

    private List<EmployeePayrollData> employeePayrollData;

    public EmployeePayrollService() {
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollData = dbService.readData();
        return this.employeePayrollData;
    }
}s
