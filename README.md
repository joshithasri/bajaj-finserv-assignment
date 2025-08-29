# Bajaj Finserv Health - Spring Boot Coding Test

## Project Overview
This project is a Spring Boot application built to meet the requirements of the Bajaj Finserv coding test.

### Features
- Sends a POST request on startup to generate a webhook and JWT token.
- Based on the webhook response, constructs the required SQL query.
- Submits the final SQL query to the provided webhook URL using the JWT token in the Authorization header.

## Final SQL Query
SELECT
p.AMOUNT AS SALARY,
CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
TIMESTAMPDIFF(YEAR, e.DOB, DATE(p.PAYMENT_TIME)) AS AGE,
d.DEPARTMENT_NAME
FROM
PAYMENTS p
JOIN
EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN
DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE
DAY(DATE(p.PAYMENT_TIME)) != 1
ORDER BY
p.AMOUNT DESC
LIMIT 1;
