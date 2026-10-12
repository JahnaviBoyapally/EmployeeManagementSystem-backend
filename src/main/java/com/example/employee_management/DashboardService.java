
package com.example.employee_management;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public DashboardSummary getSummary() {

        List<Employee> employees = employeeRepository.findAll();

        long totalEmployees = employees.size();
        long totalDepartments = departmentRepository.count();

        double totalSalary = 0.0;
        long employeesWithSalary = 0;

        Map<String, Long> departmentCounts = new TreeMap<>();

        for (Employee employee : employees) {

            // Calculate salary statistics.
            if (employee.getSalary() != null) {
                totalSalary += employee.getSalary();
                employeesWithSalary++;
            }

            // Count employees in each department.
            String departmentName = "Unassigned";

            if (employee.getDepartment() != null
                    && employee.getDepartment().getName() != null) {
                departmentName = employee.getDepartment().getName();
            }

            departmentCounts.merge(departmentName, 1L, Long::sum);
        }

        double averageSalary = employeesWithSalary == 0
                ? 0.0
                : totalSalary / employeesWithSalary;

        List<DepartmentEmployeeCount> employeesByDepartment =
                departmentCounts.entrySet()
                        .stream()
                        .map(entry -> new DepartmentEmployeeCount(
                                entry.getKey(),
                                entry.getValue()
                        ))
                        .toList();

        return new DashboardSummary(
                totalEmployees,
                totalDepartments,
                totalSalary,
                averageSalary,
                employeesByDepartment
        );
    }
}
