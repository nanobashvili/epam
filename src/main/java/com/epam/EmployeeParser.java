package com.epam;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing a CSV file into Employee objects.
 */
public class EmployeeParser {

    // Private constructor to prevent instantiation.
    private EmployeeParser() {}

    /**
     * Parses the provided CSV file and returns a map of Employee objects.
     *
     * @param filePath the path to the CSV file
     * @return a map of Employee objects with employee IDs as keys
     * @throws IOException if there is an error reading the file
     * @throws CsvValidationException if there is an error parsing the CSV format
     */
    public static Map<Integer, Employee> parseCSV(String filePath) throws IOException, CsvValidationException {
        Map<Integer, Employee> employees = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Skip the header row
            reader.readNext();

            // Read and parse each row
            String[] line;
            while ((line = reader.readNext()) != null) {
                int id = Integer.parseInt(line[0]);
                String firstName = line[1];
                String lastName = line[2];
                double salary = Double.parseDouble(line[3]);
                Integer managerId = line[4].isEmpty() ? null : Integer.parseInt(line[4]);
                employees.put(id, new Employee(id, firstName, lastName, salary, managerId));
            }
        }
        return employees;
    }
}