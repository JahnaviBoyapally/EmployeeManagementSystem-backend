
package com.example.employee_management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class EmployeeReportController {

    private final EmployeeReportService reportService;

    public EmployeeReportController(
            EmployeeReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employees")
    public EmployeeReportResponse getEmployeeReport(
            @RequestParam(required = false) Long departmentId) {

        return reportService.getReport(departmentId);
    }
}
