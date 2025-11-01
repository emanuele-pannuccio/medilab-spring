package com.apps.medilab.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.apps.medilab.model.Department;
import com.apps.medilab.repository.DepartmentRepository;
import com.apps.medilab.requests.DepartmentCreationRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;


    public Department create(Department departmentRequest){
        Department department = new Department();
        department.setName(departmentRequest.getName());
        return departmentRepository.save(department);
    }

    public List<Department> list(){
        return departmentRepository.findAll();
    }

    public Department show(Long id){
        return departmentRepository.findById(id).get();
    }

    public void delete(Long id) throws Exception {
        if (!departmentRepository.existsById(id)) {
            throw new Exception("Department not found: id=" + id);
        }
        departmentRepository.deleteById(id);
    }

    public Department update(Long id, Department department){
        Department d = show(id);

        if(Objects.nonNull(department.getName()) && !"".equalsIgnoreCase(department.getName())){
            d.setName(department.getName());
        }

        return departmentRepository.save(d);
    }


}
