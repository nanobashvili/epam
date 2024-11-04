# OrgAnalyzer

## Project Overview
The **OrgAnalyzer** project is a Java application designed to analyze a company’s organizational structure based on employee data in a CSV file. The application evaluates two main areas:

1. **Salary Requirements**: Ensures every manager earns at least 20% more than the average salary of their direct subordinates and no more than 50% more than that average.
2. **Reporting Line Length**: Identifies employees who are more than 4 levels away from the CEO, indicating a complex reporting structure.

The application parses the employee data, performs the analysis, and outputs a detailed report to the console.

## Project Structure
- **Main Classes**:
  - `Employee`: Represents an employee with fields for ID, name, salary, and manager ID.
  - `EmployeeParser`: Parses the employee data from a CSV file into `Employee` objects.
  - `OrgAnalyzer`: Core class that performs the analysis based on the salary and reporting line criteria.
  - `Main`: Entry point for running the application.

- **Tests**:
  - Unit tests are provided for `OrgAnalyzer` and `EmployeeParser` using JUnit5 to validate salary analysis and reporting line logic.

## Setup and Requirements
- **Java Version**: Java 17
- **Build Tool**: Maven
- **Dependencies**:
  - **JUnit 5**: For unit testing.
  - **OpenCSV**: For parsing CSV files.

## Instructions for Running the Program

### Step 1: Extract the Zip File

Unzip the project file to a directory on your local machine.

```bash
unzip OrgAnalyzer.zip
cd OrgAnalyzer
```

### Step 2: Prepare the CSV File
Create an `employees.csv` file in the project root directory with the following content:

```bash
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

- Id: Unique employee identifier
- firstName: Employee's first name
- lastName: Employee's last name
- salary: Employee's annual salary
- managerId: ID of the employee's manager (leave blank for the CEO)

### Step 3: Build the Project

Navigate to the project directory in your terminal or command prompt, then build the project using Maven:

```bash
mvn clean install
```

### Step 4: Run the Application
To run the application, use Maven's exec plugin:

```bash
mvn exec:java -Dexec.mainClass="com.epam.Main"
```

This will:
- Parse `employees.csv` to load employee data.
- Run the analysis on salary and reporting line length.
- Output the analysis report directly to the console.

**Example Output**
After running the program, you should see output similar to:

```bash
Salary Analysis:
Manager Martin Chekov (ID: 124) earns less than required. Current Salary: 45000.00, Minimum Required Salary: 60000.00, Difference: 15000.00, Average Subordinate Salary: 50000.00

Reporting Line Analysis:
```

### Running Unit Tests
To verify functionality, run the unit tests using:

```bash
mvn test
```

### Additional Notes
- The CEO is identified as the employee with no `managerId`.
- The application assumes that the input CSV file is well-formed.
- The output report is printed to the console for simplicity.