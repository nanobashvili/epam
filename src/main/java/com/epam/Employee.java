package com.epam;

/**
 * Represents an employee with an ID, name, salary, and manager ID.
 */
public record Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
}