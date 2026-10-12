
package com.example.employee_management;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(

        @NotBlank(message = "Employee name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        String email,

        String department,

        @NotNull(message = "Salary is required")
        @PositiveOrZero(message = "Salary cannot be negative")
        Double salary,

        @NotNull(message = "Department ID is required")
        Long departmentId
) {
}
