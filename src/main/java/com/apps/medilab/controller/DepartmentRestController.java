package com.apps.medilab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.model.Department;
import com.apps.medilab.requests.DepartmentRequestDTO;
import com.apps.medilab.response.ApiResponse;
import com.apps.medilab.service.DepartmentService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> create(@RequestBody DepartmentRequestDTO entity) {
        Department newDepartment = departmentService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDepartment);
    }

    @GetMapping
    public List<Department> list() {
        return departmentService.list();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) {
        return ResponseEntity.ok().body(
            new ApiResponse(200, departmentService.show(id))
        );
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok().body(
            new ApiResponse(200, Map.of("message", "Department deleted successfully"))
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody DepartmentRequestDTO departmentRequest) throws Exception { // <-- Usa un DTO
        return ResponseEntity.ok().body(departmentService.update(id, departmentRequest));
    }
}
