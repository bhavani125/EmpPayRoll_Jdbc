package com.bridgelabz;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollTest {

    EmployeePayrollService employeePayrollService = null;

    @Before
    public void setUp() throws Exception {
        employeePayrollService = new EmployeePayrollService();
    }

    //To check the count in database is matching in java program or not
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    //To check whether the salary is updated in the database and is synced with the DB
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    //To test whether the salary is updated in the database and is synced with the DB using JDBC PreparedStatement
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDBUsingPreparedStatement() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalaryUsingPreparedStatement("Charlie", 2000000);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Charlie");
        Assert.assertTrue(result);
    }

    //To test whether the count of the retrieved data who have joined in a particular data range matches with the expected value
    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchTheEmployeeCount() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 03);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() throws EmployeePayrollException {
        try {
            employeePayrollService.readEmployeePayrollData(DB_IO);
            Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
            Assert.assertTrue(averageSalaryByGender.get("M").equals(1500000.00) &&
                    averageSalaryByGender.get("F").equals(3000000.00));
        } catch ( EmployeePayrollException e) {
            e.printStackTrace();
        }
    }
}