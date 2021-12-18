package controller;

import model.Employee.*;
import model.Payment.PaymentEmployee;
import model.Payment.PaymentMethod;
import model.Payment.PaymentSchedule;
import model.Syndicate.AdditionalServiceTax;
import model.Syndicate.EmployeeSyndicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class EmployeeController 
{

    public void setEmployee(Employee employee, ArrayList<Employee> employees) 
    {
        employees.add(employee);
    }

    public void setPaymentEmployee(PaymentEmployee paymentEmployee, ArrayList<PaymentEmployee> paymentEmployees) 
    {
        paymentEmployees.add(paymentEmployee);
    }

    public Employee searchEmployee(String employeeId, ArrayList<Employee> employees) 
    {
        for (Employee employee: employees) 
        {
            if(employee.getId().toString().equals(employeeId)) 
            {
                return employee;
            }
        }
        return null;
    }

    public void deleteEmployee(String employeeId, ArrayList<Employee> employees, ArrayList<PaymentEmployee> paymentEmployees) 
    {
        Employee employee = this.searchEmployee(employeeId, employees);
        if (employee == null) 
        {
            System.out.println("Employee not found!");
            return;
        }
        paymentEmployees.remove(employee.getPaymentEmployee());
        employees.remove(employee);
        System.out.println("Employee removed!");
    }

    public void editEmployeeMenu(Scanner in, String employeeId, ArrayList<Employee> employees, ArrayList<PaymentSchedule> paymentSchedules) 
    {
        Employee employee = this.searchEmployee(employeeId, employees);
        int option;
        if (employee == null) 
        {
            System.out.println("Employee not found!");
            return;
        }
        EmployeeSyndicate employeeSyndicate = employee.getEmployeeSyndicate();
        System.out.println(employee);
        System.out.println("----- Edit employee menu -----");
        System.out.println("[1] - Edit name");
        System.out.println("[2] - Edit address");
        System.out.println("[3] - Edit type");
        System.out.println("[4] - Edit payment method");
        if (employeeSyndicate == null) 
        {
            System.out.println("[5] - Enter on syndicate");
        } 
        else 
        {
            System.out.println("[5] - Exit to syndicate");
            System.out.println("[6] - Edit syndicate id");
            System.out.println("[7] - Edit syndicate tax");
        }
        System.out.println("[8] - Exit edit employee menu");
        option = in.nextInt();
        if (option == 1) 
        {
            String name;
            System.out.println("New name:");
            name = in.next();
            employee.setName(name);

        } 
        else if (option == 2) 
        {
            String address;
            System.out.println("New address:");
            address = in.next();
            employee.setAddress(address);
        } 
        else if (option == 3) 
        {
            int type;
            Double salary;
            System.out.println("Type ([1]-Hourly, [2]-Salaried, [3]-Commissioned):");
            type = in.nextInt();
            System.out.println("New salary:");
            salary = in.nextDouble();
            employees.remove(employee);
            PaymentSchedule paymentSchedule;
            PaymentEmployee paymentEmployee = employee.getPaymentEmployee();
            switch (type) 
            {
                case 1:
                    employee = new Hourly(employee.getId(), employee.getName(), salary, employee.getAddress());
                    paymentSchedule = PaymentController.getPaymentSchedule(2, 1, 5, paymentSchedules);
                    paymentEmployee.setPaymentSchedule(paymentSchedule);
                    employee.setPaymentEmployee(paymentEmployee);
                    employees.add(employee);
                    break;
                case 2:
                    employee = new Salaried(employee.getId(), employee.getName(), salary, employee.getAddress());
                    paymentSchedule = PaymentController.getPaymentSchedule(1, 0,  paymentSchedules);
                    paymentEmployee.setPaymentSchedule(paymentSchedule);
                    employee.setPaymentEmployee(paymentEmployee);
                    employees.add(employee);
                    break;
                case 3:
                    Double commission;
                    System.out.println("Commission Percentage:");
                    commission = in.nextDouble();
                    employee = new Commissioned(employee.getId(), employee.getName(), salary, employee.getAddress(), commission);
                    paymentSchedule = PaymentController.getPaymentSchedule(2, 2, 5, paymentSchedules);
                    paymentEmployee.setPaymentSchedule(paymentSchedule);
                    employee.setPaymentEmployee(paymentEmployee);
                    employees.add(employee);
                    break;
                default:
                    System.out.println("Invalid option! Please, try again.");
                    break;
            }

        } 
        else if (option == 4) 
        {
            int payMethod, agency, account, variation;
            System.out.println("Payment method ([1]-deposit, [2]-check on your address, [3]-check in hands):");
            payMethod = in.nextInt();
            System.out.println("Account Informations");
            System.out.println("Agency:");
            agency = in.nextInt();
            System.out.println("Account:");
            account = in.nextInt();
            System.out.println("Variation:");
            variation = in.nextInt();

            if (payMethod == 1) 
            {
                PaymentMethod paymentMethod = new PaymentMethod(agency, account, variation, payMethod);
                employee.getPaymentEmployee().setPaymentMethod(paymentMethod);

            } 
            else if (payMethod == 2 || payMethod == 3)
            {
                int checkNumber;
                System.out.println("Check Number");
                checkNumber = in.nextInt();
                PaymentMethod paymentMethod = new PaymentMethod(agency, account, variation, payMethod, checkNumber);
                employee.getPaymentEmployee().setPaymentMethod(paymentMethod);
            } 
            else 
            {
                System.out.println("Incorrect option! Please, try again.");
            }


        }
        else if (option == 5 && employeeSyndicate == null)
        {
            Double monthlyTax;
            System.out.println("Monthly syndicate tax:");
            monthlyTax = in.nextDouble();
            employeeSyndicate = new EmployeeSyndicate(monthlyTax);
            employee.setEmployeeSyndicate(employeeSyndicate);

        } 
        else if (option == 5) 
        {
                employee.setEmployeeSyndicate(null);

            } 
            else if (option == 6 && employeeSyndicate != null) 
            {
                String syndicateId;
                System.out.println("New syndicate id:");
                syndicateId = in.next();
                employeeSyndicate.setEmployeeSyndicateId(UUID.fromString(syndicateId));

            } 
            else if (option == 7 && employeeSyndicate != null)
            {
                Double tax;
                System.out.println("New tax:");
                tax = in.nextDouble();
                employeeSyndicate.setMonthlyTax(tax);

            } 
            else if (option == 8)
            {
                return;
            } 
            else 
            {
                System.out.println("incorrect option! Please, try again.");
        }
        System.out.println("Employee edited successfully!");
        System.out.println(employee);
    }

    public void listEmployees(ArrayList<Employee> employees) 
    {
        if (employees.isEmpty()) 
        {
            System.out.println("Empty employee list!");
            return;
        }
        for (Employee employee: employees) 
        {
            System.out.println(employee);
        }
    }

    public void createEmployee(Scanner in, ArrayList<Employee> employees, ArrayList<PaymentEmployee> paymentEmployees, ArrayList<PaymentSchedule> paymentSchedules) 
    {
        String name, address;
        Double salary, commission, monthlyTax;
        int type, isSyndicateParticipant;

        System.out.println("Name:");
        name = in.next();

        System.out.println("Address:");
        address = in.next();

        System.out.println("Type ([1]-Hourly, [2]-Salaried, [3]-Commissioned):");
        type = in.nextInt();

        System.out.println("Salary:");
        salary = in.nextDouble();

        Employee employee;
        PaymentSchedule paymentSchedule;
        PaymentEmployee paymentEmployee;
        switch (type) 
        {
            case 1:
                employee = new Hourly(name, salary, address);
                paymentSchedule = PaymentController.getPaymentSchedule(2, 1, 5, paymentSchedules);
                break;
            case 2:
                employee = new Salaried(name, salary, address);
                paymentSchedule = PaymentController.getPaymentSchedule(1, 0, paymentSchedules);
                break;
            case 3:
                System.out.println("Commission percentage:");
                commission = in.nextDouble();
                employee = new Commissioned(name, salary, address, commission);
                paymentSchedule = PaymentController.getPaymentSchedule(2, 2, 5, paymentSchedules);
                break;
            default:
                System.out.println("Invalid option! Please, try again.");
                return;
        }

        System.out.println("Is syndicate participant? ([1]-Yes, [any other number]-No):");
        isSyndicateParticipant = in.nextInt();
        if (isSyndicateParticipant == 1) 
        {
            System.out.println("Monthly syndicate tax:");
            monthlyTax = in.nextDouble();
            EmployeeSyndicate employeeSyndicate = new EmployeeSyndicate(monthlyTax);
            employee.setEmployeeSyndicate(employeeSyndicate);
        }

        PaymentMethod paymentMethod = null;
        while (true) 
        {
            int payMethod, agency, account, variation;
            System.out.println("Payment method ([1]-deposit, [2]-check on your address, [3]-check in hands):");
            payMethod = in.nextInt();

            System.out.println("Account Informations");
            System.out.println("Agency:");
            agency = in.nextInt();

            System.out.println("Account:");
            account = in.nextInt();

            System.out.println("Variation:");
            variation = in.nextInt();

            if (payMethod == 1) 
            {
                paymentMethod = new PaymentMethod(agency, account, variation, payMethod);
                break;

            } 
            else if (payMethod == 2 || payMethod == 3) 
            {
                int checkNumber;
                System.out.println("Check Number:");
                checkNumber = in.nextInt();
                paymentMethod = new PaymentMethod(agency, account, variation, payMethod, checkNumber);
                break;
            } 
            else 
            {
                System.out.println("Incorrect option! Please, try again!");
            }
        }

        paymentEmployee = new PaymentEmployee(employee, paymentMethod, paymentSchedule);
        employee.setPaymentEmployee(paymentEmployee);

        this.setPaymentEmployee(paymentEmployee, paymentEmployees);
        this.setEmployee(employee, employees);

        System.out.println("Employee successfully added!");
        System.out.println(employee);
    }

    private Hourly getHourlyEmployee(String employeeId, ArrayList<Employee> employees) 
    {
        Employee employee = this.searchEmployee(employeeId, employees);
        if (employee == null || !employee.getClass().isAssignableFrom(Hourly.class)) 
        {
            return null;
        }
        return (Hourly) employee;
    }

    public void addTimeCard(Scanner in, String employeeId, ArrayList<Employee> employees)
    {
        Hourly employee = this.getHourlyEmployee(employeeId, employees);

        if (employee == null) 
        {
            System.out.println("Employee not found in the horly list!");
            return;
        }

        String dateString;
        Double workedHours;

        System.out.println("Date (Format YYYY-MM-D, ex.: 2021-12-25):");
        dateString = in.next();

        LocalDate date = LocalDate.parse(dateString);

        System.out.println("Worked hours:");
        workedHours = in.nextDouble();

        TimeCard timeCard = new TimeCard(workedHours, date);

        employee.setTimeCard(timeCard);

        System.out.println("Time card added to employee " + employee.getId());
        System.out.println(employee.getTimeCards());
    }

    private Commissioned getCommissionedEmployee(String employeeId, ArrayList<Employee> employees) 
    {
        Employee employee = this.searchEmployee(employeeId, employees);
        if (employee == null || !employee.getClass().isAssignableFrom(Commissioned.class)) 
        {
            return null;
        }
        return (Commissioned) employee;
    }

    public void addSaleResult(Scanner in, String employeeId, ArrayList<Employee> employees) 
    {
        Commissioned employee = this.getCommissionedEmployee(employeeId, employees);

        if (employee == null) 
        {
            System.out.println("Employee not found in the commissioned list!");
            return;
        }

        String dateString;
        Double saleValue;

        System.out.println("Date (Format YYYY-MM-D, ex.: 2021-12-25):");
        dateString = in.next();

        LocalDate date = LocalDate.parse(dateString);

        System.out.println("Sale value:");
        saleValue = in.nextDouble();

        SaleResult saleResult = new SaleResult(date, saleValue);

        employee.setSaleResult(saleResult);

        System.out.println("Sale result added to employee " + employee.getId());
        System.out.println(saleResult);
    }

    public void addAdditionalServiceTax(Scanner in, String employeeId, ArrayList<Employee> employees) 
    {
        Employee employee = this.searchEmployee(employeeId, employees);

        if (employee == null || employee.getEmployeeSyndicate() == null) 
        {
            System.out.println("Employee not found in the syndicalist list!");
            return;
        }
        EmployeeSyndicate employeeSyndicate = employee.getEmployeeSyndicate();

        String dateString;
        Double saleValue;

        System.out.println("Date (Format YYYY-MM-D, ex.: 2021-12-25):");
        dateString = in.next();

        LocalDate date = LocalDate.parse(dateString);

        System.out.println("Tax value:");
        saleValue = in.nextDouble();

        AdditionalServiceTax additionalServiceTax = new AdditionalServiceTax(date, saleValue);

        employeeSyndicate.setAdditionalServiceTax(additionalServiceTax);

        System.out.println("Additional service Tax added to employee " + employee.getId());
        System.out.println(additionalServiceTax);
    }
}
