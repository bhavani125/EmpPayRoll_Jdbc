package com.bridgelabz;

import java.util.List;

public class EmployeePayrollService {

    private EmployeePayrollDBService employeePayrollDBService;
    private List<EmployeePayrollData> employeePayrollList;

    public enum IOService {
        DB_IO
    }
    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    // To get the list of employee payroll from the database
    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }
    //updating EmpSalary in DB( Match the given name with EmployeePayrollData list  If found, assign the given salary to it)
    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if(result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if( employeePayrollData != null )
            employeePayrollData.salary = salary;
    }

    // checking whether the EmployeePayrollData is in sync with the DB
    public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    //Checking name in EmployeePayrollData list(If found, return the value else return null)
    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}