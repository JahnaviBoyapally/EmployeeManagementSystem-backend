
package com.example.employee_management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT e
        FROM Employee e
        WHERE
            (
                :keyword IS NULL
                OR LOWER(e.name) LIKE LOWER(
                    CONCAT('%', :keyword, '%')
                )
                OR LOWER(e.email) LIKE LOWER(
                    CONCAT('%', :keyword, '%')
                )
            )
            AND
            (
                :departmentId IS NULL
                OR e.department.id = :departmentId
            )
        """)
    Page<Employee> searchEmployees(
            @Param("keyword") String keyword,
            @Param("departmentId") Long departmentId,
            Pageable pageable
    );
}
