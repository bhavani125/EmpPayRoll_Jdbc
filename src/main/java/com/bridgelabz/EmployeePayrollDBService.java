package com.bridgelabz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    private EmployeePayrollDBService() {
    }
    // For creating the singleton object
    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    // Reading the employee payroll data from the database
    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee_payroll";
        return getEmployeePayrollDataUsingDB(sql);
    }

    //Update the salary in the DB using Statement Interface
    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    //Creating connection with the database
    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Bhavani@125";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful! " + connection);
        return connection;
    }
    // Updating the salary in the DB using Statement Interface
    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String sql = String.format("UPDATE employee_payroll SET salary = %.2f WHERE name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingStatement() for detailed information!");
        }
    }

    public int updateEmployeeDataPreparedStatement(String name, double salary) throws EmployeePayrollException {
        String sql = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, salary);
            statement.setString(2, name);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingPreparedStatement() for detailed information!");
        }
    }

    // Get the list of EmployeePayrollData using the assigned name
    // setString() is used to set the assigned name value in the sql query
    //Return all the attribute values listed for a particular name
    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(name) for detailed information!");
        }
        return employeePayrollList;
    }
    // Assigning the value of the attributes in a list and return it
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(resultSet) for detailed information!");
        }
        return employeePayrollList;
    }

    //To get the details of a particular employee from the DB using PreparedStatement Interface
    private void preparedStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the preparedStatementForEmployeeData() for detailed information!");
        }
    }
    //Create connection to execute query and read the value from the database
    //Assign the value in a list variable
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollDataUsingDB() for detailed information!");
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String sql = String.format("SELECT * FROM employee_payroll",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return getEmployeePayrollDataUsingDB(sql);
    }
    //get average salary group by gender from database
    public Map<String, Double> getAverageSalaryByGender() throws EmployeePayrollException {
        String sql = "SELECT gender,AVG(salary) AS average_salary FROM employee_payroll  GROUP BY gender;";
        Map<String,Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("average_salary");
                genderToAverageSalaryMap.put(gender,salary);
            }
        }catch (SQLException e) {
            throw new  EmployeePayrollException("Connection Error");
        }
        return genderToAverageSalaryMap;
    }
    //to add the new employee to db
    public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) throws  EmployeePayrollException {
        int employeeId = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO employee_payroll (name,gender,salary,start) " +"VALUES ('%s','%s',%s,'%s')",name,gender,salary,Date.valueOf(startDate));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
                employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
            }
        }catch (SQLException e) {
            throw new EmployeePayrollException("Connection To Database Failed !");
        }
        return employeePayrollData;
    }
}