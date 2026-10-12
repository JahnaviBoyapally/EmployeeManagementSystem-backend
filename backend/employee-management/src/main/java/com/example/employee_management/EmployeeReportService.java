
package com.example.employee_management;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeReportService {

    private final EmployeeRepository employeeRepository;

    public EmployeeReportService(
            EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeReportResponse getReport(Long departmentId) {

        List<EmployeeReportRow> rows =
                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> {
                            if (departmentId == null) {
                                return true;
                            }

                            return employee.getDepartment() != null
                                    && employee.getDepartment()
                                            .getId()
                                            .equals(departmentId);
                        })
                        .map(employee -> new EmployeeReportRow(
                                employee.getId(),
                                employee.getName(),
                                employee.getEmail(),
                                employee.getDepartment() == null
                                        ? "Unassigned"
                                        : employee.getDepartment().getName(),
                                employee.getSalary()
                        ))
                        .toList();

        double totalSalary = rows.stream()
                .filter(row -> row.salary() != null)
                .mapToDouble(EmployeeReportRow::salary)
                .sum();

        long employeesWithSalary = rows.stream()
                .filter(row -> row.salary() != null)
                .count();

        double averageSalary = employeesWithSalary == 0
                ? 0.0
                : totalSalary / employeesWithSalary;

        return new EmployeeReportResponse(
                rows.size(),
                totalSalary,
                averageSalary,
                rows
        );
    }
}
