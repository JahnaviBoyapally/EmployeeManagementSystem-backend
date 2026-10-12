
package com.example.employee_management;

import java.util.List;

public record EmployeeReportResponse(
    long totalEmployees,
    double totalSalary,
    double averageSalary,
    List<EmployeeReportRow> employees
) {}
