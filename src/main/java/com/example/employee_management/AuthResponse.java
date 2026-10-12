
package com.example.employee_management;

public record AuthResponse(
        String token,
        String name,
        String email
) {
}
