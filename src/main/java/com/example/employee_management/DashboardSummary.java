
package com.example.employee_management;

import java.util.List;

public record DashboardSummary(
        long totalEmployees,
        long totalDepartments,
        double totalSalary,
        double averageSalary,
        List<DepartmentEmployeeCount> employeesByDepartment
) {
}
