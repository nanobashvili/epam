package com.epam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Analyzes organizational structure based on employee data.
 */
public class OrgAnalyzer {
    private final Map<Integer, Employee> employees;

    /**
     * Constructs an OrgAnalyzer with a map of employees.
     *
     * @param employees a map of Employee objects with IDs as keys
     */
    public OrgAnalyzer(Map<Integer, Employee> employees) {
        this.employees = employees;
    }

    /**
     * Analyzes each manager's salary against the average salary of their subordinates.
     * Generates a report if the manager's salary is out of the required range.
     *
     * @return a list of strings describing salary issues for managers
     */
    public List<String> analyzeSalaries() {
        List<String> report = new ArrayList<>();
        for (Employee manager : employees.values()) {
            List<Employee> subordinates = getSubordinates(manager.id());
            if (subordinates.isEmpty()) continue;

            // Calculate the average subordinate salary and determine min/max salary range for the manager
            double avgSubordinateSalary = subordinates.stream()
                    .mapToDouble(Employee::salary)
                    .average()
                    .orElse(0);
            double minSalary = avgSubordinateSalary * 1.2;
            double maxSalary = avgSubordinateSalary * 1.5;

            // Check if manager's salary is outside the allowable range
            if (manager.salary() < minSalary) {
                report.add(String.format(
                        "Manager %s %s (ID: %d) earns less than required. Current Salary: %.2f, " +
                                "Minimum Required Salary: %.2f, Difference: %.2f, Average Subordinate Salary: %.2f",
                        manager.firstName(), manager.lastName(), manager.id(),
                        manager.salary(), minSalary, minSalary - manager.salary(), avgSubordinateSalary
                ));
            } else if (manager.salary() > maxSalary) {
                report.add(String.format(
                        "Manager %s %s (ID: %d) earns more than allowed. Current Salary: %.2f, " +
                                "Maximum Allowed Salary: %.2f, Difference: %.2f, Average Subordinate Salary: %.2f",
                        manager.firstName(), manager.lastName(), manager.id(),
                        manager.salary(), maxSalary, manager.salary() - maxSalary, avgSubordinateSalary
                ));
            }
        }
        return report;
    }

    /**
     * Analyzes the reporting line length for each employee and generates a report
     * if the reporting line to the CEO is too long.
     *
     * @return a list of strings describing employees with overly long reporting lines
     */
    public List<String> analyzeReportingLines() {
        List<String> report = new ArrayList<>();
        for (Employee employee : employees.values()) {
            int distanceToCEO = calculateDistanceToCEO(employee.id());
            if (distanceToCEO > 4) {
                report.add(String.format(
                        "Employee %s %s (ID: %d) has a reporting line too long by %d levels. Total Levels to CEO: %d",
                        employee.firstName(), employee.lastName(), employee.id(),
                        distanceToCEO - 4, distanceToCEO
                ));
            }
        }
        return report;
    }

    /**
     * Retrieves a list of subordinates for a given manager by their ID.
     *
     * @param managerId the ID of the manager
     * @return a list of Employee objects who report directly to the manager
     */
    private List<Employee> getSubordinates(int managerId) {
        List<Employee> subordinates = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (Objects.equals(e.managerId(), managerId)) {
                subordinates.add(e);
            }
        }
        return subordinates;
    }

    /**
     * Calculates the distance to the CEO for a given employee by their ID.
     *
     * @param employeeId the ID of the employee
     * @return the number of levels between the employee and the CEO
     */
    int calculateDistanceToCEO(int employeeId) {
        int distance = 0;
        Employee current = employees.get(employeeId);
        while (current != null && current.managerId() != null) {
            current = employees.get(current.managerId());
            distance++;
        }
        return distance;
    }
}