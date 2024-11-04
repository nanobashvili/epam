package com.epam;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main entry point of the application that parses employee data and runs analysis.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Parse employee data from a CSV file
            Map<Integer, Employee> employees = EmployeeParser.parseCSV("employees.csv");

            // Create an analyzer and run analyses
            OrgAnalyzer analyzer = new OrgAnalyzer(employees);

            System.out.println("Salary Analysis:");
            List<String> salaryReport = analyzer.analyzeSalaries();
            salaryReport.forEach(System.out::println);

            System.out.println("\nReporting Line Analysis:");
            List<String> reportingLineReport = analyzer.analyzeReportingLines();
            reportingLineReport.forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (CsvValidationException e) {
            System.err.println("Error parsing CSV file: " + e.getMessage());
        }
    }
}