package com.apps.medilab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apps.medilab.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    public Department findByName(String name);

}
