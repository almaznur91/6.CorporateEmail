package ru.itpark.corporateEmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.corporateEmail.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
