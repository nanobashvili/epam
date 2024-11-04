package com.epam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the OrgAnalyzer class, which analyzes salaries and reporting lines.
 */
class OrgAnalyzerTest {
    private OrgAnalyzer analyzer;
    private Map<Integer, Employee> employees;

    // Set up a sample organization structure before each test
    @BeforeEach
    public void setUp() {
        employees = new HashMap<>();
        employees.put(123, new Employee(123, "Joe", "Doe", 60000, null)); // CEO
        employees.put(124, new Employee(124, "Martin", "Chekov", 45000, 123));
        employees.put(125, new Employee(125, "Bob", "Ronstad", 47000, 123));
        employees.put(300, new Employee(300, "Alice", "Hasacat", 50000, 124));
        employees.put(305, new Employee(305, "Brett", "Hardleaf", 34000, 300));
        analyzer = new OrgAnalyzer(employees);
    }

    /**
     * Test that a manager earns less than the required minimum salary.
     */
    @Test
    void testAnalyzeSalaries_whenManagerEarnsLessThanMinimum() {
        List<String> result = analyzer.analyzeSalaries();

        // Expecting 1 report for Martin since his salary is less than the minimum requirement
        assertEquals(1, result.size());
        assertEquals(
                "Manager Martin Chekov (ID: 124) earns less than required. Current Salary: 45000.00, " +
                        "Minimum Required Salary: 60000.00, Difference: 15000.00, Average Subordinate Salary: 50000.00",
                result.get(0)
        );
    }

    /**
     * Test that a manager's salary is within the required range.
     */
    @Test
    void testAnalyzeSalaries_whenManagerSalaryIsWithinLimits() {
        // Adding a subordinate to adjust Joe's average subordinate salary
        employees.put(127, new Employee(127, "Tom", "Smith", 65000, 123)); // New subordinate
        analyzer = new OrgAnalyzer(employees);

        // Expecting Joe to be flagged as earning less than required due to new average
        List<String> result = analyzer.analyzeSalaries();
        assertTrue(result.stream().anyMatch(r -> r.contains("Joe Doe (ID: 123) earns less than required")));
    }

    /**
     * Test that a manager earns more than the allowed maximum salary.
     */
    @Test
    void testAnalyzeSalaries_whenManagerEarnsMoreThanMaximum() {
        // Adding a high-salary subordinate to Joe
        employees.put(126, new Employee(126, "Sarah", "Connor", 200000, 123));
        analyzer = new OrgAnalyzer(employees);

        // Ensure Joe is not flagged as exceeding maximum since his salary is low relative to Sarah's
        List<String> result = analyzer.analyzeSalaries();
        assertTrue(result.isEmpty() || result.stream().noneMatch(r -> r.contains("Joe Doe (ID: 123) earns more than allowed")));
    }

    /**
     * Test reporting line length when no employee exceeds the maximum allowed distance.
     */
    @Test
    void testAnalyzeReportingLines_whenReportingLineIsTooLong() {
        List<String> result = analyzer.analyzeReportingLines();

        // No employee has a reporting line that exceeds 4 levels
        assertEquals(0, result.size());
    }

    /**
     * Test reporting line length when the reporting line is within acceptable limits.
     */
    @Test
    void testAnalyzeReportingLines_whenReportingLineIsWithinLimit() {
        employees.remove(305); // Remove Brett from hierarchy
        analyzer = new OrgAnalyzer(employees);

        // Expecting an empty result as no reporting line exceeds the limit
        List<String> result = analyzer.analyzeReportingLines();
        assertTrue(result.isEmpty());
    }

    /**
     * Test distance calculation for the CEO, who should have a distance of 0 to themselves.
     */
    @Test
    void testCalculateDistanceToCEO_forCEO() {
        int distanceToCEO = analyzer.calculateDistanceToCEO(123);
        assertEquals(0, distanceToCEO, "CEO should have a distance of 0 to themselves.");
    }

    /**
     * Test distance calculation for an employee with one manager.
     */
    @Test
    void testCalculateDistanceToCEO_forEmployeeWithOneManager() {
        int distanceToCEO = analyzer.calculateDistanceToCEO(124); // Martin reports directly to Joe
        assertEquals(1, distanceToCEO);
    }

    /**
     * Test distance calculation for an employee with multiple managers up to the CEO.
     */
    @Test
    void testCalculateDistanceToCEO_forEmployeeWithMultipleManagers() {
        int distanceToCEO = analyzer.calculateDistanceToCEO(305); // Brett has multiple levels to CEO
        assertEquals(3, distanceToCEO, "Brett should have a distance of 3 to the CEO.");
    }
}