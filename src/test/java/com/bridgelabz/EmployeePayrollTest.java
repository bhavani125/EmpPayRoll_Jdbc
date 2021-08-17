package com.bridgelabz;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EmployeePayrollTest {

    EmployeePayrollService employeePayrollService = null;

    @Before
    public void setUp() throws Exception {
        employeePayrollService = new EmployeePayrollService();
    }

    //To check the count in database is matching in java program or not
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount()  throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    //To check whether the salary is updated in the database and is synced with the DB
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException{
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
    //To test whether the salary is updated in the database and is synced with the DB using JDBC PreparedStatement
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDBUsingPreparedStatement() throws EmployeePayrollException  {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Charlie", 2000000);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
}
