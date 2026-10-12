
package com.example.employee_management;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}
