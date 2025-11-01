package com.apps.medilab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.model.Department;
import com.apps.medilab.service.DepartmentService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@RestController
@RequestMapping("/api/department")
public class DepartmentRestController {
    
    private DepartmentService departmentService;

    @PostMapping
    public Department create(@RequestBody Department entity) {
        return departmentService.create(entity);
    }

    @GetMapping
    public List<Department> list() {
        return departmentService.list();
    }

    @GetMapping("{id}")
    public Department show(@PathVariable Long id) {
        return departmentService.show(id);
    }
     
    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id) {
        departmentService.delete(id);
        return "ok";
    }

    @PutMapping("{id}")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.update(id, department);
    }
       
}
