
package com.example.employee_management;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(
            DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Get all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(
            @PathVariable Long id) {

        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add department
    @PostMapping
    public ResponseEntity<?> addDepartment(
            @RequestBody Department department) {

        if (department.getName() == null
                || department.getName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Department name is required.");
        }

        if (departmentRepository.existsByNameIgnoreCase(
                department.getName().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Department already exists.");
        }

        department.setName(department.getName().trim());

        Department saved =
                departmentRepository.save(department);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
    }

    // Update department
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department details) {

        return departmentRepository.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    if (details.getName() == null
                            || details.getName().isBlank()) {
                        return ResponseEntity.badRequest()
                                .body("Department name is required.");
                    }

                    String newName = details.getName().trim();

                    boolean duplicate =
                            departmentRepository.findAll().stream()
                                    .anyMatch(d ->
                                            !d.getId().equals(id)
                                            && d.getName().equalsIgnoreCase(
                                                    newName));

                    if (duplicate) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body("Department already exists.");
                    }

                    existing.setName(newName);

                    return ResponseEntity.ok(
                            departmentRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id) {

        return departmentRepository.findById(id)
                .<ResponseEntity<?>>map(department -> {
                    if (department.getEmployees() != null
                            && !department.getEmployees().isEmpty()) {
                        return ResponseEntity.status(
                                HttpStatus.CONFLICT)
                                .body("Move employees to another department before deleting.");
                    }

                    departmentRepository.delete(department);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
