
package com.example.employee_management;

public record EmployeeReportRow(
    Long id,
    String name,
    String email,
    String departmentName,
    Double salary
) {}
