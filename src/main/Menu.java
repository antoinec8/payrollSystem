package main;

import controller.EmployeeController;
import controller.PaymentController;
import model.Company.Company;
import model.Employee.Employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class Menu 
{
    public static void menu(Company company) 
    {
        EmployeeController employeeController = new EmployeeController();
        PaymentController paymentController = new PaymentController();

        Stack<String> undo = new Stack<>();
        Stack<String> redo = new Stack<>();

        Scanner in = new Scanner(System.in);
        in.useDelimiter("\r?\n");

        int option;
        Menu.menuOptions();

        try 
        {
            while (true) 
            {
                option = in.nextInt();
                assert company != null;

                if (option == 1) 
                {
                    undo.push(storeState(company));
                    System.out.println("\nAdd an employee:");
                    employeeController.createEmployee(in, company.getEmployees(), company.getPaymentEmployees(), company.getPaymentSchedules());

                } 
                else if (option == 2) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nEdit an employee:");
                    System.out.println("Employee Id:");
                    employeeId = in.next();
                    employeeController.editEmployeeMenu(in, employeeId, company.getEmployees(), company.getPaymentSchedules());

                } 
                else if (option == 3) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nRemove an employee:");
                    System.out.println("Employee id:");
                    employeeId = in.next();
                    employeeController.deleteEmployee(employeeId, company.getEmployees(), company.getPaymentEmployees());
                } 
                else if (option == 4) 
                {
                    System.out.println("\nShow all employees:");
                    employeeController.listEmployees(company.getEmployees());
                } 
                else if (option == 5) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nAdd time card:");
                    System.out.println("Employee id:");
                    employeeId = in.next();
                    employeeController.addTimeCard(in, employeeId, company.getEmployees());
                } 
                else if (option == 6) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nAdd a sales result:");
                    System.out.println("Employee id:");
                    employeeId = in.next();
                    employeeController.addSaleResult(in, employeeId, company.getEmployees());
                } 
                else if (option == 7) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nAdd additional service tax:");
                    System.out.println("Employee id:");
                    employeeId = in.next();
                    employeeController.addAdditionalServiceTax(in, employeeId, company.getEmployees());
                } 
                else if (option == 8) 
                {
                    undo.push(storeState(company));
                    System.out.println("\nRun payroll:");
                    paymentController.payRoll(in, company.getPaymentEmployees(), company.getPaymentHistories());
                } 
                else if (option == 9) 
                {
                    if (undo.isEmpty()) 
                    {
                        System.out.println("Undo stack is empty!");
                    } 
                    else 
                    {
                        redo.push(storeState(company));
                        company = restoreState(undo.pop());
                        System.out.println("Successful!");
                    }
                } 
                else if (option == 10) 
                {
                    if (redo.isEmpty()) 
                    {
                        System.out.println("Redo stack is empty!");
                    }
                    else 
                    {
                        undo.push(storeState(company));
                        company = restoreState(redo.pop());
                        System.out.println("Successful!");
                    }

                } 
                else if (option == 11) 
                {
                    undo.push(storeState(company));
                    String employeeId;
                    System.out.println("\nChange employee payment schedule:");
                    System.out.println("Employee Id:");
                    employeeId = in.next();
                    Employee employee = employeeController.searchEmployee(employeeId, company.getEmployees());
                    if (employee == null) 
                    {
                        System.out.println("Employee not found!");
                    } 
                    else 
                    {
                        paymentController.editEmployeePaymentSchedule(in, employee.getPaymentEmployee(), company.getPaymentSchedules());
                    }
                } 
                else if (option == 12) 
                {
                    undo.push(storeState(company));
                    System.out.println("Add new payment schedule:");
                    paymentController.addPaymentSchedule(in, company.getPaymentSchedules());

                } 
                else if (option == 13) 
                {
                    if (company.getPaymentHistories().isEmpty()) 
                    {
                        System.out.println("Empty payment histories");
                    } 
                    else 
                    {
                        PaymentController.printPaymentHistories(company.getPaymentHistories());
                    }

                } 
                else if (option == 14) 
                {
                    System.out.println("\nGoodbye!");
                    return;

                } 
                else 
                {
                    System.out.println("Invalid option! Please, try again.");
                }

            }
        } 
        catch (DateTimeException | IndexOutOfBoundsException err) 
        {
            System.out.println("Invalid date");
            menu(company);

        } 
        catch (InputMismatchException | NumberFormatException err)
        {
            System.out.println("Invalid input");
            menu(company);
        }
    }

    private static void menuOptions() 
    {
        System.out.println("             ----- WELCOME -----");
        System.out.println("----- Choose one of the options below: -----");
        System.out.println("[1] - Add an employee");
        System.out.println("[2] - Edit an employee");
        System.out.println("[3] - Remove an employee");
        System.out.println("[4] - Show all employees");
        System.out.println("[5] - Add a timecard for an hourly employee");
        System.out.println("[6] - Add a sales result for a commissioned employee");
        System.out.println("[7] - Add additional service tax for a syndicalist employee");
        System.out.println("[8] - Run payroll");
        System.out.println("[9] - Undo");
        System.out.println("[10] - Redo");
        System.out.println("[11] - Change employee payment schedule");
        System.out.println("[12] - Add new payment schedule");
        System.out.println("[13] - List payment histories");
        System.out.println("[14] - Exit");
    }

    private static String storeState(Company company) 
    {
        try 
        {
            ByteArrayOutputStream bass = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bass);
            oos.writeObject(company);
            oos.close();
            return Base64.getEncoder().encodeToString(bass.toByteArray());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return "Error!";
        }
    }

    private static Company restoreState(String state) 
    {
        try 
        {
            byte[] data = Base64.getDecoder().decode(state);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (Company) ois.readObject();
        }
         catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Error!");
            return null;
        }
    }
}
