package com.home.SteticApi.exception.EmployeeException;


public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(int employeeId) {
        super();
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(long id) {
        super("The employee " + id + " doesn't exist");
    }
}
