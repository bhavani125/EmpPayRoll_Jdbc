package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class EmployeePayrollTest {

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        //Creating object for EmployeePayRollService Class
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }
}
