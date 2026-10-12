
package com.example.employee_management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // CREATE EMPLOYEE
    public Employee addEmployee(EmployeeRequest request) {
        validateEmployeeRequest(request);

        Department department = departmentRepository
                .findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: "
                                + request.departmentId()));

        Employee employee = new Employee();
        employee.setName(request.name().trim());
        employee.setEmail(request.email().trim());
        employee.setSalary(request.salary());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    // GET ALL EMPLOYEES
    @Transactional(readOnly = true)
    public java.util.List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // GET EMPLOYEE BY ID
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id));
    }

    // UPDATE EMPLOYEE
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        validateEmployeeRequest(request);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id));

        Department department = departmentRepository
                .findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: "
                                + request.departmentId()));

        employee.setName(request.name().trim());
        employee.setEmail(request.email().trim());
        employee.setSalary(request.salary());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    // DELETE EMPLOYEE
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id));

        employeeRepository.delete(employee);
    }

    // SEARCH, FILTER, SORT AND PAGINATE
    @Transactional(readOnly = true)
    public Page<Employee> searchEmployees(
            String keyword,
            Long departmentId,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        if (!"name".equals(sortBy)
                && !"email".equals(sortBy)
                && !"salary".equals(sortBy)) {
            throw new IllegalArgumentException(
                    "Sort field must be name, email, or salary");
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException
                 | NullPointerException ex) {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        if (departmentId != null && departmentId < 1) {
            throw new IllegalArgumentException(
                    "Department ID must be a positive number");
        }

        String searchKeyword =
                keyword == null || keyword.isBlank()
                        ? null
                        : keyword.trim();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return employeeRepository.searchEmployees(
                searchKeyword,
                departmentId,
                pageable
        );
    }

    // BASIC REQUEST VALIDATION
    private void validateEmployeeRequest(EmployeeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Employee request cannot be empty");
        }

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException(
                    "Employee name is required");
        }

        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException(
                    "Employee email is required");
        }

        if (request.salary() == null || request.salary() < 0) {
            throw new IllegalArgumentException(
                    "Salary must be zero or greater");
        }

        if (request.departmentId() == null) {
            throw new IllegalArgumentException(
                    "Department ID is required");
        }
    }
}
