# Java Qualifier 1 - SQL Salary Query Project

## Project Overview

This project is a solution to a database query problem involving employee salary records. It demonstrates how to extract specific data from multiple related tables using SQL and integrate it into a Java Spring Boot application.  

---

## Database Schema

There are three tables used in this project:

### 1. DEPARTMENT
Contains details about the department.  

| Column Name      | Description              |
|-----------------|--------------------------|
| DEPARTMENT_ID    | Primary Key             |
| DEPARTMENT_NAME  | Name of the department  |

### 2. EMPLOYEE
Contains employee details.  

| Column Name      | Description                                   |
|-----------------|-----------------------------------------------|
| EMP_ID           | Primary Key                                  |
| FIRST_NAME       | Employee's first name                         |
| LAST_NAME        | Employee's last name                          |
| DOB              | Date of birth                                 |
| GENDER           | Gender of the employee                        |
| DEPARTMENT       | Foreign Key referencing DEPARTMENT_ID        |

### 3. PAYMENTS
Contains salary payment records.  

| Column Name      | Description                                   |
|-----------------|-----------------------------------------------|
| PAYMENT_ID       | Primary Key                                  |
| EMP_ID           | Foreign Key referencing EMP_ID in EMPLOYEE  |
| AMOUNT           | Salary credited                              |
| PAYMENT_TIME     | Date and time of the transaction            |

---

## Problem Statement

Find the **highest salary** that was credited to an employee, **excluding transactions made on the 1st day of any month**. Along with the salary, extract the following details of the employee who received it:

- **Name**: Combine `FIRST_NAME` and `LAST_NAME` into a single column `NAME`.
- **Age**: Calculate the age of the employee.
- **Department**: The department name of the employee.

### Output Format

The result should contain these columns:

| Column Name        | Description                                                  |
|-------------------|--------------------------------------------------------------|
| SALARY             | The highest salary credited (not on the 1st of the month)    |
| NAME               | Employee's full name (`FIRST_NAME LAST_NAME`)               |
| AGE                | Employee's age                                              |
| DEPARTMENT_NAME    | Name of the department                                       |

---

## SQL Solution

Here’s the SQL query written to solve the problem:

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    FLOOR(DATEDIFF(CURDATE(), e.DOB)/365) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1;
