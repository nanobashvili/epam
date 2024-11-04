package com.epam;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EmployeeParser class, which parses a CSV file into Employee objects.
 */
class EmployeeParserTest {

    // Temporary file for testing purposes
    private Path tempFile;

    // Set up a temporary file before each test
    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("employees", ".csv");
    }

    // Clean up the temporary file after each test
    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    /**
     * Test parsing a valid CSV file containing multiple employees.
     */
    @Test
    void testParseCSV_whenValidFileProvided() throws IOException, CsvValidationException {
        // Write test data to the temporary file
        try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile.toFile()))) {
            writer.println("Id,firstName,lastName,salary,managerId");  // Header row
            writer.println("123,Joe,Doe,60000,");
            writer.println("124,Martin,Chekov,45000,123");
            writer.println("125,Bob,Ronstad,47000,123");
        }

        // Parse the CSV file and get the map of employees
        Map<Integer, Employee> employees = EmployeeParser.parseCSV(tempFile.toString());

        // Check that the parsed data is not null and has the expected size
        assertNotNull(employees);
        assertEquals(3, employees.size(), "Expected 3 employees to be parsed.");

        // Verify details of each parsed employee
        Employee joe = employees.get(123);
        assertNotNull(joe);
        assertEquals("Joe", joe.firstName());
        assertEquals("Doe", joe.lastName());
        assertEquals(60000, joe.salary());
        assertNull(joe.managerId()); // CEO has no manager

        Employee martin = employees.get(124);
        assertNotNull(martin);
        assertEquals("Martin", martin.firstName());
        assertEquals("Chekov", martin.lastName());
        assertEquals(45000, martin.salary());
        assertEquals(123, martin.managerId());

        Employee bob = employees.get(125);
        assertNotNull(bob);
        assertEquals("Bob", bob.firstName());
        assertEquals("Ronstad", bob.lastName());
        assertEquals(47000, bob.salary());
        assertEquals(123, bob.managerId());
    }

    /**
     * Test parsing a CSV file with only a header and no employee data.
     */
    @Test
    void testParseCSV_whenEmptyFile() throws IOException, CsvValidationException {
        // Write only the header row to simulate an empty file
        try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile.toFile()))) {
            writer.println("Id,firstName,lastName,salary,managerId"); // Header row only
        }

        // Parse the CSV file
        Map<Integer, Employee> employees = EmployeeParser.parseCSV(tempFile.toString());

        // Verify that no employees were parsed
        assertNotNull(employees);
        assertEquals(0, employees.size(), "Expected 0 employees to be parsed for an empty file.");
    }

    /**
     * Test parsing a CSV file where an employee's managerId field is empty.
     */
    @Test
    void testParseCSV_whenManagerIdIsEmpty() throws IOException, CsvValidationException {
        // Write data with an empty managerId for an employee
        try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile.toFile()))) {
            writer.println("Id,firstName,lastName,salary,managerId"); // Header row
            writer.println("126,Sarah,Connor,80000,"); // ManagerId is empty
        }

        // Parse the CSV file
        Map<Integer, Employee> employees = EmployeeParser.parseCSV(tempFile.toString());

        // Verify that the data is parsed correctly
        assertNotNull(employees);
        assertEquals(1, employees.size(), "Expected 1 employee to be parsed.");

        // Verify the employee details
        Employee sarah = employees.get(126);
        assertNotNull(sarah);
        assertEquals("Sarah", sarah.firstName());
        assertEquals("Connor", sarah.lastName());
        assertEquals(80000, sarah.salary());
        assertNull(sarah.managerId()); // ManagerId should be null
    }
}